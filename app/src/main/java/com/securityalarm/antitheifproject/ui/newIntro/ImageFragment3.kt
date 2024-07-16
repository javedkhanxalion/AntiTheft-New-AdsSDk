package com.securityalarm.antitheifproject.ui.newIntro

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
import com.securityalarm.antitheifproject.utilities.id_native_intro_screen2
import com.securityalarm.antitheifproject.utilities.introDetailText
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.onboarding1_bottom
import com.securityalarm.antitheifproject.utilities.onboarding2_bottom
import com.securityalarm.antitheifproject.utilities.onboarding3_bottom
import com.securityalarm.antitheifproject.utilities.slideImages
import com.securityalarm.antitheifproject.utilities.val_native_intro_screen
import com.securityalarm.antitheifproject.utilities.val_native_intro_screen2

class ImageFragment3 : Fragment()  {


    private var ads: AdsManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainIntroBinding.inflate(inflater, container, false)
        return _binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt("position") ?: 0
        ads = AdsManager.appAdsInit(activity ?: return)
        _binding?.sliderImage?.setImageResource(slideImages[position])
        _binding?.sliderHeading?.text =
            com.securityalarm.antitheifproject.utilities.introHeading[position]
        _binding?.sliderDesc?.text = introDetailText[position]
        _binding?.wormDotsIndicator?.attachTo(viewPager22 ?: return)
        if(isInternetAvailable && Onboarding_Full_Native ==0){
            if (position == 2) {
                _binding?.nextApp?.visibility = View.VISIBLE
                _binding?.skipApp?.visibility = View.INVISIBLE
            } else {
                _binding?.nextApp?.visibility = View.VISIBLE
                _binding?.skipApp?.visibility = View.VISIBLE
            }
        }else{
            if (position == 3) {
                _binding?.nextApp?.visibility = View.VISIBLE
                _binding?.skipApp?.visibility = View.INVISIBLE
            } else {
                _binding?.nextApp?.visibility = View.VISIBLE
                _binding?.skipApp?.visibility = View.VISIBLE
            }
        }
        _binding?.nextApp?.clickWithThrottle {
            fragmentN?.invoke()
        }
        _binding?.skipApp?.clickWithThrottle {
            fragmentA?.invoke()
        }
        if (isInternetAvailable(context?:requireContext())) {
                    _binding?.skipApp?.visibility = View.INVISIBLE
                    _binding?.nextApp?.text = getString(R.string.start)
                    loadNewNative3()
        } else {
            _binding?.mainAdsNative?.visibility = View.GONE
            _binding?.skipApp?.visibility = View.INVISIBLE
            _binding?.nextApp?.text = getString(R.string.start)
        }

     /*   IkmSdkController.setAppOpenAdsCallback(callback =
        object : SdkAppOpenAdsCallback {
            override fun onAdDismiss() {
                _binding?.mainAdsNative?.visibility = View.VISIBLE
                Log.d("app_open_call_back", " onAdDismiss")
            }

            override fun onAdLoading() {
                _binding?.mainAdsNative?.visibility = View.INVISIBLE
                Log.d("app_open_call_back", " onAdLoading")
            }

            override fun onAdsShowTimeout() {
                _binding?.mainAdsNative?.visibility = View.INVISIBLE
                Log.d("app_open_call_back", " onAdsShowTimeout")
            }

            override fun onShowAdComplete() {
                _binding?.mainAdsNative?.visibility = View.INVISIBLE
                Log.d("app_open_call_back", " onShowAdComplete")
            }

            override fun onShowAdFail() {
                _binding?.mainAdsNative?.visibility = View.VISIBLE
                Log.d("app_open_call_back", " onShowAdFail")
            }

        })*/

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var _binding: FragmentMainIntroBinding? = null
        var viewPager22: ViewPager2? = null
        var fragmentN: (() -> Unit)? = null
        var fragmentA: (() -> Unit)? = null
        fun newInstance(
            position: Int,
            ads: Boolean,
            viewPager2: ViewPager2,
            fragmentNext: (() -> Unit),
            fragmentActivity: (() -> Unit)
        ) = ImageFragment3().apply {
            viewPager22 = viewPager2
            fragmentN = fragmentNext
            fragmentA = fragmentActivity
            arguments = Bundle().apply {
                putInt("position", position)
                putBoolean("ads", ads)
            }
        }
        fun onAdVisibilityChanged(visible: Boolean) {
            Log.d("check_position", "onPageScrolled: Fragment--33333")
            if(!val_native_intro_screen2){
                return
            }
            _binding!!.mainAdsNative!!.visibility = if (visible) View.VISIBLE
            else
                View.INVISIBLE

        }
    }

    fun loadNewNative3() {
        val adView = LayoutInflater.from(context).inflate(
            getNativeLayout(onboarding3_bottom,_binding?.mainAdsNative!!,context?:return),
            null, false
        ) as NativeAdView
        ads?.nativeAdsMain()?.loadNativeAd(
            activity ?: return,
            val_native_intro_screen2,
            id_native_intro_screen2,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    _binding?.mainAdsNative?.visibility = View.VISIBLE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    ads?.nativeAdsMain()?.nativeViewMedia(currentNativeAd ?: return, adView)
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
