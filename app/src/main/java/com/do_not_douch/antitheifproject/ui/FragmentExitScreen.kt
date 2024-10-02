package com.do_not_douch.antitheifproject.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.FragmentExitScreenBinding
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.interfaces.NativeListener
import com.do_not_douch.antitheifproject.utilities.BaseFragment
import com.do_not_douch.antitheifproject.utilities.getNativeLayout
import com.do_not_douch.antitheifproject.utilities.id_exit_screen_native
import com.do_not_douch.antitheifproject.utilities.setupBackPressedCallback
import com.do_not_douch.antitheifproject.utilities.thankyou_bottom
import com.do_not_douch.antitheifproject.utilities.val_exit_dialog_native
import com.do_not_douch.antitheifproject.utilities.val_exit_screen_native
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentExitScreen :
    BaseFragment<FragmentExitScreenBinding>(FragmentExitScreenBinding::inflate) {
    private var adsManager: AdsManager? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adsManager = AdsManager.appAdsInit(activity ?: return)
        loadNative()
        setupBackPressedCallback {
        }

    }

    private fun loadNative() {
        lifecycleScope.launchWhenResumed {
            if(!val_exit_dialog_native){
                _binding?.nativeExitAd?.visibility = View.GONE
                _binding?.shimmerLayout?.visibility = View.GONE
                return@launchWhenResumed
            }
            val adView =layoutInflater.inflate(
                getNativeLayout(
                    thankyou_bottom,
                    _binding?.nativeExitAd!!,context?:return@launchWhenResumed),
                null
            ) as NativeAdView
            adsManager?.nativeAds()?.loadNativeAd(
                activity ?: return@launchWhenResumed,
                val_exit_screen_native,
                id_exit_screen_native,
                object : NativeListener {
                    override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                        if (isAdded && isVisible && !isDetached) {
                            _binding?.nativeExitAd?.visibility = View.VISIBLE
                            _binding?.shimmerLayout?.visibility = View.GONE
                            adsManager?.nativeAds()
                                ?.nativeViewMediaSplashSplash(context?:return,currentNativeAd ?: return, adView)
                            _binding?.nativeExitAd?.removeAllViews()
                            _binding?.nativeExitAd?.addView(adView)
                        }
                        super.nativeAdLoaded(currentNativeAd)
                    }

                    override fun nativeAdFailed(loadAdError: LoadAdError) {
                        _binding?.nativeExitAd?.visibility = View.INVISIBLE
                        _binding?.shimmerLayout?.visibility = View.INVISIBLE
                        super.nativeAdFailed(loadAdError)
                    }

                    override fun nativeAdValidate(string: String) {
                        _binding?.nativeExitAd?.visibility = View.INVISIBLE
                        _binding?.shimmerLayout?.visibility = View.INVISIBLE
                        super.nativeAdValidate(string)
                    }
                })
        }
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            lifecycleScope.launch {
                delay(6000)
                activity?.finishAffinity()
            }
        }
    }
}