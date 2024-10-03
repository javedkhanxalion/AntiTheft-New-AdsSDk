package com.do_not_douch.antitheifproject.ui.newIntro

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.IntroMainActivityBinding
import com.do_not_douch.antitheifproject.adapter.OnBordScreenAdapter
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.PurchasePrefs
import com.do_not_douch.antitheifproject.ads_manager.interfaces.NativeListener
import com.do_not_douch.antitheifproject.ads_manager.showTwoInterAd
import com.do_not_douch.antitheifproject.helper_class.DbHelper
import com.do_not_douch.antitheifproject.utilities.BaseFragment
import com.do_not_douch.antitheifproject.utilities.IS_INTRO
import com.do_not_douch.antitheifproject.utilities.LANG_SCREEN
import com.do_not_douch.antitheifproject.utilities.Onboarding_Full_Native
import com.do_not_douch.antitheifproject.utilities.clickWithThrottle
import com.do_not_douch.antitheifproject.utilities.firebaseAnalytics
import com.do_not_douch.antitheifproject.utilities.getNativeLayout
import com.do_not_douch.antitheifproject.utilities.id_inter_main_medium
import com.do_not_douch.antitheifproject.utilities.id_native_screen
import com.do_not_douch.antitheifproject.utilities.isInternetAvailable
import com.do_not_douch.antitheifproject.utilities.isNetworkAvailable
import com.do_not_douch.antitheifproject.utilities.onboarding1_bottom
import com.do_not_douch.antitheifproject.utilities.setupBackPressedCallback
import com.do_not_douch.antitheifproject.utilities.val_inter_on_bord_screen
import com.do_not_douch.antitheifproject.utilities.val_is_inapp_splash
import com.do_not_douch.antitheifproject.utilities.val_native_intro_screen
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class OnBordScreenNewScreen :
    BaseFragment<IntroMainActivityBinding>(IntroMainActivityBinding::inflate) {
    var currentpage = 0
    private var sharedPrefUtils: DbHelper? = null
    private var onBordScreenAdapter: OnBordScreenAdapter? = null
    private var ads: AdsManager? = null

    private var viewListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(i: Int, v: Float, i1: Int) {
            currentpage = i

            _binding?.wormDotsIndicator?.attachTo(_binding?.viewPager?:return)
            if (currentpage == 4) {
                _binding?.skipApp?.visibility = View.INVISIBLE
                _binding?.nextApp?.text = getString(R.string.finish)
            } else {
                _binding?.skipApp?.visibility = View.VISIBLE
                _binding?.nextApp?.text = getString(R.string.next)
            }
        }

        override fun onPageSelected(i: Int) {
            currentpage = i
        }

        override fun onPageScrollStateChanged(i: Int) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAnalytics("intro_fragment_open", "intro_fragment_open -->  Click")
        onBordScreenAdapter = OnBordScreenAdapter(requireContext())
        sharedPrefUtils = DbHelper(context ?: return)
        ads = AdsManager.appAdsInit(activity ?: return)
        _binding?.run {
            viewPager.adapter = onBordScreenAdapter
            viewPager.addOnPageChangeListener(viewListener)
            skipApp.clickWithThrottle {
                firebaseAnalytics(
                    "intro_fragment_move_to_next",
                    "intro_fragment_move_to_next -->  Click"
                )
                sharedPrefUtils?.saveData(requireContext(), IS_INTRO, true)
                findNavController().navigate(
                    R.id.myMainMenuFragment
                )
            }
            nextApp.clickWithThrottle {
                if (currentpage == 4) {
                    firebaseAnalytics(
                        "intro_fragment_move_to_next",
                        "intro_fragment_move_to_next -->  Click"
                    )
                    sharedPrefUtils?.saveData(requireContext(), IS_INTRO, true)
                    findNavController().navigate(
                        R.id.myMainMenuFragment
                    )
                } else {
                    viewPager.setCurrentItem(getItem(+1), true)
                }
            }
        }
        loadNative()
        setupBackPressedCallback {
        }

    }

    private fun getItem(i: Int): Int {
        return _binding?.viewPager?.currentItem!! + i
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    fun loadNative() {
        val adView = LayoutInflater.from(context).inflate(
            getNativeLayout(onboarding1_bottom, _binding?.mainAdsNative!!, context ?: return),
            null, false
        ) as NativeAdView

        ads?.nativeAdsMain()?.loadNativeAd(
            activity ?: return,
            val_native_intro_screen,
            id_native_screen,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    _binding?.mainAdsNative?.visibility = View.VISIBLE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    ads?.nativeAdsMain()?.nativeViewMediaSplashSplash(context?:return,currentNativeAd ?: return, adView)
                    _binding?.mainAdsNative?.removeAllViews()
                    _binding?.mainAdsNative?.addView(adView)
                    super.nativeAdLoaded(currentNativeAd)
                }

                override fun nativeAdFailed(loadAdError: LoadAdError) {
                    _binding?.mainAdsNative?.visibility = View.GONE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdFailed(loadAdError)
                }

                override fun nativeAdValidate(string: String) {
                    _binding?.mainAdsNative?.visibility = View.GONE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdValidate(string)
                }
            })
    }

}

