package com.securityalarm.antitheifproject.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.securityalarm.antitheifproject.adapter.SoundSelectGridAdapter
import com.securityalarm.antitheifproject.adapter.SoundSelectLinearAdapter
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentSoundSelectionBinding
import com.bmik.android.sdk.IkmSdkController
import com.bmik.android.sdk.SDKBaseController
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.widgets.IkmWidgetAdLayout
import com.securityalarm.antitheifproject.helper_class.DbHelper
import com.securityalarm.antitheifproject.model.MainMenuModel
import com.securityalarm.antitheifproject.utilities.ANTI_TITLE
import com.securityalarm.antitheifproject.utilities.BaseFragment
import com.securityalarm.antitheifproject.utilities.IS_GRID
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
import com.securityalarm.antitheifproject.utilities.soundData


class FragmentSoundSelection :
    BaseFragment<FragmentSoundSelectionBinding>(FragmentSoundSelectionBinding::inflate) {

    private var sharedPrefUtils: DbHelper? = null
    private var isGridLayout: Boolean? = null
    private var adapterGrid: SoundSelectGridAdapter? = null
    private var adapterLinear: SoundSelectLinearAdapter? = null
    private var position: MainMenuModel? = null
    private var isInternetDialog: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getParcelable(ANTI_TITLE) ?: return@let
        }
        SDKBaseController.getInstance().preloadNativeAd(
            activity ?: return, position?.nativeId?:return,
            position?.nativeId?:return, object : CustomSDKAdsListenerAdapter() {
                override fun onAdsLoaded() {
                    super.onAdsLoaded()
                    Log.d("check_ads", "onAdsLoaded: Load Ad")
                }
                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    Log.d("check_ads", "onAdsLoadFail: Load No")
                }
            }
        )
        sharedPrefUtils = DbHelper(context ?: return)
        sharedPrefUtils?.getBooleanData(context ?: return, IS_GRID, true)?.let {
            loadLayoutDirection(it)
            isGridLayout = it
        }
        _binding?.topLay?.title?.text = getString(R.string.select_sound)
        _binding?.topLay?.navMenu?.loadImage(context ?: return, R.drawable.back_btn)
        _binding?.run {
            topLay.navMenu.clickWithThrottle {
                findNavController().popBackStack()
            }
            topLay.setLayoutBtn.clickWithThrottle {
                loadLayoutDirection(!(isGridLayout ?: return@clickWithThrottle))
            }
        }
        setupBackPressedCallback {
            findNavController().popBackStack()
        }
        loadNative()
    }

    private fun loadLayoutDirection(isGrid: Boolean) {

        if (isGrid) {
            isGridLayout = true
            sharedPrefUtils?.saveData(context ?: return, IS_GRID, true)
            _binding?.topLay?.setLayoutBtn?.setImage(R.drawable.icon_grid)
            adapterGrid = SoundSelectGridAdapter(clickItem = {
                sharedPrefUtils?.setTone(position?.maniTextTitle, it)
            }, soundData = soundData())
            val managerLayout = GridLayoutManager(context, 3)
            _binding?.recycler?.layoutManager = managerLayout
            sharedPrefUtils?.getTone(context ?: return, position?.maniTextTitle)
                ?.let { adapterGrid?.selectSound(it) }
            _binding?.recycler?.adapter = adapterGrid

        } else {
            isGridLayout = false
            sharedPrefUtils?.saveData(context ?: return, IS_GRID, false)
            _binding?.topLay?.setLayoutBtn?.setImage( R.drawable.icon_list)
            adapterLinear = SoundSelectLinearAdapter(clickItem = {
                sharedPrefUtils?.setTone(position?.maniTextTitle, it)
            }, soundData = soundData())

            val managerLayout = LinearLayoutManager(context)
            managerLayout.orientation = LinearLayoutManager.VERTICAL
            _binding?.recycler?.layoutManager = managerLayout
            sharedPrefUtils?.getTone(context ?: return, position?.maniTextTitle)
                ?.let { adapterLinear?.selectLanguage(it) }
            _binding?.recycler?.adapter = adapterLinear
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    private fun loadNative() {

        val adLayout = LayoutInflater.from(context).inflate(
            getNativeLayout(position?.nativeSoundLayout?:return,_binding?.nativeExitAd!!,context?:return),
            null, false
        ) as NativeAdView
        adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
        adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
        adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
        adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
        adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
        _binding?.nativeExitAd?.loadAd(
            activity ?: return,  getNativeLayoutShimmer(position?.nativeSoundLayout?:return),
            adLayout!!, position?.nativeSoundId?:return,
            position?.nativeSoundId?:return, object : CustomSDKAdsListenerAdapter() {

                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.nativeExitAd?.visibility = View.GONE
                }
            }
        )

    }

    override fun onPause() {
        super.onPause()
        isInternetDialog=true
        if (!isInternetAvailable(context ?: return)) {
            IkmSdkController.setEnableShowResumeAds(false)
        }
    }
    override fun onResume() {
        super.onResume()
        if (isInternetDialog) {
            if (!isInternetAvailable(context ?: return)) {
                IkmSdkController.setEnableShowResumeAds(false)
/*                showInternetDialog(
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
                )*/
                return
            }else{
                IkmSdkController.setEnableShowResumeAds(true)
            }
        }
    }

}