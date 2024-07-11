package com.securityalarm.antitheifproject.ui.newIntro

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentMainIntroBinding
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.securityalarm.antitheifproject.ads_manager.AdsManager
import com.securityalarm.antitheifproject.ads_manager.interfaces.NativeListener
import com.securityalarm.antitheifproject.utilities.Onboarding_Full_Native
import com.securityalarm.antitheifproject.utilities.clickWithThrottle
import com.securityalarm.antitheifproject.utilities.getNativeLayout
import com.securityalarm.antitheifproject.utilities.getNativeLayoutShimmer
import com.securityalarm.antitheifproject.utilities.id_native_intro_screen
import com.securityalarm.antitheifproject.utilities.introDetailText
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.onboarding1_bottom
import com.securityalarm.antitheifproject.utilities.slideImages
import com.securityalarm.antitheifproject.utilities.val_native_intro_screen

class ImageFragment1 : Fragment() {

    private var ads: AdsManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding1 = FragmentMainIntroBinding.inflate(inflater, container, false)
        return _binding1?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt("position") ?: 0
        ads = AdsManager.appAdsInit(activity ?: return)
        _binding1?.sliderImage?.setImageResource(slideImages[position])
        _binding1?.sliderHeading?.text =
            com.securityalarm.antitheifproject.utilities.introHeading[position]
        _binding1?.sliderDesc?.text = introDetailText[position]
        _binding1?.wormDotsIndicator?.attachTo(viewPager22 ?: return)
        if (isInternetAvailable && Onboarding_Full_Native == 0) {
            if (position == 2) {
                _binding1?.nextApp?.visibility = View.VISIBLE
                _binding1?.skipApp?.visibility = View.INVISIBLE
            } else {
                _binding1?.nextApp?.visibility = View.VISIBLE
                _binding1?.skipApp?.visibility = View.VISIBLE
            }
        } else {
            if (position == 3) {
                _binding1?.nextApp?.visibility = View.VISIBLE
                _binding1?.skipApp?.visibility = View.INVISIBLE
            } else {
                _binding1?.nextApp?.visibility = View.VISIBLE
                _binding1?.skipApp?.visibility = View.VISIBLE
            }
        }
        _binding1?.nextApp?.clickWithThrottle {
            fragmentN?.invoke()
        }
        _binding1?.skipApp?.clickWithThrottle {
            fragmentA?.invoke()
        }
        if (isInternetAvailable(context ?: requireContext())) {
            loadNewNative1()
        } else {
            _binding1?.mainAdsNative?.visibility = View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding1 = null
    }

    companion object {
        var viewPager22: ViewPager2? = null
        var fragmentN: (() -> Unit)? = null
        var fragmentA: (() -> Unit)? = null
        var _binding1: FragmentMainIntroBinding? = null
        fun newInstance(
            position: Int,
            ads: Boolean,
            viewPager2: ViewPager2,
            fragmentNext: (() -> Unit),
            fragmentActivity: (() -> Unit),
        ) = ImageFragment1().apply {
            viewPager22 = viewPager2
            fragmentN = fragmentNext
            fragmentA = fragmentActivity
            arguments = Bundle().apply {
                putInt("position", position)
                putBoolean("ads", ads)
            }
        }

        fun onAdVisibilityChanged(visible: Boolean) {
            Log.d("check_position", "onPageScrolled: Fragment--1111 $visible")
            _binding1!!.mainAdsNative!!.visibility = if (visible) View.VISIBLE
            else
                View.INVISIBLE
        }
    }

    fun loadNewNative1() {
        val adView = LayoutInflater.from(context).inflate(
            getNativeLayout(onboarding1_bottom, _binding1?.mainAdsNative!!, context ?: return),
            null, false
        ) as NativeAdView

        ads?.nativeAdsMain()?.loadNativeAd(
            activity ?: return,
            val_native_intro_screen,
            id_native_intro_screen,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    _binding1?.mainAdsNative?.visibility = View.VISIBLE
                    _binding1?.shimmerLayout?.visibility = View.GONE
                    ads?.nativeAdsMain()?.nativeViewMedia(currentNativeAd ?: return, adView)
                    _binding1?.mainAdsNative?.removeAllViews()
                    _binding1?.mainAdsNative?.addView(adView)
                    super.nativeAdLoaded(currentNativeAd)
                }

                override fun nativeAdFailed(loadAdError: LoadAdError) {
                    _binding1?.mainAdsNative?.visibility = View.GONE
                    _binding1?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdFailed(loadAdError)
                }

                override fun nativeAdValidate(string: String) {
                    _binding1?.mainAdsNative?.visibility = View.GONE
                    _binding1?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdValidate(string)
                }
            })
    }

}
