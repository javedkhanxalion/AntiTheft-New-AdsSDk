package com.do_not_douch.antitheifproject.ui

import android.annotation.SuppressLint
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.FragmentDetailModuleBinding
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.do_not_douch.antitheifproject.Admin
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.interfaces.NativeListener
import com.do_not_douch.antitheifproject.ads_manager.showTwoInterAd
import com.do_not_douch.antitheifproject.helper_class.Constants.Intruder_Alarm
import com.do_not_douch.antitheifproject.helper_class.Constants.isServiceRunning
import com.do_not_douch.antitheifproject.helper_class.DbHelper
import com.do_not_douch.antitheifproject.model.MainMenuModel
import com.do_not_douch.antitheifproject.utilities.ANTI_TITLE
import com.do_not_douch.antitheifproject.utilities.BaseFragment
import com.do_not_douch.antitheifproject.utilities.IS_GRID
import com.do_not_douch.antitheifproject.utilities.PurchaseScreen
import com.do_not_douch.antitheifproject.utilities.autoServiceFunctionIntruder
import com.do_not_douch.antitheifproject.utilities.clickWithThrottle
import com.do_not_douch.antitheifproject.utilities.getNativeLayout
import com.do_not_douch.antitheifproject.utilities.id_adaptive_banner
import com.do_not_douch.antitheifproject.utilities.id_inter_main_medium
import com.do_not_douch.antitheifproject.utilities.id_native_screen
import com.do_not_douch.antitheifproject.utilities.loadImage
import com.do_not_douch.antitheifproject.utilities.setImage
import com.do_not_douch.antitheifproject.utilities.setupBackPressedCallback
import com.do_not_douch.antitheifproject.utilities.startLottieAnimation
import com.do_not_douch.antitheifproject.utilities.val_ad_native_password_screen
import com.do_not_douch.antitheifproject.utilities.val_banner_1
import com.do_not_douch.antitheifproject.utilities.val_inapp_frequency
import com.do_not_douch.antitheifproject.utilities.val_inter_language_screen
import com.do_not_douch.antitheifproject.utilities.val_inter_sound_screen

