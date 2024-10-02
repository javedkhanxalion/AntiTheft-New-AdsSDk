package com.do_not_douch.antitheifproject.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.FragmentDetailModuleBinding
import com.do_not_douch.antitheifproject.ads_manager.AdsBanners
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.interfaces.NativeListener
import com.do_not_douch.antitheifproject.helper_class.Constants.isServiceRunning
import com.do_not_douch.antitheifproject.helper_class.DbHelper
import com.do_not_douch.antitheifproject.model.MainMenuModel
import com.do_not_douch.antitheifproject.utilities.ANTI_TITLE
import com.do_not_douch.antitheifproject.utilities.BaseFragment
import com.do_not_douch.antitheifproject.utilities.IS_GRID
import com.do_not_douch.antitheifproject.utilities.autoServiceFunctionInternalModule
import com.do_not_douch.antitheifproject.utilities.clickWithThrottle
import com.do_not_douch.antitheifproject.utilities.getNativeLayout
import com.do_not_douch.antitheifproject.utilities.id_banner_1
import com.do_not_douch.antitheifproject.utilities.loadImage
import com.do_not_douch.antitheifproject.utilities.setImage
import com.do_not_douch.antitheifproject.utilities.setupBackPressedCallback
import com.do_not_douch.antitheifproject.utilities.startLottieAnimation
import com.do_not_douch.antitheifproject.utilities.val_banner_1
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class FragmentDetectionSameFunction :
    BaseFragment<FragmentDetailModuleBinding>(FragmentDetailModuleBinding::inflate) {

    private var isGridLayout: Boolean? = null
    private var model: MainMenuModel? = null
    var sharedPrefUtils: DbHelper? = null
    private var adsManager: AdsManager? = null
    private var isInternetDialog: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            model = it.getParcelable(ANTI_TITLE) ?: return@let
        }
        adsManager = AdsManager.appAdsInit(activity ?: return)
        _binding?.topLay?.title?.text = model?.textTitle
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
            topLay.settingBtn.clickWithThrottle {
                findNavController().navigate(R.id.FragmentBuyScreen)
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
                activeAnimationView.clickWithThrottle {
                    if (sharedPrefUtils?.getBooleanData(
                            context ?: return@clickWithThrottle,
                            model?.isActive,
                            true
                        ) == false
                    ) {

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
                        gridLayout.customSwitch.isChecked = true
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
                        gridLayout.customSwitch.isChecked = false
                    }

                }
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
                activeAnimationView.clickWithThrottle {
                    if (sharedPrefUtils?.getBooleanData(
                            context ?: return@clickWithThrottle,
                            model?.isActive,
                            true
                        ) == false
                    ) {

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
                        linearlayout.customSwitch.isChecked = true
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
                        linearlayout.customSwitch.isChecked = false
                    }

                }
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
    }

    override fun onResume() {
        super.onResume()
        checkSwitch()
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
        val adView = LayoutInflater.from(context).inflate(
            getNativeLayout(
                model?.nativeLayout ?: return,
                _binding?.gridLayout?.nativeExitAd!!,
                context ?: return
            ),
            null, false
        ) as NativeAdView
        adsManager?.nativeAds()?.loadNativeAd(
            activity ?: return,
            model?.remoteValue ?: return,
            model?.idAds ?: return,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    if (!isAdded && !isVisible && isDetached) {
                        return
                    }
                    _binding?.gridLayout?.nativeExitAd?.visibility = View.VISIBLE
                    _binding?.gridLayout?.shimmerLayout?.visibility = View.GONE
                    if (isAdded && isVisible && !isDetached) {
                        adsManager?.nativeAds()?.nativeViewPolicy(context?:return,currentNativeAd ?: return, adView)
                        _binding?.gridLayout?.nativeExitAd?.removeAllViews()
                        _binding?.gridLayout?.nativeExitAd?.addView(adView)
                    }

                    super.nativeAdLoaded(currentNativeAd)
                }

                override fun nativeAdFailed(loadAdError: LoadAdError) {
                    _binding?.gridLayout?.nativeExitAd?.visibility = View.GONE
                    _binding?.gridLayout?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdFailed(loadAdError)
                }

                override fun nativeAdValidate(string: String) {
                    _binding?.gridLayout?.nativeExitAd?.visibility = View.GONE
                    _binding?.gridLayout?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdValidate(string)
                }


            })
    }

    private fun loadNativeList() {
        val adView = LayoutInflater.from(context).inflate(
            getNativeLayout(
                model?.nativeLayout ?: return,
                _binding?.linearlayout?.nativeExitAd!!,
                context ?: return
            ),
            null, false
        ) as NativeAdView
        adsManager?.nativeAds()?.loadNativeAd(
            activity ?: return,
            model?.remoteValue ?: return,
            model?.idAds ?: return,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    if (!isAdded && !isVisible && isDetached) {
                        return
                    }
                    _binding?.linearlayout?.nativeExitAd?.visibility = View.VISIBLE
                    _binding?.linearlayout?.shimmerLayout?.visibility = View.GONE
                    if (isAdded && isVisible && !isDetached) {
                        adsManager?.nativeAds()?.nativeViewPolicy(context?:return,currentNativeAd ?: return, adView)
                        _binding?.linearlayout?.nativeExitAd?.removeAllViews()
                        _binding?.linearlayout?.nativeExitAd?.addView(adView)
                    }
                    super.nativeAdLoaded(currentNativeAd)
                }

                override fun nativeAdFailed(loadAdError: LoadAdError) {
                    _binding?.linearlayout?.nativeExitAd?.visibility = View.GONE
                    _binding?.linearlayout?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdFailed(loadAdError)
                }

                override fun nativeAdValidate(string: String) {
                    _binding?.linearlayout?.nativeExitAd?.visibility = View.GONE
                    _binding?.linearlayout?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdValidate(string)
                }


            })
    }

    private fun loadBanner() {
        adsManager?.adsBanners()?.loadBanner(
            activity = activity ?: return,
            view = _binding?.bannerAds!!,
            viewS = _binding?.shimmerLayout!!,
            addConfig = val_banner_1,
            bannerId = id_banner_1
        ){
            _binding?.shimmerLayout!!.visibility=View.GONE
        }
    }


}
