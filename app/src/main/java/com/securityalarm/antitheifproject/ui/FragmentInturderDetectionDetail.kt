package com.securityalarm.antitheifproject.ui

import android.app.Activity.RESULT_OK
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.navigation.fragment.findNavController
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentInturderDetectionDetailBinding
import com.bmik.android.sdk.SDKBaseController
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.widgets.IkmWidgetAdLayout
import com.securityalarm.antitheifproject.Admin
import com.securityalarm.antitheifproject.helper_class.Constants.Intruder_Alarm
import com.securityalarm.antitheifproject.helper_class.Constants.Intruder_Selfie
import com.securityalarm.antitheifproject.helper_class.Constants.isServiceRunning
import com.securityalarm.antitheifproject.helper_class.DbHelper
import com.securityalarm.antitheifproject.utilities.BaseFragment
import com.securityalarm.antitheifproject.utilities.CAMERA_PERMISSION
import com.securityalarm.antitheifproject.utilities.IS_GRID
import com.securityalarm.antitheifproject.utilities.autoServiceFunctionIntruder
import com.securityalarm.antitheifproject.utilities.clickWithThrottle
import com.securityalarm.antitheifproject.utilities.getNativeLayout
import com.securityalarm.antitheifproject.utilities.getNativeLayoutShimmer
import com.securityalarm.antitheifproject.utilities.intruderimage_bottom
import com.securityalarm.antitheifproject.utilities.loadImage
import com.securityalarm.antitheifproject.utilities.requestCameraPermission
import com.securityalarm.antitheifproject.utilities.setImage
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
import com.securityalarm.antitheifproject.utilities.startLottieAnimation

class FragmentInturderDetectionDetail :
    BaseFragment<FragmentInturderDetectionDetailBinding>(FragmentInturderDetectionDetailBinding::inflate) {

    private var dbHelper: DbHelper? = null
    private var mComponentName: ComponentName? = null
    private var mDevicePolicyManager: DevicePolicyManager? = null
    private var isGridLayout: Boolean? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DbHelper(context ?: return)
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
        }
        loadBanner()
        SDKBaseController.getInstance().preloadNativeAd(
            activity ?: return, "intruder_native",
            "intruder_native", object : CustomSDKAdsListenerAdapter() {
                override fun onAdsLoaded() {
                    super.onAdsLoaded()
                    Log.d("check_ads", "onAdsLoaded: Load Ad")
                }
                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                }
            }
        )
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
                gridLayout.inturderAlertSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (!bool) {
                            startLottieAnimation(
                                activeAnimationView,
                                activeAnimationViewText,
                                false
                            )
                            dbHelper?.setBroadCast(Intruder_Selfie, false)
                        } else if (ContextCompat.checkSelfPermission(
                                context ?: return@setOnCheckedChangeListener,
                                CAMERA_PERMISSION
                            ) == 0
                        ) {
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
                                dbHelper?.setBroadCast(Intruder_Selfie, true)
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
                                cameraActivityResultLauncher.launch(intent)
                            }
                        } else {
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
                linearlayout.inturderAlertSwitch.setOnCheckedChangeListener { compoundButton, bool ->
                    if (compoundButton.isPressed) {
                        if (!bool) {
                            startLottieAnimation(
                                activeAnimationView,
                                activeAnimationViewText,
                                false
                            )
                            dbHelper?.setBroadCast(Intruder_Selfie, false)
                        } else if (ContextCompat.checkSelfPermission(
                                context ?: return@setOnCheckedChangeListener,
                                CAMERA_PERMISSION
                            ) == 0
                        ) {
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
                                dbHelper?.setBroadCast(Intruder_Selfie, true)
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
                                cameraActivityResultLauncher.launch(intent)
                            }
                        } else {
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
                    context ?: return@registerForActivityResult,
                    CAMERA_PERMISSION
                ) == 0
                && (mDevicePolicyManager == null || mDevicePolicyManager!!.isAdminActive(
                    (mComponentName) ?: return@registerForActivityResult
                )
                        )
            ) {
                dbHelper?.setBroadCast(Intruder_Selfie, true)
            }
        }
    }

    override fun onResume() {
        super.onResume()
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
            gridLayout.stopAlertSwitch.isChecked =
                dbHelper?.chkBroadCast(Intruder_Alarm) ?: return
            linearlayout.stopAlertSwitch.isChecked =
                dbHelper?.chkBroadCast(Intruder_Alarm) ?: return

        }
    }

    private fun loadNativeGrid() {
        val adLayout = LayoutInflater.from(context).inflate(
            getNativeLayout(intruderimage_bottom,_binding?.gridLayout?.nativeExitAd!!,context?:return),
            null, false
        ) as? IkmWidgetAdLayout
        adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
        adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
        adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
        adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
        adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
        _binding?.gridLayout?.nativeExitAd?.loadAd(
            activity ?: return,  getNativeLayoutShimmer(intruderimage_bottom),
            adLayout!!, "intruder_native",
            "intruder_native", object : CustomSDKAdsListenerAdapter() {

                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.gridLayout?.nativeExitAd?.visibility = View.GONE
                }
            }
        )

    }

    private fun loadNativeList() {
        val adLayout = LayoutInflater.from(context).inflate(
            getNativeLayout(intruderimage_bottom,_binding?.linearlayout?.nativeExitAd!!,context?:return),
            null, false
        ) as? IkmWidgetAdLayout
        adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
        adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
        adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
        adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
        adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
        _binding?.linearlayout?.nativeExitAd?.loadAd(
            activity ?: return,  getNativeLayoutShimmer(intruderimage_bottom),
            adLayout!!, "intruder_native",
            "intruder_native", object : CustomSDKAdsListenerAdapter() {

                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.linearlayout?.nativeExitAd?.visibility = View.GONE
                }
            }
        )
    }

    private fun loadBanner() {
        binding?.bannerAds?.loadAd(
            activity, "antithef_banner",
            "antithef_banner", object : CustomSDKAdsListenerAdapter() {
                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.bannerAds?.visibility = View.GONE
                }
            }
        )
    }

}