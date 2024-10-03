package com.do_not_douch.antitheifproject.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.NavigationLayoutBinding
import com.bumptech.glide.Glide
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.FunctionClass.feedBackWithEmail
import com.do_not_douch.antitheifproject.ads_manager.PurchasePrefs
import com.do_not_douch.antitheifproject.ads_manager.showTwoInterAd
import com.do_not_douch.antitheifproject.helper_class.Constants
import com.do_not_douch.antitheifproject.helper_class.Constants.isServiceRunning
import com.do_not_douch.antitheifproject.helper_class.DbHelper
import com.do_not_douch.antitheifproject.service.SystemEventsService
import com.do_not_douch.antitheifproject.utilities.BaseFragment
import com.do_not_douch.antitheifproject.utilities.IS_NOTIFICATION
import com.do_not_douch.antitheifproject.utilities.LANG_SCREEN
import com.do_not_douch.antitheifproject.utilities.PurchaseScreen
import com.do_not_douch.antitheifproject.utilities.autoServiceFunctionIntruder
import com.do_not_douch.antitheifproject.utilities.clickWithThrottle
import com.do_not_douch.antitheifproject.utilities.id_adaptive_banner
import com.do_not_douch.antitheifproject.utilities.id_inter_main_medium
import com.do_not_douch.antitheifproject.utilities.isSplash
import com.do_not_douch.antitheifproject.utilities.moreApp
import com.do_not_douch.antitheifproject.utilities.privacyPolicy
import com.do_not_douch.antitheifproject.utilities.setupBackPressedCallback
import com.do_not_douch.antitheifproject.utilities.shareApp
import com.do_not_douch.antitheifproject.utilities.showRatingDialog
import com.do_not_douch.antitheifproject.utilities.showServiceDialog
import com.do_not_douch.antitheifproject.utilities.val_banner_setting_screen
import com.do_not_douch.antitheifproject.utilities.val_inapp_frequency
import com.do_not_douch.antitheifproject.utilities.val_inter_language_screen
import com.do_not_douch.antitheifproject.utilities.val_is_inapp

class FragmentNavigationScreen :
    BaseFragment<NavigationLayoutBinding>(NavigationLayoutBinding::inflate) {
    private var sharedPrefUtils: DbHelper? = null
    private var isActivated = false
    private var adsManager: AdsManager? = null

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(++PurchaseScreen ==val_inapp_frequency){
            PurchaseScreen =0
            findNavController().navigate(R.id.FragmentBuyScreen, bundleOf("isSplash" to false))
            return
        }
        sharedPrefUtils = DbHelper(context ?: return)
        adsManager = AdsManager.appAdsInit(activity ?: return)

        _binding?.backBtn?.clickWithThrottle {
           findNavController().navigateUp()
        }
        _binding?.customSwitch?.setOnCheckedChangeListener { compoundButton, _bool ->
            if (compoundButton.isPressed) {
                if (_bool) {
                    if (!isServiceRunning()) {
                        autoServiceFunctionIntruder(true,sharedPrefUtils)
                    }
                } else {
                    showServiceDialog(
                        onPositiveNoClick = {
                            _binding?.customSwitch!!.isChecked = true
                        },
                        onPositiveYesClick = {
                            if (isServiceRunning()) {
                                autoServiceFunctionIntruder(false,sharedPrefUtils)
                            }
                        })
                }
            }
        }
        _binding?.languageView?.setOnClickListener {
            adsManager?.let {
                showTwoInterAd(
                    ads = it,
                    activity = activity ?: return@setOnClickListener,
                    remoteConfigNormal = val_inter_language_screen,
                    adIdNormal = id_inter_main_medium,
                    tagClass = "main_menu",
                    isBackPress = false,
                    layout = _binding?.adsLay ?: return@setOnClickListener,
                ) {
                    findNavController().navigate(R.id.LanguageFragment,
                        bundleOf("isSplash" to false))
                }
            }
        }
        _binding?.rateUsView?.setOnClickListener {
            showRatingDialog( onPositiveButtonClick = { it, _dialog ->
            })
        }
        _binding?.shareAppView?.setOnClickListener {
            requireContext().shareApp()
        }
        _binding?.privacyView?.setOnClickListener {
            requireContext().privacyPolicy()
        }
        _binding?.moreAppView?.setOnClickListener {
            requireContext().moreApp()
        }
        _binding?.viewTop?.clickWithThrottle {
            findNavController().navigate(R.id.FragmentBuyScreen, bundleOf("isSplash" to false))
        }
       _binding?.customerSupportView?.setOnClickListener {
            feedBackWithEmail(
                context =activity?:return@setOnClickListener,
                title = "Feed Back",
                message = "User Send Feed Back",
                emailId = "fireitinc.dev@gmail.com"
            )
        }
        _binding?.feedBackView?.setOnClickListener {
            findNavController().navigate(R.id.FragmentFeedBack)
        }
        _binding?.exitAppViewView?.setOnClickListener {
            findNavController().navigate(R.id.FragmentExitScreen)
        }
        loadNative()
        _binding?.run {
        if(PurchasePrefs(context).getBoolean("inApp") || !val_is_inapp){
            viewTop.visibility=View.GONE
            buyText.visibility=View.GONE
            buyUs.visibility=View.GONE
        }else{
            viewTop.visibility=View.VISIBLE
            buyText.visibility=View.VISIBLE
            buyUs.visibility=View.VISIBLE
        }
            }
        setupBackPressedCallback {
            findNavController().navigateUp()
        }
    }

    override fun onResume() {
        super.onResume()
        sharedPrefUtils?.getBooleanData(context ?: return, IS_NOTIFICATION, false)?.let {
            _binding?.customSwitch?.isChecked = it
        }

    }

    private fun checkPermissionOverlay(activity: Activity): Boolean {
        return try {
            if (Settings.canDrawOverlays(activity)) {
                return true
            }
            askPermission(activity)
            false
        } catch (unused: Exception) {
            true
        }
    }

    private fun askPermission(activity: Activity) {
        isSplash = false
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context?.packageName}")
        )
        startActivityForResult(intent, 100)
    }

    private fun loadNative() {
        adsManager?.adsBanners()?.loadCollapsibleBanner(
            activity = activity ?: return,
            view = binding!!.adsView,
            viewS = binding!!.shimmerLayout,
            addConfig = val_banner_setting_screen,
            bannerId = id_adaptive_banner
        ) {
            _binding!!.shimmerLayout.visibility =View.GONE
        }
    }

}