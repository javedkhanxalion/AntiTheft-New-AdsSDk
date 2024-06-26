package com.securityalarm.antitheifproject.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentDetailModuleBinding
import com.bmik.android.sdk.IkmSdkController
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.widgets.IkmWidgetAdLayout
import com.securityalarm.antitheifproject.helper_class.Constants.isServiceRunning
import com.securityalarm.antitheifproject.helper_class.DbHelper
import com.securityalarm.antitheifproject.model.MainMenuModel
import com.securityalarm.antitheifproject.utilities.ANTI_TITLE
import com.securityalarm.antitheifproject.utilities.BaseFragment
import com.securityalarm.antitheifproject.utilities.IS_GRID
import com.securityalarm.antitheifproject.utilities.autoServiceFunctionInternalModule
import com.securityalarm.antitheifproject.utilities.clickWithThrottle
import com.securityalarm.antitheifproject.utilities.getBannerId
import com.securityalarm.antitheifproject.utilities.getNativeLayout
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.loadImage
import com.securityalarm.antitheifproject.utilities.openMobileDataSettings
import com.securityalarm.antitheifproject.utilities.openWifiSettings
import com.securityalarm.antitheifproject.utilities.setImage
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
import com.securityalarm.antitheifproject.utilities.startLottieAnimation

