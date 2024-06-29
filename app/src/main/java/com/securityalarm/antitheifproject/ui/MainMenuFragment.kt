package com.securityalarm.antitheifproject.ui

import MainMenuGridAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentMainMenuActivityBinding
import com.bmik.android.sdk.IkmSdkController
import com.bmik.android.sdk.SDKBaseController
import com.bmik.android.sdk.listener.CommonAdsListenerAdapter
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.listener.SDKDialogLoadingCallback
import com.bmik.android.sdk.utils.IkmSdkUtils
import com.securityalarm.antitheifproject.adapter.MainMenuLinearAdapter
import com.securityalarm.antitheifproject.helper_class.Constants.isServiceRunning
import com.securityalarm.antitheifproject.helper_class.DbHelper
import com.securityalarm.antitheifproject.model.MainMenuModel
import com.securityalarm.antitheifproject.utilities.ANTI_TITLE
import com.securityalarm.antitheifproject.utilities.AUDIO_PERMISSION
import com.securityalarm.antitheifproject.utilities.BaseFragment
import com.securityalarm.antitheifproject.utilities.BottomSheetFragment
import com.securityalarm.antitheifproject.utilities.IS_GRID
import com.securityalarm.antitheifproject.utilities.IS_NOTIFICATION
import com.securityalarm.antitheifproject.utilities.LANG_SCREEN
import com.securityalarm.antitheifproject.utilities.NOTIFICATION_PERMISSION
import com.securityalarm.antitheifproject.utilities.PHONE_PERMISSION
import com.securityalarm.antitheifproject.utilities.autoServiceFunction
import com.securityalarm.antitheifproject.utilities.checkNotificationPermission
import com.securityalarm.antitheifproject.utilities.clickWithThrottle
import com.securityalarm.antitheifproject.utilities.firebaseAnalytics
import com.securityalarm.antitheifproject.utilities.getMenuListGrid
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.moreApp
import com.securityalarm.antitheifproject.utilities.openMobileDataSettings
import com.securityalarm.antitheifproject.utilities.openWifiSettings
import com.securityalarm.antitheifproject.utilities.privacyPolicy
import com.securityalarm.antitheifproject.utilities.rateUs
import com.securityalarm.antitheifproject.utilities.requestCameraPermissionAudio
import com.securityalarm.antitheifproject.utilities.requestCameraPermissionNotification
import com.securityalarm.antitheifproject.utilities.setImage
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
import com.securityalarm.antitheifproject.utilities.shareApp
import com.securityalarm.antitheifproject.utilities.showInternetDialog
import com.securityalarm.antitheifproject.utilities.showRatingDialog
import com.securityalarm.antitheifproject.utilities.showServiceDialog
import com.securityalarm.antitheifproject.utilities.showToast

class MainMenuFragment : BaseFragment<FragmentMainMenuActivityBinding>(FragmentMainMenuActivityBinding::inflate) {

