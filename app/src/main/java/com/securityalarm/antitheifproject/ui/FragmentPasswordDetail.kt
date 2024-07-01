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
import com.bmik.android.sdk.IkmSdkController
import com.bmik.android.sdk.SDKBaseController
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.widgets.IkmWidgetAdLayout
import com.securityalarm.antitheifproject.Admin
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
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.loadImage
import com.securityalarm.antitheifproject.utilities.openMobileDataSettings
import com.securityalarm.antitheifproject.utilities.openWifiSettings
import com.securityalarm.antitheifproject.utilities.setImage
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
import com.securityalarm.antitheifproject.utilities.showInternetDialog
import com.securityalarm.antitheifproject.utilities.startLottieAnimation

class FragmentPasswordDetail :
    BaseFragment<FragmentDetailModuleBinding>(FragmentDetailModuleBinding::inflate) {

    private var isGridLayout: Boolean? = null
    private var model: MainMenuModel? = null
    private var dbHelper: DbHelper? = null
    private var mComponentName: ComponentName? = null
    private var mDevicePolicyManager: DevicePolicyManager? = null
    private var isInternetDialog: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            model = it.getParcelable(ANTI_TITLE) ?: return
        }
        SDKBaseController.getInstance().preloadNativeAd(
            activity ?: return, model?.nativeSoundId?:return,
            model?.nativeSoundId?:return, object : CustomSDKAdsListenerAdapter() {
                override fun onAdsLoaded() {
                    super.onAdsLoaded()
                    Log.d("check_ads", "onAdsLoaded: Load Ad")
                }
                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                }
            }
        )
        _binding?.textView3?.text = model?.bottomText
        mDevicePolicyManager =
            context?.getSystemService(AppCompatActivity.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        mComponentName = ComponentName(context ?: return, Admin::class.java)
        dbHelper = DbHelper(context ?: return)
        _binding?.topLay?.title?.text = model?.maniTextTitle
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
        dbHelper?.getBooleanData(context ?: return, IS_GRID, true)?.let {
            loadLayoutDirection(it)
            isGridLayout = it
        }
        loadBanner()
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
            } else {
                IkmSdkController.setEnableShowResumeAds(true)
            }
        }
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
            val adLayout = LayoutInflater.from(context).inflate(
                getNativeLayout(model?.nativeLayout?:return,_binding?.gridLayout?.nativeExitAd!!,context?:return),
                null, false
            ) as? IkmWidgetAdLayout
            adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
            adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
            adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
            adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
            adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
            _binding?.gridLayout?.nativeExitAd?.loadAd(
                activity ?: return,  getNativeLayoutShimmer(model?.nativeLayout?:return),
                adLayout!!, "password_native",
                "password_native", object : CustomSDKAdsListenerAdapter() {

                    override fun onAdsLoadFail() {
                        super.onAdsLoadFail()
                        _binding?.gridLayout?.nativeExitAd?.visibility = View.GONE
                    }
                }
            )

        }

        private fun loadNativeList() {
            val adLayout = LayoutInflater.from(context).inflate(
                getNativeLayout(model?.nativeLayout?:return,_binding?.linearlayout?.nativeExitAd!!,context?:return),
                null, false
            ) as? IkmWidgetAdLayout
            adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
            adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
            adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
            adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
            adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
            _binding?.linearlayout?.nativeExitAd?.loadAd(
                activity ?: return,  getNativeLayoutShimmer(model?.nativeLayout?:return),
                adLayout!!, "password_native",
                "password_native", object : CustomSDKAdsListenerAdapter() {

                    override fun onAdsLoadFail() {
                        super.onAdsLoadFail()
                        _binding?.linearlayout?.nativeExitAd?.visibility = View.GONE
                    }
                }
            )
        }

    private fun loadBanner() {
        binding?.bannerAds?.loadAd(
            activity, "password_banner",
            "password_banner", object : CustomSDKAdsListenerAdapter() {
                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.bannerAds?.visibility = View.GONE
                }
            }
        )
    }


}