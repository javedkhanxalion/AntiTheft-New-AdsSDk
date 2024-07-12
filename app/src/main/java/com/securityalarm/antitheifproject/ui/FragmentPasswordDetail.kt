package com.securityalarm.antitheifproject.ui

import android.annotation.SuppressLint
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentDetailModuleBinding
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.securityalarm.antitheifproject.Admin
import com.securityalarm.antitheifproject.ads_manager.AdsBanners
import com.securityalarm.antitheifproject.ads_manager.AdsManager
import com.securityalarm.antitheifproject.ads_manager.interfaces.NativeListener
import com.securityalarm.antitheifproject.helper_class.Constants.Intruder_Alarm
import com.securityalarm.antitheifproject.helper_class.Constants.isServiceRunning
import com.securityalarm.antitheifproject.helper_class.DbHelper
import com.securityalarm.antitheifproject.model.MainMenuModel
import com.securityalarm.antitheifproject.utilities.ANTI_TITLE
import com.securityalarm.antitheifproject.utilities.BaseFragment
import com.securityalarm.antitheifproject.utilities.IS_GRID
import com.securityalarm.antitheifproject.utilities.autoServiceFunctionIntruder
import com.securityalarm.antitheifproject.utilities.clickWithThrottle
import com.securityalarm.antitheifproject.utilities.getNativeLayout
import com.securityalarm.antitheifproject.utilities.getNativeLayoutShimmer
import com.securityalarm.antitheifproject.utilities.id_banner_1
import com.securityalarm.antitheifproject.utilities.id_native_password_screen
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.loadImage
import com.securityalarm.antitheifproject.utilities.openMobileDataSettings
import com.securityalarm.antitheifproject.utilities.openWifiSettings
import com.securityalarm.antitheifproject.utilities.setImage
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
import com.securityalarm.antitheifproject.utilities.showInternetDialog
import com.securityalarm.antitheifproject.utilities.startLottieAnimation
import com.securityalarm.antitheifproject.utilities.val_ad_native_password_screen
import com.securityalarm.antitheifproject.utilities.val_banner_1

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
        arguments?.let {
            model = it.getParcelable(ANTI_TITLE) ?: return
        }
        adsManager=AdsManager.appAdsInit(activity?:return)
        _binding?.textView3?.text = model?.bottomText
        mDevicePolicyManager =
            context?.getSystemService(AppCompatActivity.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        mComponentName = ComponentName(context ?: return, Admin::class.java)
        dbHelper = DbHelper(context ?: return)
        _binding?.topLay?.title?.text = model?.maniTextTitle
        _binding?.topLay?.navMenu?.loadImage(context ?: return, R.drawable.back_btn)
        loadBanner()
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
        dbHelper?.getBooleanData(context ?: return, IS_GRID, true)?.let {
            loadLayoutDirection(it)
            isGridLayout = it
        }
    }

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
                linearlayout.titleText.text = model?.maniTextTitle ?: return
                activeAnimationView.clickWithThrottle {
                    if ((dbHelper?.getBooleanData(context?:return@clickWithThrottle,Intruder_Alarm,true)==true)!!) {
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
            val adView = LayoutInflater.from(context).inflate(
                getNativeLayout(model?.nativeLayout?:return,_binding?.gridLayout?.nativeExitAd!!,context?:return),
                null, false
            ) as NativeAdView
            adsManager?.nativeAds()?.loadNativeAd(
                activity ?: return,
                val_ad_native_password_screen,
                id_native_password_screen,
                object : NativeListener {
                    override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                        if (isAdded && isVisible && !isDetached) {
                            _binding?.gridLayout?.nativeExitAd?.visibility = View.GONE
                            _binding?.gridLayout?.shimmerLayout?.visibility = View.GONE
                            if (isAdded && isVisible && !isDetached) {
                                adsManager?.nativeAds()
                                    ?.nativeViewPolicy(currentNativeAd ?: return, adView)
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
            val adView = LayoutInflater.from(context).inflate(
                getNativeLayout(model?.nativeLayout?:return,_binding?.linearlayout?.nativeExitAd!!,context?:return),
                null, false
            ) as NativeAdView
            adsManager?.nativeAds()?.loadNativeAd(
                activity ?: return,
                val_ad_native_password_screen,
                id_native_password_screen,
                object : NativeListener {
                    override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                        if (isAdded && isVisible && !isDetached) {
                            _binding?.linearlayout?.nativeExitAd?.visibility = View.VISIBLE
                            _binding?.linearlayout?.shimmerLayout?.visibility = View.GONE

                            adsManager?.nativeAds()?.nativeViewPolicy(currentNativeAd ?: return, adView)
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
            bannerId = id_banner_1,
            bannerListener = {
                _binding?.shimmerLayout!!.visibility=View.GONE
            }
        )
    }


}