    private var adapterGrid: MainMenuGridAdapter? = null
    private var adapterLinear: MainMenuLinearAdapter? = null
    var sharedPrefUtils: DbHelper? = null
    private var isGridLayout: Boolean? = null
    private var isInternetDialog: Boolean = false
    private var isInternetPermission: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAnalytics("main_menu_fragment_open", "main_menu_fragment_open -->  Click")
        sharedPrefUtils = DbHelper(context ?: return)
        if(IkmSdkUtils.isUserIAPAvailable()){
            _binding?.navView?.viewTop?.visibility=View.INVISIBLE
            _binding?.navView?.buyText?.visibility=View.INVISIBLE
            _binding?.navView?.rateUs?.visibility=View.INVISIBLE
        }
        setupBackPressedCallback {
            if (_binding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
                _binding?.drawerLayout?.closeDrawer(GravityCompat.START)
            } else {
                SDKBaseController.getInstance().showInterstitialAds(
                    activity,
                    screen = "exit_app",
                    trackingScreen = "exit_app",
                    showLoading = true,
                    loadingCallback = object : SDKDialogLoadingCallback {
                        override fun onClose() {
//                            _binding?.mainLayout?.bannerAds?.visibility=View.GONE
                        }
                        override fun onShow() {
//                            _binding?.mainLayout?.bannerAds?.visibility=View.VISIBLE
                        }
                    },
                    adsListener = object : CommonAdsListenerAdapter() {
                        override fun onAdsShowFail(errorCode: Int) {
                            val bottomSheetFragment = BottomSheetFragment(activity?:return)
                            bottomSheetFragment.show(fragmentManager?:return, bottomSheetFragment.tag)
                        }
                        override fun onAdsDismiss() {
                            findNavController().navigate(R.id.FragmentExitScreen)
                        }
                    }
                )
            }
        }
        _binding?.run {
            mainLayout.topLay.setLayoutBtn.clickWithThrottle {
                loadLayoutDirection(!(isGridLayout ?: return@clickWithThrottle))
            }
            mainLayout.topLay.settingBtn.clickWithThrottle {
            }
            navView.rateUsView.clickWithThrottle {
                showRatingDialog(onPositiveButtonClick = { it, _dialog ->
                    if (it >= 1F && it < 3F) {
                        _dialog.dismiss()
                        showToast("Thanks For Your Time")
                    } else if (it in 3F..5F) {
                        _dialog.dismiss()
                        requireContext().rateUs()
                    }
                })
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            navView.shareAppView.clickWithThrottle {
                requireContext().shareApp()
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            navView.privacyView.clickWithThrottle {
                requireContext().privacyPolicy()
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            navView.moreAppView.clickWithThrottle {
                requireContext().moreApp()
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            navView.languageView.clickWithThrottle {
                firebaseAnalytics(
                    "main_menu_fragment_language_open",
                    "main_menu_fragment_language_open -->  Click"
                )
                findNavController().navigate(R.id.LanguageFragment, bundleOf(LANG_SCREEN to false))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            navView.navigationMain.setOnClickListener { }
            navView.customSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                if (compoundButton.isPressed) {
                    if (bool) {
                        if (!isServiceRunning()) {
                            autoServiceFunction(true)
                            drawerLayout.closeDrawer(GravityCompat.START)
                        }
                    } else {
                        drawerLayout.closeDrawer(GravityCompat.START)
                        showServiceDialog(
                            onPositiveNoClick = {
                                navView.customSwitch.isChecked = true
                            },
                            onPositiveYesClick = {
                                if (isServiceRunning()) {
                                    autoServiceFunction(false)
                                }
                            })
                    }
                }
            }
            mainLayout.topLay.navMenu.clickWithThrottle {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            sharedPrefUtils?.getBooleanData(context ?: return, IS_GRID, true)?.let {
                loadLayoutDirection(it)
                isGridLayout = it
            }
        }
//        checkNotificationPermission(_binding?.mainLayout?.hideAd!!)
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
                MainMenuGridAdapter(activity ?: return, getMenuListGrid(sharedPrefUtils ?: return))
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
                    activity ?: return,
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
                SDKBaseController.getInstance().showInterstitialAds(
                    activity ?: return,
                    screen = "home_tabanyfuntion",
                    trackingScreen = "home_tabanyfuntion",
                    showLoading = true,
                    loadingCallback = object : SDKDialogLoadingCallback {
                        override fun onClose() {
                            _binding?.mainLayout?.bannerAds?.visibility=View.GONE
                        }

                        override fun onShow() {
                            _binding?.mainLayout?.bannerAds?.visibility=View.VISIBLE
                        }
                    },
                    adsListener = object : CommonAdsListenerAdapter() {
                        override fun onAdsShowFail(errorCode: Int) {
                            findNavController().navigate(R.id.FragmentInturderDetectionDetail)
                        }

                        override fun onAdsDismiss() {
                            findNavController().navigate(R.id.FragmentInturderDetectionDetail)
                        }
                    }
                )
            }

            2 -> {
                SDKBaseController.getInstance().showInterstitialAds(
                    activity ?: return,
                    screen = "home_tabanyfuntion",
                    trackingScreen = "home_tabanyfuntion",
                    showLoading = true,
                    loadingCallback = object : SDKDialogLoadingCallback {
                        override fun onClose() {
                            _binding?.mainLayout?.bannerAds?.visibility=View.GONE
                        }

                        override fun onShow() {
                            _binding?.mainLayout?.bannerAds?.visibility=View.VISIBLE
                        }
                    },
                    adsListener = object : CommonAdsListenerAdapter() {
                        override fun onAdsShowFail(errorCode: Int) {
                            findNavController().navigate(
                                R.id.FragmentPasswordDetail,
                                bundleOf(ANTI_TITLE to model)
                            )
                        }

                        override fun onAdsDismiss() {
                            findNavController().navigate(
                                R.id.FragmentPasswordDetail,
                                bundleOf(ANTI_TITLE to model)
                            )
                        }
                    }
                )
            }

            else -> {
                if (ContextCompat.checkSelfPermission(
                        context ?: return,
                        AUDIO_PERMISSION
                    ) == 0 &&
                    ContextCompat.checkSelfPermission(
                        context ?: return,
                        PHONE_PERMISSION
                    ) == 0
                ) {
                    SDKBaseController.getInstance().showInterstitialAds(
                        activity ?: return,
                        screen = "home_tabanyfuntion",
                        trackingScreen = "home_tabanyfuntion",
                        showLoading = true,
                        loadingCallback = object : SDKDialogLoadingCallback {
                            override fun onClose() {
                                _binding?.mainLayout?.bannerAds?.visibility=View.GONE
                            }

                            override fun onShow() {
                                _binding?.mainLayout?.bannerAds?.visibility=View.VISIBLE
                            }
                        },
                        adsListener = object : CommonAdsListenerAdapter() {
                            override fun onAdsShowFail(errorCode: Int) {
                                findNavController().navigate(
                                    R.id.FragmentDetectionSameFunction,
                                    bundleOf(ANTI_TITLE to model)
                                )
                            }

                            override fun onAdsDismiss() {
                                findNavController().navigate(
                                    R.id.FragmentDetectionSameFunction,
                                    bundleOf(ANTI_TITLE to model)
                                )
                            }
                        }
                    )
                } else {
                    isInternetPermission=false
                    _binding?.mainLayout?.hideAd?.visibility = View.VISIBLE
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), NOTIFICATION_PERMISSION) != 0) {
                _binding?.mainLayout?.hideAd?.visibility = View.VISIBLE
                requestCameraPermissionNotification(_binding?.mainLayout?.hideAd)
            }else{
                _binding?.mainLayout?.hideAd?.visibility=View.GONE
            }
        }else{
            _binding?.mainLayout?.hideAd?.visibility=View.GONE
        }
        sharedPrefUtils?.getBooleanData(context ?: return, IS_NOTIFICATION, false)?.let {
            _binding?.navView?.customSwitch?.isChecked = it
        }
        if (isInternetDialog) {
            if (!isInternetAvailable(context ?: return)) {
                IkmSdkController.setEnableShowResumeAds(false)
                showInternetDialog(
                    onPositiveButtonClick = {
                        isInternetDialog = true
                        openMobileDataSettings(context ?: requireContext())
                    },
                    onNegitiveButtonClick = {
                        isInternetDialog = true
                        openWifiSettings(context ?: requireContext())
                    },
                    onCloseButtonClick = {
                    }
                )
                return
            }else{
                IkmSdkController.setEnableShowResumeAds(true)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), NOTIFICATION_PERMISSION) != 0) {
                _binding?.mainLayout?.hideAd?.visibility = View.VISIBLE
            }
        }else{
            _binding?.mainLayout?.hideAd?.visibility = View.VISIBLE
        }
         if (!isInternetAvailable(context ?: return)) {
            IkmSdkController.setEnableShowResumeAds(false)
        }
        if(isInternetPermission) {
            isInternetDialog = true
        }
    }

    private fun loadBanner() {
        binding?.mainLayout?.bannerAds?.loadAd(
            activity, "home_banner",
            "home_banner", object : CustomSDKAdsListenerAdapter() {

                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.mainLayout?.bannerAds?.visibility = View.GONE
                }
            }
        )
    }

}