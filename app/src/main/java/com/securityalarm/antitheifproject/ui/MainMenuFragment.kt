package com.securityalarm.antitheifproject.ui

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
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentMainMenuActivityBinding
import com.securityalarm.antitheifproject.adapter.MainMenuGridAdapter
import com.securityalarm.antitheifproject.adapter.MainMenuLinearAdapter
import com.securityalarm.antitheifproject.ads_manager.AdsManager
import com.securityalarm.antitheifproject.ads_manager.showTwoInterAd
import com.securityalarm.antitheifproject.ads_manager.showTwoInterAdExit
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
import com.securityalarm.antitheifproject.utilities.clickWithThrottle
import com.securityalarm.antitheifproject.utilities.firebaseAnalytics
import com.securityalarm.antitheifproject.utilities.getMenuListGrid
import com.securityalarm.antitheifproject.utilities.id_banner_main_screen
import com.securityalarm.antitheifproject.utilities.id_inter_main_medium
import com.securityalarm.antitheifproject.utilities.id_inter_main_normal
import com.securityalarm.antitheifproject.utilities.moreApp
import com.securityalarm.antitheifproject.utilities.privacyPolicy
import com.securityalarm.antitheifproject.utilities.rateUs
import com.securityalarm.antitheifproject.utilities.requestCameraPermissionAudio
import com.securityalarm.antitheifproject.utilities.requestCameraPermissionNotification
import com.securityalarm.antitheifproject.utilities.setImage
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
import com.securityalarm.antitheifproject.utilities.shareApp
import com.securityalarm.antitheifproject.utilities.showRatingDialog
import com.securityalarm.antitheifproject.utilities.showServiceDialog
import com.securityalarm.antitheifproject.utilities.showToast
import com.securityalarm.antitheifproject.utilities.val_banner_main_menu_screen
import com.securityalarm.antitheifproject.utilities.val_inter_exit_screen
import com.securityalarm.antitheifproject.utilities.val_inter_main_normal

class MainMenuFragment :
    BaseFragment<FragmentMainMenuActivityBinding>(FragmentMainMenuActivityBinding::inflate) {

    private var adapterGrid: MainMenuGridAdapter? = null
    private var adapterLinear: MainMenuLinearAdapter? = null
    var sharedPrefUtils: DbHelper? = null
    private var isGridLayout: Boolean? = null
    private var isInternetDialog: Boolean = false
    private var isInternetPermission: Boolean = true
    private var adsManager: AdsManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAnalytics("main_menu_fragment_open", "main_menu_fragment_open -->  Click")
        sharedPrefUtils = DbHelper(context ?: return)
        adsManager = AdsManager.appAdsInit(activity ?: return)
        // Register the receiver for local broadcasts
        // Register the receiver for connectivity changes


//        if (IkmSdkUtils.isUserIAPAvailable()) {
//            _binding?.navViewLayout?.viewTop?.visibility = View.INVISIBLE
//            _binding?.navViewLayout?.buyText?.visibility = View.INVISIBLE
//            _binding?.navViewLayout?.rateUs?.visibility = View.INVISIBLE
//        }
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
                        if (it == 1) {
                            findNavController().navigate(R.id.FragmentExitScreen)
                        } else {
                            val bottomSheetFragment =
                                BottomSheetFragment(activity ?: return@showTwoInterAdExit)
                            bottomSheetFragment.show(
                                fragmentManager ?: return@showTwoInterAdExit,
                                bottomSheetFragment.tag
                            )
                        }
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
                        adIdNormal = id_inter_main_normal,
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
                        adIdNormal = id_inter_main_normal,
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
                            adIdNormal = id_inter_main_normal,
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), NOTIFICATION_PERMISSION) != 0) {
//                _binding?.mainLayout?.hideAd?.visibility = View.VISIBLE
                requestCameraPermissionNotification()
            } else {
//                _binding?.mainLayout?.hideAd?.visibility = View.GONE
            }
        } else {
//            _binding?.mainLayout?.hideAd?.visibility = View.GONE
        }
        sharedPrefUtils?.getBooleanData(context ?: return, IS_NOTIFICATION, false)?.let {
            _binding?.navViewLayout?.customSwitch?.isChecked = it
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), NOTIFICATION_PERMISSION) != 0) {
//                _binding?.mainLayout?.hideAd?.visibility = View.VISIBLE
            }
        } else {
//            _binding?.mainLayout?.hideAd?.visibility = View.VISIBLE
        }
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
            bannerId = id_banner_main_screen
        ) {
        }
    }

}