class FragmentDetectionSameFunction :
    BaseFragment<FragmentDetailModuleBinding>(FragmentDetailModuleBinding::inflate) {

    private var isGridLayout: Boolean? = null
    private var model: MainMenuModel? = null
    var sharedPrefUtils: DbHelper? = null
    private var isInternetDialog: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            model = it.getParcelable(ANTI_TITLE) ?: return@let
        }
        _binding?.topLay?.title?.text = model?.maniTextTitle
        _binding?.textView3?.text = model?.bottomText
        sharedPrefUtils = DbHelper(context ?: return)


        _binding?.topLay?.navMenu?.loadImage(context ?: return, R.drawable.back_btn)

        _binding?.run {

            topLay.navMenu.clickWithThrottle {
                findNavController().navigateUp()
            }


            gridLayout.soundIcon.clickWithThrottle {
                findNavController().navigate(
                    R.id.FragmentSoundSelection,
                    bundleOf(ANTI_TITLE to model)
                )
            }

            linearlayout.soundIcon.clickWithThrottle {
                findNavController().navigate(
                    R.id.FragmentSoundSelection,
                    bundleOf(ANTI_TITLE to model)
                )
            }

            topLay.setLayoutBtn.clickWithThrottle {
                loadLayoutDirection(!(isGridLayout ?: return@clickWithThrottle))
            }

        }

        setupBackPressedCallback {
            findNavController().navigateUp()
        }

        sharedPrefUtils?.getBooleanData(context ?: return, IS_GRID, true)?.let {
            loadLayoutDirection(it)
            isGridLayout = it
        }
        loadBanner()
    }

    private fun loadLayoutDirection(isGrid: Boolean) {
        _binding?.run {
            if (isGrid) {
                isGridLayout = true
                sharedPrefUtils?.saveData(context ?: return, IS_GRID, true)
                topLay.setLayoutBtn.setImage(R.drawable.icon_grid)
                gridLayout.topGrid.visibility = View.VISIBLE
                linearlayout.topLinear.visibility = View.GONE
                gridLayout.titleText.text = model?.maniTextTitle
                model?.subMenuIcon?.let { gridLayout.topImage.loadImage(context, it) }
                gridLayout.soundImage.loadImage(context, R.drawable.icon_sound)
                gridLayout.customSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (compoundButton.isPressed) {
                            if (bool) {
                                startLottieAnimation(
                                    activeAnimationView,
                                    activeAnimationViewText,
                                    true
                                )
                                if (!isServiceRunning()) {
                                    autoServiceFunctionInternalModule(true, model?.isActive)
                                } else {
                                    sharedPrefUtils?.setBroadCast(model?.isActive, true)
                                }
                            } else {
                                startLottieAnimation(
                                    activeAnimationView,
                                    activeAnimationViewText,
                                    false
                                )
                                if (isServiceRunning()) {
                                    autoServiceFunctionInternalModule(false, model?.isActive)
                                } else {
                                    sharedPrefUtils?.setBroadCast(model?.isActive, false)
                                }
                            }
                        }
                    }
                }
                gridLayout.flashSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (bool) {
                            sharedPrefUtils?.setBroadCast(
                                model?.isFlash, true
                            )
                        } else {
                            sharedPrefUtils?.setBroadCast(
                                model?.isFlash,
                                false
                            )
                        }
                    }
                }
                gridLayout.vibrationSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (bool) {
                            sharedPrefUtils?.setBroadCast(
                                model?.isVibration, true
                            )
                        } else {
                            sharedPrefUtils?.setBroadCast(
                                model?.isVibration,
                                false
                            )
                        }
                    }
                }
                checkSwitch()
                loadNativeGrid()
            } else {
                isGridLayout = false
                sharedPrefUtils?.saveData(context ?: return, IS_GRID, false)
                topLay.setLayoutBtn.setImage(R.drawable.icon_list)
                linearlayout.topLinear.visibility = View.VISIBLE
                gridLayout.topGrid.visibility = View.GONE
                linearlayout.titleText.text = model?.maniTextTitle ?: return
                model?.subMenuIcon?.let { linearlayout.topImage.loadImage(context, it) }
                linearlayout.soundImage.loadImage(context, R.drawable.icon_sound)
                linearlayout.customSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (bool) {
                            startLottieAnimation(activeAnimationView, activeAnimationViewText, true)
                            if (!isServiceRunning()) {
                                autoServiceFunctionInternalModule(true, model?.isActive)
                            } else {
                                sharedPrefUtils?.setBroadCast(model?.isActive, true)
                            }
                        } else {
                            startLottieAnimation(
                                activeAnimationView,
                                activeAnimationViewText,
                                false
                            )
                            if (isServiceRunning()) {
                                autoServiceFunctionInternalModule(false, model?.isActive)
                            } else {
                                sharedPrefUtils?.setBroadCast(model?.isActive, false)
                            }
                        }
                    }
                }
                linearlayout.flashSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (bool) {
                            sharedPrefUtils?.setBroadCast(
                                model?.isFlash, true
                            )
                        } else {
                            sharedPrefUtils?.setBroadCast(
                                model?.isFlash,
                                false
                            )
                        }
                    }
                }
                linearlayout.vibrationSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (bool) {
                            sharedPrefUtils?.setBroadCast(
                                model?.isVibration, true
                            )
                        } else {
                            sharedPrefUtils?.setBroadCast(
                                model?.isVibration,
                                false
                            )
                        }
                    }
                }
                checkSwitch()
                loadNativeList()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    override fun onPause() {
        super.onPause()
        isInternetDialog = true
        if (!isInternetAvailable(context ?: return)) {
            IkmSdkController.setEnableShowResumeAds(false)
        }
    }

    override fun onResume() {
        super.onResume()
        checkSwitch()
        if (isInternetDialog) {
            if (!isInternetAvailable(context ?: return)) {
                IkmSdkController.setEnableShowResumeAds(false)
//                showInternetDialog(
//                    onPositiveButtonClick = {
//                        isInternetDialog = true
//                        openMobileDataSettings(context ?: requireContext())
//                    },
//                    onNegitiveButtonClick = {
//                        isInternetDialog = true
//                        openWifiSettings(context ?: requireContext())
//                    },
//                    onCloseButtonClick = {
//                    }
//                )
                return
            } else {
                IkmSdkController.setEnableShowResumeAds(true)
            }
        }
    }

    private fun checkSwitch() {
        _binding?.run {
            if (sharedPrefUtils?.chkBroadCast(model?.isActive) ?: return) {
                startLottieAnimation(activeAnimationView, activeAnimationViewText, true)
            } else {
                startLottieAnimation(activeAnimationView, activeAnimationViewText, false)
            }
            gridLayout.customSwitch.isChecked =
                sharedPrefUtils?.chkBroadCast(model?.isActive) ?: return
            linearlayout.customSwitch.isChecked =
                sharedPrefUtils?.chkBroadCast(model?.isActive) ?: return
            gridLayout.flashSwitch.isChecked =
                sharedPrefUtils?.chkBroadCast(model?.isFlash) ?: return
            linearlayout.flashSwitch.isChecked =
                sharedPrefUtils?.chkBroadCast(model?.isFlash) ?: return
            gridLayout.vibrationSwitch.isChecked =
                sharedPrefUtils?.chkBroadCast(model?.isVibration) ?: return
            linearlayout.vibrationSwitch.isChecked =
                sharedPrefUtils?.chkBroadCast(model?.isVibration) ?: return
        }
    }

        private fun loadNativeGrid() {
            val adLayout = LayoutInflater.from(context).inflate(
                getNativeLayout(model?.nativeLayout?:return,_binding?.gridLayout?.nativeExitAd!!),
                null, false
            ) as? IkmWidgetAdLayout
            adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
            adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
            adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
            adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
            adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
            _binding?.gridLayout?.nativeExitAd?.loadAd(
                activity ?: return, R.layout.shimmer_loading_native,
                adLayout!!, model?.nativeId?:return,
                model?.nativeId?:return, object : CustomSDKAdsListenerAdapter() {

                    override fun onAdsLoadFail() {
                        super.onAdsLoadFail()
                        _binding?.gridLayout?.nativeExitAd?.visibility = View.GONE
                    }
                }
            )

        }

        private fun loadNativeList() {
            val adLayout = LayoutInflater.from(context).inflate(
                getNativeLayout(model?.nativeLayout?:return,_binding?.linearlayout?.nativeExitAd!!),
                null, false
            ) as? IkmWidgetAdLayout
            adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
            adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
            adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
            adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
            adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
            _binding?.linearlayout?.nativeExitAd?.loadAd(
                    activity ?: return, R.layout.shimmer_loading_native,
                    adLayout!!, model?.nativeId?:return,
                    model?.nativeId?:return, object : CustomSDKAdsListenerAdapter() {

                        override fun onAdsLoadFail() {
                            super.onAdsLoadFail()
                            _binding?.gridLayout?.nativeExitAd?.visibility = View.GONE
                        }
                    }
                )
        }
    private fun loadBanner() {
        binding?.bannerAds?.loadAd(
            activity, getBannerId(model?.maniTextTitle!!),
            getBannerId(model?.maniTextTitle!!), object : CustomSDKAdsListenerAdapter() {
                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.bannerAds?.visibility = View.GONE
                }
            }
        )
    }


}