class FragmentPasswordDetail :
    BaseFragment<FragmentDetailModuleBinding>(FragmentDetailModuleBinding::inflate) {

    private var isGridLayout: Boolean? = null
    private var model: MainMenuModel? = null
    private var dbHelper: DbHelper? = null
    private var mComponentName: ComponentName? = null
    private var adsManager: AdsManager? = null
    private var mDevicePolicyManager: DevicePolicyManager? = null
    private var isInternetDialog: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(++PurchaseScreen == val_inapp_frequency){
            PurchaseScreen =0
            findNavController().navigate(R.id.FragmentBuyScreen, bundleOf("isSplash" to false))
            return
        }
        arguments?.let {
            model = it.getParcelable(ANTI_TITLE) ?: return
        }
        adsManager=AdsManager.appAdsInit(activity?:return)
        _binding?.textView3?.text = model?.bottomText
        mDevicePolicyManager =
            context?.getSystemService(AppCompatActivity.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        mComponentName = ComponentName(context ?: return, Admin::class.java)
        dbHelper = DbHelper(context ?: return)
        _binding?.topLay?.title?.text = model?.textTitle
        _binding?.topLay?.navMenu?.loadImage(context ?: return, R.drawable.back_btn)
        loadBanner()
        _binding?.run {
            topLay.navMenu.clickWithThrottle {
                findNavController().navigateUp()
            }
            gridLayout.soundIcon.clickWithThrottle {
                adsManager?.let {
                    showTwoInterAd(
                        ads = it,
                        activity = activity ?: return@let,
                        remoteConfigNormal = val_inter_sound_screen,
                        adIdNormal = id_inter_main_medium,
                        tagClass = "main_menu",
                        isBackPress = false,
                        layout = _binding?.adsLay ?: return@let,
                    ) {

                    }
                }
                findNavController().navigate(
                    R.id.FragmentSoundSelection,
                    bundleOf(ANTI_TITLE to model)
                )
            }
            linearlayout.soundIcon.clickWithThrottle {
                adsManager?.let {
                    showTwoInterAd(
                        ads = it,
                        activity = activity ?: return@let,
                        remoteConfigNormal = val_inter_sound_screen,
                        adIdNormal = id_inter_main_medium,
                        tagClass = "main_menu",
                        isBackPress = false,
                        layout = _binding?.adsLay ?: return@let,
                    ) {

                    }
                }
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
        dbHelper?.getBooleanData(context ?: return, IS_GRID, true)?.let {
            loadLayoutDirection(it)
            isGridLayout = it
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadLayoutDirection(isGrid: Boolean) {
        _binding?.run {
            if (isGrid) {
                isGridLayout = true
                dbHelper?.saveData(context ?: return, IS_GRID, true)
                topLay.setLayoutBtn.setImage(R.drawable.icon_grid)
                gridLayout.topImage.loadImage(context ?: return, model?.subMenuIcon ?: return)
                gridLayout.soundImage.loadImage(context ?: return, R.drawable.icon_sound)
                gridLayout.topGrid.visibility = View.VISIBLE
                linearlayout.topLinear.visibility = View.GONE
                gridLayout.titleText.text = model?.maniTextTitle ?: return
                activeAnimationView.clickWithThrottle {
                    if ((dbHelper?.getBooleanData(context?:return@clickWithThrottle,Intruder_Alarm,true)==true)) {
                            startLottieAnimation(
                                activeAnimationView,
                                activeAnimationViewText,
                                false
                            )
                            dbHelper?.setBroadCast(Intruder_Alarm, false)
                        gridLayout.customSwitch.isChecked=false
                        } else {
                            val devicePolicyManager = mDevicePolicyManager
                            if (devicePolicyManager == null || devicePolicyManager.isAdminActive(
                                    (mComponentName) ?: return@clickWithThrottle
                                )
                            ) {
                                startLottieAnimation(
                                    activeAnimationView,
                                    activeAnimationViewText,
                                    true
                                )
                                dbHelper?.setBroadCast(Intruder_Alarm, true)
                                gridLayout.customSwitch.isChecked=true
                                if (!isServiceRunning()) {
                                    autoServiceFunctionIntruder(true, dbHelper)
                                }
                                return@clickWithThrottle
                            } else {
                                val intent = Intent("android.app.action.ADD_DEVICE_ADMIN")
                                intent.putExtra("android.app.extra.DEVICE_ADMIN", mComponentName)
                                intent.putExtra(
                                    "android.app.extra.ADD_EXPLANATION",
                                    "Administrator description"
                                )
                                startActivityForResult(intent, 3)
                            }
                        }

                }
                gridLayout.customSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (!bool) {
                            startLottieAnimation(
                                activeAnimationView,
                                activeAnimationViewText,
                                false
                            )
                            dbHelper?.setBroadCast(Intruder_Alarm, false)
                        } else {
                            val devicePolicyManager = mDevicePolicyManager
                            if (devicePolicyManager == null || devicePolicyManager.isAdminActive(
                                    (mComponentName) ?: return@setOnCheckedChangeListener
                                )
                            ) {
                                startLottieAnimation(
                                    activeAnimationView,
                                    activeAnimationViewText,
                                    true
                                )
                                dbHelper?.setBroadCast(Intruder_Alarm, true)
                                if (!isServiceRunning()) {
                                    autoServiceFunctionIntruder(true, dbHelper)
                                }
                                return@setOnCheckedChangeListener
                            } else {
                                val intent = Intent("android.app.action.ADD_DEVICE_ADMIN")
                                intent.putExtra("android.app.extra.DEVICE_ADMIN", mComponentName)
                                intent.putExtra(
                                    "android.app.extra.ADD_EXPLANATION",
                                    "Administrator description"
                                )
                                startActivityForResult(intent, 3)
                            }
                        }
                    }
                }
                gridLayout.flashSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (bool) {
                            dbHelper?.setBroadCast(
                                model?.isFlash, true
                            )
                        } else {
                            dbHelper?.setBroadCast(
                                model?.isFlash,
                                false
                            )
                        }
                    }
                }
                gridLayout.vibrationSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (bool) {
                            dbHelper?.setBroadCast(
                                model?.isVibration, true
                            )
                        } else {
                            dbHelper?.setBroadCast(
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
                dbHelper?.saveData(context ?: return, IS_GRID, false)
                topLay.setLayoutBtn.setImage(R.drawable.icon_list)
                linearlayout.topImage.loadImage(context ?: return, model?.subMenuIcon ?: return)
                linearlayout.soundImage.loadImage(context ?: return, R.drawable.icon_sound)
                linearlayout.topLinear.visibility = View.VISIBLE
                gridLayout.topGrid.visibility = View.GONE
                linearlayout.titleText.text =  (model?.textTitle ?: return) +" "+(model?.textTitle ?: return)
                activeAnimationView.clickWithThrottle {
                    if ((dbHelper?.getBooleanData(context?:return@clickWithThrottle,Intruder_Alarm,true)==true)) {
                            startLottieAnimation(
                                activeAnimationView,
                                activeAnimationViewText,
                                false
                            )
                            dbHelper?.setBroadCast(Intruder_Alarm, false)
                        gridLayout.customSwitch.isChecked=false
                        } else {
                        val devicePolicyManager = mDevicePolicyManager
                        if (devicePolicyManager == null || devicePolicyManager.isAdminActive(
                                (mComponentName) ?: return@clickWithThrottle
                            )
                        ) {
                            startLottieAnimation(
                                activeAnimationView,
                                activeAnimationViewText,
                                true
                            )
                            dbHelper?.setBroadCast(Intruder_Alarm, true)
                            gridLayout.customSwitch.isChecked=true
                            if (!isServiceRunning()) {
                                autoServiceFunctionIntruder(true, dbHelper)
                            }
                            return@clickWithThrottle
                        } else {
                            val intent = Intent("android.app.action.ADD_DEVICE_ADMIN")
                            intent.putExtra("android.app.extra.DEVICE_ADMIN", mComponentName)
                            intent.putExtra(
                                "android.app.extra.ADD_EXPLANATION",
                                "Administrator description"
                            )
                            startActivityForResult(intent, 3)
                        }
                    }

                }
                linearlayout.customSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (!bool) {
                            startLottieAnimation(
                                activeAnimationView,
                                activeAnimationViewText,
                                false
                            )
                            dbHelper?.setBroadCast(Intruder_Alarm, false)
                        } else {
                            val devicePolicyManager = mDevicePolicyManager
                            if (devicePolicyManager == null || devicePolicyManager.isAdminActive(
                                    (mComponentName) ?: return@setOnCheckedChangeListener
                                )
                            ) {
                                startLottieAnimation(
                                    activeAnimationView,
                                    activeAnimationViewText,
                                    true
                                )
                                dbHelper?.setBroadCast(Intruder_Alarm, true)
                                if (!isServiceRunning()) {
                                    autoServiceFunctionIntruder(true, dbHelper)
                                }
                                return@setOnCheckedChangeListener
                            } else {
                                val intent = Intent("android.app.action.ADD_DEVICE_ADMIN")
                                intent.putExtra("android.app.extra.DEVICE_ADMIN", mComponentName)
                                intent.putExtra(
                                    "android.app.extra.ADD_EXPLANATION",
                                    "Administrator description"
                                )
                                startActivityForResult(intent, 3)
                            }
                        }
                    }
                }
                linearlayout.flashSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (bool) {
                            dbHelper?.setBroadCast(
                                model?.isFlash, true
                            )
                        } else {
                            dbHelper?.setBroadCast(
                                model?.isFlash,
                                false
                            )
                        }
                    }
                }
                linearlayout.vibrationSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (bool) {
                            dbHelper?.setBroadCast(
                                model?.isVibration, true
                            )
                        } else {
                            dbHelper?.setBroadCast(
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
            if (dbHelper?.chkBroadCast(Intruder_Alarm) ?: return) {
                startLottieAnimation(activeAnimationView, activeAnimationViewText, true)
            } else {
                startLottieAnimation(activeAnimationView, activeAnimationViewText, false)
            }
            gridLayout.customSwitch.isChecked =
                dbHelper?.chkBroadCast(Intruder_Alarm) ?: return
            linearlayout.customSwitch.isChecked =
                dbHelper?.chkBroadCast(Intruder_Alarm) ?: return
            gridLayout.flashSwitch.isChecked =
                dbHelper?.chkBroadCast(model?.isFlash)
                    ?: return
            linearlayout.flashSwitch.isChecked =
                dbHelper?.chkBroadCast(model?.isFlash)
                    ?: return
            gridLayout.vibrationSwitch.isChecked =
                dbHelper?.chkBroadCast(model?.isVibration)
                    ?: return
            linearlayout.vibrationSwitch.isChecked =
                dbHelper?.chkBroadCast(model?.isVibration)
                    ?: return
        }
    }

        private fun loadNativeGrid() {
            if(!val_ad_native_password_screen){
                _binding?.gridLayout?.nativeExitAd?.visibility = View.GONE
                _binding?.gridLayout?.shimmerLayout?.visibility = View.GONE
                return
            }
            val adView = LayoutInflater.from(context).inflate(
                getNativeLayout(model?.nativeLayout?:return,_binding?.gridLayout?.nativeExitAd!!,context?:return),
                null, false
            ) as NativeAdView
            adsManager?.nativeAds()?.loadNativeAd(
                activity ?: return,
                val_ad_native_password_screen,
                id_native_screen,
                object : NativeListener {
                    override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                        if (isAdded && isVisible && !isDetached) {
                            _binding?.gridLayout?.nativeExitAd?.visibility = View.GONE
                            _binding?.gridLayout?.shimmerLayout?.visibility = View.GONE
                            if (isAdded && isVisible && !isDetached) {
                                adsManager?.nativeAds()
                                    ?.nativeViewMediaSplashSplash(context?:return,currentNativeAd ?: return, adView)
                                _binding?.gridLayout?.nativeExitAd?.removeAllViews()
                                _binding?.gridLayout?.nativeExitAd?.addView(adView)
                            }
                        }
                        super.nativeAdLoaded(currentNativeAd)
                    }

                    override fun nativeAdFailed(loadAdError: LoadAdError) {
                        if (isAdded && isVisible && !isDetached) {
                            _binding?.gridLayout?.nativeExitAd?.visibility = View.GONE
                            _binding?.gridLayout?.shimmerLayout?.visibility = View.GONE
                        }
                        super.nativeAdFailed(loadAdError)
                    }

                    override fun nativeAdValidate(string: String) {
                        if (isAdded && isVisible && !isDetached) {
                            _binding?.gridLayout?.nativeExitAd?.visibility = View.GONE
                            _binding?.gridLayout?.shimmerLayout?.visibility = View.GONE
                        }
                        super.nativeAdValidate(string)
                    }

                })
        }

        private fun loadNativeList() {
            if(!val_ad_native_password_screen){
                _binding?.linearlayout?.nativeExitAd?.visibility = View.GONE
                _binding?.linearlayout?.shimmerLayout?.visibility = View.GONE
                return
            }
            val adView = LayoutInflater.from(context).inflate(
                getNativeLayout(model?.nativeLayout?:return,_binding?.linearlayout?.nativeExitAd!!,context?:return),
                null, false
            ) as NativeAdView
            adsManager?.nativeAds()?.loadNativeAd(
                activity ?: return,
                val_ad_native_password_screen,
                id_native_screen,
                object : NativeListener {
                    override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                        if (isAdded && isVisible && !isDetached) {
                            _binding?.linearlayout?.nativeExitAd?.visibility = View.VISIBLE
                            _binding?.linearlayout?.shimmerLayout?.visibility = View.GONE

                            adsManager?.nativeAds()?.nativeViewMediaSplashSplash(context?:return,currentNativeAd ?: return, adView)
                            _binding?.linearlayout?.nativeExitAd?.removeAllViews()
                            _binding?.linearlayout?.nativeExitAd?.addView(adView)
                        }
                        super.nativeAdLoaded(currentNativeAd)
                    }

                    override fun nativeAdFailed(loadAdError: LoadAdError) {
                        if (isAdded && isVisible && !isDetached) {
                            _binding?.linearlayout?.nativeExitAd?.visibility = View.GONE
                            _binding?.linearlayout?.shimmerLayout?.visibility = View.GONE
                        }
                        super.nativeAdFailed(loadAdError)
                    }

                    override fun nativeAdValidate(string: String) {
                        if (isAdded && isVisible && !isDetached) {
                            _binding?.linearlayout?.nativeExitAd?.visibility = View.GONE
                            _binding?.linearlayout?.shimmerLayout?.visibility = View.GONE
                        }
                        super.nativeAdValidate(string)
                    }


                })
        }

    private fun loadBanner() {
        adsManager?.adsBanners()?.loadBanner(
            activity = activity?:return,
            view = _binding?.bannerAds!!,
            viewS = _binding?.shimmerLayout!!,
            addConfig = val_banner_1,
            bannerId = id_adaptive_banner
        ){
            _binding?.shimmerLayout!!.visibility=View.GONE
        }
    }


}