package com.do_not_douch.antitheifproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.do_not_douch.antitheifproject.adapter.SoundSelectGridAdapter
import com.do_not_douch.antitheifproject.adapter.SoundSelectLinearAdapter
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.FragmentSoundSelectionBinding
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.interfaces.NativeListener
import com.do_not_douch.antitheifproject.helper_class.DbHelper
import com.do_not_douch.antitheifproject.model.MainMenuModel
import com.do_not_douch.antitheifproject.utilities.ANTI_TITLE
import com.do_not_douch.antitheifproject.utilities.BaseFragment
import com.do_not_douch.antitheifproject.utilities.IS_GRID
import com.do_not_douch.antitheifproject.utilities.clickWithThrottle
import com.do_not_douch.antitheifproject.utilities.getNativeLayout
import com.do_not_douch.antitheifproject.utilities.id_native_sound_screen
import com.do_not_douch.antitheifproject.utilities.loadImage
import com.do_not_douch.antitheifproject.utilities.setImage
import com.do_not_douch.antitheifproject.utilities.setupBackPressedCallback
import com.do_not_douch.antitheifproject.utilities.soundData
import com.do_not_douch.antitheifproject.utilities.val_ad_native_sound_screen


class FragmentSoundSelection :
    BaseFragment<FragmentSoundSelectionBinding>(FragmentSoundSelectionBinding::inflate) {

    private var sharedPrefUtils: DbHelper? = null
    private var isGridLayout: Boolean? = null
    private var adapterGrid: SoundSelectGridAdapter? = null
    private var adapterLinear: SoundSelectLinearAdapter? = null
    private var position: MainMenuModel? = null
    private var isInternetDialog: Boolean = false
    private var adsManager: AdsManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getParcelable(ANTI_TITLE) ?: return@let
        }
        sharedPrefUtils = DbHelper(context ?: return)
        adsManager = AdsManager.appAdsInit(activity ?: return)
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
            topLay.settingBtn.clickWithThrottle {
                findNavController().navigate(R.id.FragmentBuyScreen)
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

        val adView = LayoutInflater.from(context).inflate(
            getNativeLayout(position?.nativeSoundLayout?:return,_binding?.nativeExitAd!!,context?:return),
            null, false
        ) as NativeAdView
        adsManager?.nativeAds()?.loadNativeAd(
            activity ?: return,
            val_ad_native_sound_screen,
            id_native_sound_screen,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    if(!isAdded && !isVisible && isDetached){
                        return
                    }
                    _binding?.nativeExitAd?.visibility = View.VISIBLE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    if(isAdded && isVisible && !isDetached) {
                        adsManager?.nativeAds()?.nativeViewPolicy(currentNativeAd ?: return, adView)
                        _binding?.nativeExitAd?.removeAllViews()
                        _binding?.nativeExitAd?.addView(adView)
                    }
                    super.nativeAdLoaded(currentNativeAd)
                }

                override fun nativeAdFailed(loadAdError: LoadAdError) {
                    _binding?.nativeExitAd?.visibility = View.GONE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdFailed(loadAdError)
                }

                override fun nativeAdValidate(string: String) {
                    _binding?.nativeExitAd?.visibility = View.GONE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdValidate(string)
                }
            })

    }

    override fun onPause() {
        super.onPause()
        isInternetDialog=true
    }
    override fun onResume() {
        super.onResume()
    }

}