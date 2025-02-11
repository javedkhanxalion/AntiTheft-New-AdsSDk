package com.do_not_douch.antitheifproject.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.FragmentMainMenuActivityBinding
import com.do_not_douch.antitheifproject.adapter.MainMenuGridAdapter
import com.do_not_douch.antitheifproject.adapter.MainMenuLinearAdapter
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.PurchasePrefs
import com.do_not_douch.antitheifproject.ads_manager.showTwoInterAd
import com.do_not_douch.antitheifproject.ads_manager.showTwoInterAdExit
import com.do_not_douch.antitheifproject.helper_class.Constants.isServiceRunning
import com.do_not_douch.antitheifproject.helper_class.DbHelper
import com.do_not_douch.antitheifproject.model.MainMenuModel
import com.do_not_douch.antitheifproject.utilities.ANTI_TITLE
import com.do_not_douch.antitheifproject.utilities.askRatings
import com.do_not_douch.antitheifproject.utilities.AUDIO_PERMISSION
import com.do_not_douch.antitheifproject.utilities.BaseFragment
import com.do_not_douch.antitheifproject.utilities.BottomSheetFragment
import com.do_not_douch.antitheifproject.utilities.IS_GRID
import com.do_not_douch.antitheifproject.utilities.IS_NOTIFICATION
import com.do_not_douch.antitheifproject.utilities.LANG_SCREEN
import com.do_not_douch.antitheifproject.utilities.NOTIFICATION_PERMISSION
import com.do_not_douch.antitheifproject.utilities.PHONE_PERMISSION
import com.do_not_douch.antitheifproject.utilities.appUpdateType
import com.do_not_douch.antitheifproject.utilities.autoServiceFunction
import com.do_not_douch.antitheifproject.utilities.clickWithThrottle
import com.do_not_douch.antitheifproject.utilities.firebaseAnalytics
import com.do_not_douch.antitheifproject.utilities.getMenuListGrid
import com.do_not_douch.antitheifproject.utilities.id_adaptive_banner
import com.do_not_douch.antitheifproject.utilities.id_inter_main_medium
import com.do_not_douch.antitheifproject.utilities.moreApp
import com.do_not_douch.antitheifproject.utilities.privacyPolicy
import com.do_not_douch.antitheifproject.utilities.rateUs
import com.do_not_douch.antitheifproject.utilities.requestCameraPermissionAudio
import com.do_not_douch.antitheifproject.utilities.requestCameraPermissionNotification
import com.do_not_douch.antitheifproject.utilities.setImage
import com.do_not_douch.antitheifproject.utilities.setupBackPressedCallback
import com.do_not_douch.antitheifproject.utilities.shareApp
import com.do_not_douch.antitheifproject.utilities.showRatingDialog
import com.do_not_douch.antitheifproject.utilities.showServiceDialog
import com.do_not_douch.antitheifproject.utilities.showToast
import com.do_not_douch.antitheifproject.utilities.val_banner_main_menu_screen
import com.do_not_douch.antitheifproject.utilities.val_inter_exit_screen
import com.do_not_douch.antitheifproject.utilities.val_inter_main_normal
import com.do_not_douch.antitheifproject.utilities.val_is_inapp
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainMenuFragment :
    BaseFragment<FragmentMainMenuActivityBinding>(FragmentMainMenuActivityBinding::inflate) {

    private var isSplashScreen: Boolean = false
    private var adapterGrid: MainMenuGridAdapter? = null
    private var adapterLinear: MainMenuLinearAdapter? = null
    var sharedPrefUtils: DbHelper? = null
    private var isGridLayout: Boolean? = null
    private var isInternetDialog: Boolean = false
    private var isInternetPermission: Boolean = true
    private var adsManager: AdsManager? = null
    private lateinit var appUpdateManager: AppUpdateManager
    private val RC_APP_UPDATE = 200

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAnalytics("main_menu_fragment_open", "main_menu_fragment_open -->  Click")
        sharedPrefUtils = DbHelper(context ?: return)
        adsManager = AdsManager.appAdsInit(activity ?: return)

        setupBackPressedCallback {
            if (_binding?.navViewLayout?.navigationMain?.isVisible == true) {
                _binding?.navViewLayout?.navigationMain?.visibility = View.GONE
            } else {
                adsManager?.let {
                    showTwoInterAdExit(
                        ads = it,
                        activity = activity ?: return@let,
                        remoteConfigNormal = val_inter_exit_screen,
                        adIdNormal = id_inter_main_medium,
                        tagClass = "main_menu",
                        layout = binding?.mainLayout?.adsLay!!,
                        isBackPress = true
                    ) {
                            findNavController().navigate(R.id.FragmentExitScreen)
                    }

                }
            }
        }
        _binding?.run {
            navViewLayout.backBtn.clickWithThrottle {
                _binding?.navViewLayout?.navigationMain?.visibility = View.GONE
            }
            mainLayout.topLay.setLayoutBtn.clickWithThrottle {
                loadLayoutDirection(!(isGridLayout ?: return@clickWithThrottle))
            }
            mainLayout.topLay.settingBtn.clickWithThrottle {
                findNavController().navigate(R.id.FragmentBuyScreen)
            }
            navViewLayout.rateUsView.clickWithThrottle {
                showRatingDialog(onPositiveButtonClick = { it, _dialog ->
                    if (it >= 1F && it < 3F) {
                        _dialog.dismiss()
                        showToast("Thanks For Your Time")
                    } else if (it in 3F..5F) {
                        _dialog.dismiss()
                        requireContext().rateUs()
                    }
                })
                _binding?.navViewLayout?.navigationMain?.visibility = View.GONE
            }
            navViewLayout.shareAppView.clickWithThrottle {
                requireContext().shareApp()
                _binding?.navViewLayout?.navigationMain?.visibility = View.GONE
            }
            navViewLayout.privacyView.clickWithThrottle {
                requireContext().privacyPolicy()
                _binding?.navViewLayout?.navigationMain?.visibility = View.GONE
            }
            navViewLayout.moreAppView.clickWithThrottle {
                requireContext().moreApp()
                _binding?.navViewLayout?.navigationMain?.visibility = View.GONE
            }
            navViewLayout.languageView.clickWithThrottle {
                firebaseAnalytics(
                    "main_menu_fragment_language_open",
                    "main_menu_fragment_language_open -->  Click"
                )
                findNavController().navigate(R.id.LanguageFragment, bundleOf(LANG_SCREEN to false))
                _binding?.navViewLayout?.navigationMain?.visibility = View.GONE
            }
            navViewLayout.navigationMain.setOnClickListener { }
            navViewLayout.customSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                if (compoundButton.isPressed) {
                    if (bool) {
                        if (!isServiceRunning()) {
                            autoServiceFunction(true)
                            _binding?.navViewLayout?.navigationMain?.visibility = View.GONE
                        }
                    } else {
                        showServiceDialog(
                            onPositiveNoClick = {
                                navViewLayout.customSwitch.isChecked = true
                                _binding?.navViewLayout?.navigationMain?.visibility = View.GONE
                            },
                            onPositiveYesClick = {
                                _binding?.navViewLayout?.navigationMain?.visibility = View.GONE
                                if (isServiceRunning()) {
                                    autoServiceFunction(false)
                                }
                            })
                    }
                }
            }
            mainLayout.topLay.navMenu.clickWithThrottle {
                _binding?.navViewLayout?.navigationMain?.visibility = View.VISIBLE
            }
            sharedPrefUtils?.getBooleanData(context ?: return, IS_GRID, true)?.let {
                loadLayoutDirection(it)
                isGridLayout = it
            }
            if(PurchasePrefs(context).getBoolean("inApp") || !val_is_inapp){
                mainLayout.topLay.settingBtn.visibility=View.GONE
                navViewLayout.viewTop.visibility=View.GONE
                navViewLayout.buyText.visibility=View.GONE
                navViewLayout.buyUs.visibility=View.GONE
            }else{
                mainLayout.topLay.settingBtn.visibility=View.VISIBLE
                navViewLayout.viewTop.visibility=View.VISIBLE
                navViewLayout.buyText.visibility=View.VISIBLE
                navViewLayout.buyUs.visibility=View.VISIBLE
            }
            navViewLayout.viewTop.clickWithThrottle {
                findNavController().navigate(R.id.FragmentBuyScreen)
            }
            mainLayout.topLay.settingBtn.clickWithThrottle {
                findNavController().navigate(R.id.FragmentBuyScreen)
            }
        }
        val drawerView: View = view.findViewById(R.id.drawerLayout)
        if (drawerView is DrawerLayout) {
            drawerView.setDrawerListener(object : DrawerListener {
                override fun onDrawerSlide(view: View, v: Float) {
                    Log.d("drawer_check", "onDrawerSlide: $v")
                }

                override fun onDrawerOpened(view: View) {
                    _binding?.mainLayout?.recycler?.visibility = View.INVISIBLE
                    _binding?.mainLayout?.bannerAds?.visibility = View.INVISIBLE
                }

                override fun onDrawerClosed(view: View) {
                    _binding?.mainLayout?.recycler?.visibility = View.VISIBLE
                    _binding?.mainLayout?.bannerAds?.visibility = View.VISIBLE
                }

                override fun onDrawerStateChanged(i: Int) {
                    Log.d("drawer_check", "onDrawerStateChanged: $i")
                }
            })
        }
        loadBanner()

    }

    private fun loadLayoutDirection(isGrid: Boolean) {
        if (isGrid) {
            isGridLayout = true
            sharedPrefUtils?.saveData(context ?: return, IS_GRID, true)
            _binding?.mainLayout?.topLay?.setLayoutBtn?.setImage(
                R.drawable.icon_grid
            )
            adapterGrid =
                MainMenuGridAdapter(
                    activity ?: return,
                    adsManager ?: return,
                    getMenuListGrid(sharedPrefUtils ?: return)
                )
            val managerLayout = GridLayoutManager(context, 3)
            val spanSizeLookup1 = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    // Return the span size for each item
                    return if (position == 3) {
                        // Return a larger span size for items at positions divisible by 3
                        3
                    } else {
                        // Return the default span size for other items
                        1
                    }
                }
            }
            managerLayout.spanSizeLookup = spanSizeLookup1
            _binding?.mainLayout?.recycler?.layoutManager = managerLayout
            _binding?.mainLayout?.recycler?.adapter = adapterGrid
            adapterGrid?.onClick = { s: MainMenuModel, i: Int ->
                loadFunction(s, i)
            }
        } else {
            isGridLayout = false
            sharedPrefUtils?.saveData(context ?: return, IS_GRID, false)
            adapterLinear =
                MainMenuLinearAdapter(
                    activity ?: return, adsManager ?: return,
                    getMenuListGrid(sharedPrefUtils ?: return)
                )
            _binding?.mainLayout?.topLay?.setLayoutBtn?.setImage(
                R.drawable.icon_list
            )
            val managerLayout = LinearLayoutManager(context)
            managerLayout.orientation = LinearLayoutManager.VERTICAL
            _binding?.mainLayout?.recycler?.layoutManager = managerLayout
            _binding?.mainLayout?.recycler?.adapter = adapterLinear
            adapterLinear?.onClick = { s: MainMenuModel, i: Int ->
                loadFunction(s, i)
            }
        }
    }

    private fun loadFunction(model: MainMenuModel, position: Int) {
        firebaseAnalytics(model.maniTextTitle, "${model.maniTextTitle}_open -->  Click")

        when (position) {
            0 -> {
                adsManager?.let {
                    showTwoInterAd(
                        ads = it,
                        activity = activity ?: return,
                        remoteConfigNormal = val_inter_main_normal,
                        adIdNormal = id_inter_main_medium,
                        tagClass = model.maniTextTitle,
                        isBackPress = false,
                        layout = binding?.mainLayout?.adsLay!!
                    ) {
                    }
                }
                findNavController().navigate(R.id.FragmentInturderDetectionDetail)
            }

            2 -> {
                adsManager?.let {
                    showTwoInterAd(
                        ads = it,
                        activity = activity ?: return,
                        remoteConfigNormal = val_inter_main_normal,
                        adIdNormal = id_inter_main_medium,
                        tagClass = model.maniTextTitle,
                        isBackPress = false,
                        layout = binding?.mainLayout?.adsLay!!
                    ) {
                    }
                }
                findNavController().navigate(
                    R.id.FragmentPasswordDetail,
                    bundleOf(ANTI_TITLE to model)
                )
            }

            else -> {
                if (ContextCompat.checkSelfPermission(context ?: return, AUDIO_PERMISSION) == 0 &&
                    ContextCompat.checkSelfPermission(context ?: return, PHONE_PERMISSION) == 0
                ) {
                    adsManager?.let {
                        showTwoInterAd(
                            ads = it,
                            activity = activity ?: return,
                            remoteConfigNormal = val_inter_main_normal,
                            adIdNormal = id_inter_main_medium,
                            tagClass = model.maniTextTitle,
                            isBackPress = false,
                            layout = binding?.mainLayout?.adsLay!!
                        ) {
                        }
                    }
                    findNavController().navigate(
                        R.id.FragmentDetectionSameFunction,
                        bundleOf(ANTI_TITLE to model)
                    )
                } else {
                    requestCameraPermissionAudio()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    override fun onResume() {
        super.onResume()
        arguments?.let {
            isSplashScreen = it.getBoolean(LANG_SCREEN)
        }
        CoroutineScope(Dispatchers.Main).launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (context?.let {
                        ContextCompat.checkSelfPermission(
                            it,
                            NOTIFICATION_PERMISSION
                        )
                    } != 0) {
                    requestCameraPermissionNotification()
                } else {
                    if (isSplashScreen) askRatings(activity ?: return@launch)
                }
            } else {
                if (isSplashScreen) askRatings(activity ?: return@launch)
            }
            sharedPrefUtils?.getBooleanData(context ?: return@launch, IS_NOTIFICATION, false)?.let {
                _binding?.navViewLayout?.customSwitch?.isChecked = it
            }
            // Initialize AppUpdateManager
            appUpdateManager = AppUpdateManagerFactory.create(context ?: return@launch)
            // Fetch Remote Config and Check for App Update
            checkForUpdate()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isInternetPermission) {
            isInternetDialog = true
        }
    }

    private fun loadBanner() {
        adsManager?.adsBanners()?.loadBanner(
            activity = activity ?: return,
            view = _binding?.mainLayout?.bannerAds!!,
            viewS = _binding?.mainLayout?.shimmerLayout!!,
            addConfig = val_banner_main_menu_screen,
            bannerId = id_adaptive_banner
        ){
            _binding?.mainLayout?.shimmerLayout!!.visibility=View.GONE
        }
    }

    private fun checkForUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            ) {
                when (appUpdateType){
                    0->{
                        // Request the update
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            activity?:return@addOnSuccessListener,
                            RC_APP_UPDATE
                        )
                    }
                    1->{
                        // Request the update
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.FLEXIBLE,
                            activity?:return@addOnSuccessListener,
                            RC_APP_UPDATE
                        )
                    }
                }
            }
        }
    }

}