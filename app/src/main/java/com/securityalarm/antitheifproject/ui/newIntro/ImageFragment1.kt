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
import com.bmik.android.sdk.IkmSdkController
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.listener.SdkAppOpenAdsCallback
import com.bmik.android.sdk.widgets.IkmWidgetAdLayout
import com.securityalarm.antitheifproject.utilities.Onboarding_Full_Native
import com.securityalarm.antitheifproject.utilities.clickWithThrottle
import com.securityalarm.antitheifproject.utilities.getNativeLayout
import com.securityalarm.antitheifproject.utilities.getNativeLayoutShimmer
import com.securityalarm.antitheifproject.utilities.introDetailText
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.onboarding1_bottom
import com.securityalarm.antitheifproject.utilities.slideImages

class ImageFragment1 : Fragment() {


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
        IkmSdkController.setAppOpenAdsCallback(callback =
        object : SdkAppOpenAdsCallback {
            override fun onAdDismiss() {
                _binding1?.mainAdsNative?.visibility = View.VISIBLE
                Log.d("app_open_call_back", " onAdDismiss")
            }

            override fun onAdLoading() {
                _binding1?.mainAdsNative?.visibility = View.INVISIBLE
                Log.d("app_open_call_back", " onAdLoading")
            }

            override fun onAdsShowTimeout() {
                _binding1?.mainAdsNative?.visibility = View.INVISIBLE
                Log.d("app_open_call_back", " onAdsShowTimeout")
            }

            override fun onShowAdComplete() {
                _binding1?.mainAdsNative?.visibility = View.INVISIBLE
                Log.d("app_open_call_back", " onShowAdComplete")
            }

            override fun onShowAdFail() {
                _binding1?.mainAdsNative?.visibility = View.VISIBLE
                Log.d("app_open_call_back", " onShowAdFail")
            }

        })


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
                View.GONE
        }
    }

    fun loadNewNative1() {
        val adLayout = LayoutInflater.from(context).inflate(
            getNativeLayout(onboarding1_bottom, _binding1?.mainAdsNative!!, context ?: return),
            null, false
        ) as? IkmWidgetAdLayout
        adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
        adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
        adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
        adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
        adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
        _binding1?.mainAdsNative?.loadAd(
            activity ?: return, getNativeLayoutShimmer(onboarding1_bottom),
            adLayout!!, "onboarding1_bottom",
            "onboarding1_bottom", object : CustomSDKAdsListenerAdapter() {

                override fun onAdsLoaded() {
                    super.onAdsLoaded()
                    _binding1?.mainAdsNative?.visibility = View.VISIBLE
                }

                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding1?.mainAdsNative?.visibility = View.GONE
                }
            }
        )

    }

}
