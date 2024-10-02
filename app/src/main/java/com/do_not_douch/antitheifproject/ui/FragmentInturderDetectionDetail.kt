package com.do_not_douch.antitheifproject.ui

import android.app.Activity.RESULT_OK
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.navigation.fragment.findNavController
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.FragmentInturderDetectionDetailBinding
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.do_not_douch.antitheifproject.Admin
import com.do_not_douch.antitheifproject.ads_manager.AdsBanners
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.interfaces.NativeListener
import com.do_not_douch.antitheifproject.helper_class.Constants.Intruder_Alarm
import com.do_not_douch.antitheifproject.helper_class.Constants.Intruder_Selfie
import com.do_not_douch.antitheifproject.helper_class.Constants.isServiceRunning
import com.do_not_douch.antitheifproject.helper_class.DbHelper
import com.do_not_douch.antitheifproject.utilities.BaseFragment
import com.do_not_douch.antitheifproject.utilities.CAMERA_PERMISSION
import com.do_not_douch.antitheifproject.utilities.IS_GRID
import com.do_not_douch.antitheifproject.utilities.autoServiceFunctionIntruder
import com.do_not_douch.antitheifproject.utilities.clickWithThrottle
import com.do_not_douch.antitheifproject.utilities.getNativeLayout
import com.do_not_douch.antitheifproject.utilities.id_banner_1
import com.do_not_douch.antitheifproject.utilities.id_native_intruder_detection_screen
import com.do_not_douch.antitheifproject.utilities.intruderimage_bottom
import com.do_not_douch.antitheifproject.utilities.loadImage
import com.do_not_douch.antitheifproject.utilities.requestCameraPermission
import com.do_not_douch.antitheifproject.utilities.setImage
import com.do_not_douch.antitheifproject.utilities.setupBackPressedCallback
import com.do_not_douch.antitheifproject.utilities.startLottieAnimation
import com.do_not_douch.antitheifproject.utilities.val_ad_native_intruder_detection_screen
import com.do_not_douch.antitheifproject.utilities.val_banner_1

class FragmentInturderDetectionDetail :
    BaseFragment<FragmentInturderDetectionDetailBinding>(FragmentInturderDetectionDetailBinding::inflate) {

    private var dbHelper: DbHelper? = null
    private var mComponentName: ComponentName? = null
    private var mDevicePolicyManager: DevicePolicyManager? = null
    private var isGridLayout: Boolean? = null
    private var adsManager: AdsManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DbHelper(context ?: return)
        adsManager= AdsManager.appAdsInit(activity?:return)
        _binding?.textView3?.text = getString(R.string.title_intruder)
        mDevicePolicyManager =
            context?.getSystemService(AppCompatActivity.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        mComponentName = ComponentName(context ?: return, Admin::class.java)
        _binding?.topLay?.navMenu?.loadImage(context ?: return, R.drawable.back_btn)
        _binding?.run {
            _binding?.topLay?.navMenu?.clickWithThrottle {
                findNavController().navigateUp()
            }
            topLay.setLayoutBtn.clickWithThrottle {
                loadLayoutDirection(!(isGridLayout ?: return@clickWithThrottle))
            }
            topLay.settingBtn.clickWithThrottle {
                findNavController().navigate(R.id.FragmentBuyScreen)
            }
        }
        loadBanner()
        dbHelper?.getBooleanData(context ?: return, IS_GRID, true)?.let {
            isGridLayout = it
            loadLayoutDirection(it)
        }
        setupBackPressedCallback {
            findNavController().navigateUp()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    private fun loadLayoutDirection(isGrid: Boolean) {
        checkSwitch()
        if (isGrid) {
            isGridLayout = true
            dbHelper?.saveData(context ?: return, IS_GRID, true)
            _binding?.run {
                topLay.setLayoutBtn.setImage(R.drawable.icon_grid)
                gridLayout.topGrid.visibility = View.VISIBLE
                linearlayout.topLinear.visibility = View.GONE
                activeAnimationView.clickWithThrottle {
                    if ((dbHelper?.getBooleanData(
                            context ?: return@clickWithThrottle,
                            Intruder_Selfie,
                            true
                        ) == true)
                    ) {
                        startLottieAnimation(
                            activeAnimationView, activeAnimationViewText, false
                        )
                        dbHelper?.setBroadCast(Intruder_Selfie, false)
                        gridLayout.inturderAlertSwitch.isChecked = false
                    } else if (ContextCompat.checkSelfPermission(
                            context ?: return@clickWithThrottle, CAMERA_PERMISSION
                        ) == 0
                    ) {
                        val devicePolicyManager = mDevicePolicyManager
                        if (devicePolicyManager == null || devicePolicyManager.isAdminActive(
                                (mComponentName) ?: return@clickWithThrottle
                            )
                        ) {
                            startLottieAnimation(
                                activeAnimationView, activeAnimationViewText, true
                            )
                            dbHelper?.setBroadCast(Intruder_Selfie, true)
                            gridLayout.inturderAlertSwitch.isChecked = true
                            if (!isServiceRunning()) {
                                autoServiceFunctionIntruder(true, dbHelper)
                            }
                            return@clickWithThrottle
                        } else {
                            val intent = Intent("android.app.action.ADD_DEVICE_ADMIN")
                            intent.putExtra("android.app.extra.DEVICE_ADMIN", mComponentName)
                            intent.putExtra(
                                "android.app.extra.ADD_EXPLANATION", "Administrator description"
                            )
                            cameraActivityResultLauncher.launch(intent)
                        }
                    } else {
                        isHideAds(true)
                        requestCameraPermission(gridLayout.inturderAlertSwitch)
                    }

                }
                gridLayout.inturderAlertSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (!bool) {
                            startLottieAnimation(
                                activeAnimationView, activeAnimationViewText, false
                            )
                            dbHelper?.setBroadCast(Intruder_Selfie, false)
                        } else if (ContextCompat.checkSelfPermission(
                                context ?: return@setOnCheckedChangeListener, CAMERA_PERMISSION
                            ) == 0
                        ) {
                            val devicePolicyManager = mDevicePolicyManager
                            if (devicePolicyManager == null || devicePolicyManager.isAdminActive(
                                    (mComponentName) ?: return@setOnCheckedChangeListener
                                )
                            ) {
                                startLottieAnimation(
                                    activeAnimationView, activeAnimationViewText, true
                                )
                                dbHelper?.setBroadCast(Intruder_Selfie, true)
                                if (!isServiceRunning()) {
                                    autoServiceFunctionIntruder(true, dbHelper)
                                }
                                return@setOnCheckedChangeListener
                            } else {
                                val intent = Intent("android.app.action.ADD_DEVICE_ADMIN")
                                intent.putExtra("android.app.extra.DEVICE_ADMIN", mComponentName)
                                intent.putExtra(
                                    "android.app.extra.ADD_EXPLANATION", "Administrator description"
                                )
                                cameraActivityResultLauncher.launch(intent)
                            }
                        } else {
                            isHideAds(true)
                            requestCameraPermission(gridLayout.inturderAlertSwitch)
                        }
                    }
                }
                gridLayout.stopAlertSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        dbHelper?.setAlarmSetting(Intruder_Alarm, bool)
                        if (bool) {
                            if (!isServiceRunning()) {
                                autoServiceFunctionIntruder(true, dbHelper)
                            }
                        }
                    }
                }
                gridLayout.intruderImagesView.clickWithThrottle {
                    findNavController().navigate(R.id.FragmentShowIntruder)

                }
                gridLayout.run {
                    at1.clickWithThrottle {
                        at1.setBackgroundColor(Color.parseColor("#5F82E2"))
                        at2.setBackgroundColor(
                            ContextCompat.getColor(
                                context ?: return@clickWithThrottle, R.color.menuBgColor
                            )
                        )
                        at3.setBackgroundColor(
                            ContextCompat.getColor(
                                context ?: return@clickWithThrottle, R.color.menuBgColor
                            )
                        )
                        dbHelper?.setAttemptNo("1")
                    }

                    at2.clickWithThrottle {
                        dbHelper?.setAttemptNo(ExifInterface.GPS_MEASUREMENT_2D)
                        at2.setBackgroundColor(Color.parseColor("#5F82E2"))
                        at1.setBackgroundColor(
                            ContextCompat.getColor(
                                context ?: return@clickWithThrottle, R.color.menuBgColor
                            )
                        )
                        at3.setBackgroundColor(
                            ContextCompat.getColor(
                                context ?: return@clickWithThrottle, R.color.menuBgColor
                            )
                        )
                    }

                    at3.clickWithThrottle {
                        dbHelper?.setAttemptNo(ExifInterface.GPS_MEASUREMENT_3D)
                        at3.setBackgroundColor(Color.parseColor("#5F82E2"))
                        at2.setBackgroundColor(
                            ContextCompat.getColor(
                                context ?: return@clickWithThrottle, R.color.menuBgColor
                            )
                        )
                        at1.setBackgroundColor(
                            ContextCompat.getColor(
                                context ?: return@clickWithThrottle, R.color.menuBgColor
                            )
                        )
                    }
                }
                loadNativeGrid()
            }
        } else {
            isGridLayout = false
            dbHelper?.saveData(context ?: return, IS_GRID, false)
            _binding?.run {
                topLay.setLayoutBtn.setImage(R.drawable.icon_list)
                linearlayout.topLinear.visibility = View.VISIBLE
                gridLayout.topGrid.visibility = View.GONE
                linearlayout.inturderAlertSwitch.isChecked =
                    dbHelper?.chkBroadCast(Intruder_Selfie) ?: return
                activeAnimationView.clickWithThrottle {

                    if (dbHelper?.getBooleanData(
                            context ?: return@clickWithThrottle,
                            Intruder_Selfie,
                            true
                        )!!
                    ) {
                        startLottieAnimation(
                            activeAnimationView, activeAnimationViewText, false
                        )
                        dbHelper?.setBroadCast(Intruder_Selfie, false)
                        gridLayout.inturderAlertSwitch.isChecked = false
                    } else if (ContextCompat.checkSelfPermission(
                            context ?: return@clickWithThrottle, CAMERA_PERMISSION
                        ) == 0
                    ) {
                        val devicePolicyManager = mDevicePolicyManager
                        if (devicePolicyManager == null || devicePolicyManager.isAdminActive(
                                (mComponentName) ?: return@clickWithThrottle
                            )
                        ) {
                            startLottieAnimation(
                                activeAnimationView, activeAnimationViewText, true
                            )
                            dbHelper?.setBroadCast(Intruder_Selfie, true)
                            gridLayout.inturderAlertSwitch.isChecked = true
                            if (!isServiceRunning()) {
                                autoServiceFunctionIntruder(true, dbHelper)
                            }
                            return@clickWithThrottle
                        } else {
                            val intent = Intent("android.app.action.ADD_DEVICE_ADMIN")
                            intent.putExtra("android.app.extra.DEVICE_ADMIN", mComponentName)
                            intent.putExtra(
                                "android.app.extra.ADD_EXPLANATION", "Administrator description"
                            )
                            cameraActivityResultLauncher.launch(intent)
                        }
                    } else {
                        isHideAds(true)
                        requestCameraPermission(
                            linearlayout.inturderAlertSwitch
                        )
                    }
                }
                linearlayout.inturderAlertSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (!bool) {
                            startLottieAnimation(
                                activeAnimationView, activeAnimationViewText, false
                            )
                            dbHelper?.setBroadCast(Intruder_Selfie, false)
                        } else if (ContextCompat.checkSelfPermission(
                                context ?: return@setOnCheckedChangeListener, CAMERA_PERMISSION
                            ) == 0
                        ) {
                            val devicePolicyManager = mDevicePolicyManager
                            if (devicePolicyManager == null || devicePolicyManager.isAdminActive(
                                    (mComponentName) ?: return@setOnCheckedChangeListener
                                )
                            ) {
                                startLottieAnimation(
                                    activeAnimationView, activeAnimationViewText, true
                                )
                                dbHelper?.setBroadCast(Intruder_Selfie, true)
                                if (!isServiceRunning()) {
                                    autoServiceFunctionIntruder(true, dbHelper)
                                }
                                return@setOnCheckedChangeListener
                            } else {
                                val intent = Intent("android.app.action.ADD_DEVICE_ADMIN")
                                intent.putExtra("android.app.extra.DEVICE_ADMIN", mComponentName)
                                intent.putExtra(
                                    "android.app.extra.ADD_EXPLANATION", "Administrator description"
                                )
                                cameraActivityResultLauncher.launch(intent)
                            }
                        } else {
                            isHideAds(true)
                            requestCameraPermission(
                                linearlayout.inturderAlertSwitch
                            )
                        }
                    }
                }
                linearlayout.stopAlertSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        dbHelper?.setAlarmSetting(Intruder_Alarm, bool)
                        if (bool) {
                            if (!isServiceRunning()) {
                                autoServiceFunctionIntruder(true, dbHelper)
                            }
                        }
                    }
                }
                linearlayout.intruderImagesView.clickWithThrottle {
                    findNavController().navigate(R.id.FragmentShowIntruder)
                }
                linearlayout.run {
                    at1.clickWithThrottle {
                        at1.setBackgroundColor(Color.parseColor("#5F82E2"))
                        at2.setBackgroundColor(
                            ContextCompat.getColor(
                                context ?: return@clickWithThrottle, R.color.menuBgColor
                            )
                        )
                        at3.setBackgroundColor(
                            ContextCompat.getColor(
                                context ?: return@clickWithThrottle, R.color.menuBgColor
                            )
                        )
                        dbHelper?.setAttemptNo("1")

                    }
                    at2.clickWithThrottle {
                        dbHelper?.setAttemptNo(ExifInterface.GPS_MEASUREMENT_2D)
                        at2.setBackgroundColor(Color.parseColor("#5F82E2"))
                        at1.setBackgroundColor(
                            ContextCompat.getColor(
                                context ?: return@clickWithThrottle, R.color.menuBgColor
                            )
                        )
                        at3.setBackgroundColor(
                            ContextCompat.getColor(
                                context ?: return@clickWithThrottle, R.color.menuBgColor
                            )
                        )
                    }
                    at3.clickWithThrottle {
                        dbHelper?.setAttemptNo(ExifInterface.GPS_MEASUREMENT_3D)
                        at3.setBackgroundColor(Color.parseColor("#5F82E2"))
                        at2.setBackgroundColor(
                            ContextCompat.getColor(
                                context ?: return@clickWithThrottle, R.color.menuBgColor
                            )
                        )
                        at1.setBackgroundColor(
                            ContextCompat.getColor(
                                context ?: return@clickWithThrottle, R.color.menuBgColor
                            )
                        )
                    }
                }
                loadNativeList()
            }
        }
    }

    private var cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result in this block
        if (result.resultCode == RESULT_OK) {
            // Extract data from the result Intent if needed
            if (ContextCompat.checkSelfPermission(
                    context ?: return@registerForActivityResult, CAMERA_PERMISSION
                ) == 0 && (mDevicePolicyManager == null || mDevicePolicyManager!!.isAdminActive(
                    (mComponentName) ?: return@registerForActivityResult
                ))
            ) {
                dbHelper?.setBroadCast(Intruder_Selfie, true)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isHideAds(false)
        checkSwitch()
        checkPinAttempt()
    }

    private fun checkPinAttempt() {
        when (dbHelper?.attemptNo) {
            1 -> {
                _binding?.gridLayout?.at1?.setBackgroundColor(Color.parseColor("#5F82E2"))
                _binding?.linearlayout?.at1?.setBackgroundColor(Color.parseColor("#5F82E2"))
            }

            2 -> {
                _binding?.gridLayout?.at2?.setBackgroundColor(Color.parseColor("#5F82E2"))
                _binding?.linearlayout?.at2?.setBackgroundColor(Color.parseColor("#5F82E2"))
            }

            3 -> {
                _binding?.gridLayout?.at3?.setBackgroundColor(Color.parseColor("#5F82E2"))
                _binding?.linearlayout?.at3?.setBackgroundColor(Color.parseColor("#5F82E2"))
            }
        }
    }

    private fun checkSwitch() {

        _binding?.run {
            if (dbHelper?.chkBroadCast(Intruder_Selfie) ?: return) {
                startLottieAnimation(activeAnimationView, activeAnimationViewText, true)
            } else {
                startLottieAnimation(activeAnimationView, activeAnimationViewText, false)
            }

            gridLayout.inturderAlertSwitch.isChecked =
                dbHelper?.chkBroadCast(Intruder_Selfie) ?: return
            linearlayout.inturderAlertSwitch.isChecked =
                dbHelper?.chkBroadCast(Intruder_Selfie) ?: return
            gridLayout.stopAlertSwitch.isChecked = dbHelper?.chkBroadCast(Intruder_Alarm) ?: return
            linearlayout.stopAlertSwitch.isChecked =
                dbHelper?.chkBroadCast(Intruder_Alarm) ?: return

        }
    }

    private fun loadNativeGrid() {
        val adView = LayoutInflater.from(context).inflate(
            getNativeLayout(
                intruderimage_bottom,
                _binding?.gridLayout?.nativeExitAd!!,
                context ?: return
            ), null, false
        ) as NativeAdView

        adsManager?.nativeAds()?.loadNativeAd(
            activity ?: return,
            val_ad_native_intruder_detection_screen,
            id_native_intruder_detection_screen,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    _binding?.gridLayout?.nativeExitAd?.visibility = View.VISIBLE
                    _binding?.gridLayout?.shimmerLayout?.visibility = View.GONE
                    if (isAdded && isVisible && !isDetached) {
                        adsManager?.nativeAds()
                            ?.nativeViewPolicy(context?:return,currentNativeAd ?: return, adView)
                        _binding?.gridLayout?.nativeExitAd?.removeAllViews()
                        _binding?.gridLayout?.nativeExitAd?.addView(adView)
                    } else {
                        _binding?.gridLayout?.nativeExitAd?.visibility = View.GONE
                        _binding?.gridLayout?.shimmerLayout?.visibility = View.GONE
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
                intruderimage_bottom,
                _binding?.linearlayout?.nativeExitAd!!,
                context ?: return
            ), null, false
        ) as NativeAdView
        adsManager?.nativeAds()?.loadNativeAd(
            activity ?: return,
            val_ad_native_intruder_detection_screen,
            id_native_intruder_detection_screen,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    _binding?.linearlayout?.nativeExitAd?.visibility = View.VISIBLE
                    _binding?.linearlayout?.shimmerLayout?.visibility = View.GONE
                    if (isAdded && isVisible && !isDetached) {
                        adsManager?.nativeAds()
                            ?.nativeViewPolicy(context?:return,currentNativeAd ?: return, adView)
                        _binding?.linearlayout?.nativeExitAd?.removeAllViews()
                        _binding?.linearlayout?.nativeExitAd?.addView(adView)
                    } else {
                        _binding?.linearlayout?.nativeExitAd?.visibility = View.GONE
                        _binding?.linearlayout?.shimmerLayout?.visibility = View.GONE
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
            activity = activity?:return,
            view = _binding?.bannerAds!!,
            viewS = _binding?.shimmerLayout!!,
            addConfig = val_banner_1,
            bannerId = id_banner_1
        ) {
            _binding?.shimmerLayout!!.visibility = View.GONE
        }
    }

    private fun isHideAds(isBoolean: Boolean) {
        if (isBoolean) {
            _binding?.bannerAds?.visibility = View.GONE
            if (isGridLayout == true) {
                _binding?.gridLayout?.nativeExitAd?.visibility = View.GONE
            } else {
                _binding?.linearlayout?.nativeExitAd?.visibility = View.GONE
            }
        } else {
            _binding?.bannerAds?.visibility = View.VISIBLE
            if (isGridLayout == true) {
                _binding?.gridLayout?.nativeExitAd?.visibility = View.VISIBLE
            } else {
                _binding?.linearlayout?.nativeExitAd?.visibility = View.VISIBLE
            }
        }
    }


}