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
import com.bmik.android.sdk.IkmSdkController
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.listener.SdkAppOpenAdsCallback
import com.bmik.android.sdk.widgets.IkmWidgetAdLayout
import com.securityalarm.antitheifproject.utilities.clickWithThrottle
import com.securityalarm.antitheifproject.utilities.getNativeLayout
import com.securityalarm.antitheifproject.utilities.getNativeLayoutShimmer
import com.securityalarm.antitheifproject.utilities.introDetailText
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.onboarding1_bottom
import com.securityalarm.antitheifproject.utilities.onboarding2_bottom
import com.securityalarm.antitheifproject.utilities.onboarding3_bottom
import com.securityalarm.antitheifproject.utilities.slideImages

class ImageFragment : Fragment() {

    private var _binding: FragmentMainIntroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt("position") ?: 0
        _binding?.sliderImage?.setImageResource(slideImages[position])
        _binding?.sliderHeading?.text =
            com.securityalarm.antitheifproject.utilities.introHeading[position]
        _binding?.sliderDesc?.text = introDetailText[position]
        _binding?.wormDotsIndicator?.attachTo(viewPager22 ?: return)
        if (position == 3) {
            _binding?.nextApp?.visibility = View.VISIBLE
            _binding?.skipApp?.visibility = View.INVISIBLE
        } else {
            _binding?.nextApp?.visibility = View.VISIBLE
            _binding?.skipApp?.visibility = View.VISIBLE
        }
        _binding?.nextApp?.clickWithThrottle {
            fragmentN?.invoke()
        }
        _binding?.skipApp?.clickWithThrottle {
            fragmentA?.invoke()
        }
        if (isInternetAvailable(context?:requireContext())) {
            when (position) {
                0 -> {
                    loadNewNative1()
                }

                1 -> {
                    loadNewNative2()
                }

                2 -> {
                    _binding?.skipApp?.visibility = View.INVISIBLE
                    _binding?.nextApp?.text = getString(R.string.start)
                    loadNewNative3()
                }
            }
        } else {
            _binding?.mainAdsNative?.visibility = View.GONE
        }



        IkmSdkController.setAppOpenAdsCallback(callback =
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

        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var viewPager22: ViewPager2? = null
        var fragmentN: (() -> Unit)? = null
        var fragmentA: (() -> Unit)? = null
        fun newInstance(
            position: Int,
            ads: Boolean,
            viewPager2: ViewPager2,
            fragmentNext: (() -> Unit),
            fragmentActivity: (() -> Unit)
        ) = ImageFragment().apply {
            viewPager22 = viewPager2
            fragmentN = fragmentNext
            fragmentA = fragmentActivity
            arguments = Bundle().apply {
                putInt("position", position)
                putBoolean("ads", ads)
            }
        }
    }

    fun loadNewNative1() {
        val adLayout = LayoutInflater.from(context).inflate(
            getNativeLayout(onboarding1_bottom,_binding?.mainAdsNative!!,context?:return),
            null, false
        ) as? IkmWidgetAdLayout
        adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
        adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
        adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
        adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
        adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
        _binding?.mainAdsNative?.loadAd(
            activity ?: return,  getNativeLayoutShimmer(onboarding1_bottom),
            adLayout!!, "onboarding1_bottom",
            "onboarding1_bottom", object : CustomSDKAdsListenerAdapter() {

                override fun onAdsLoaded() {
                    super.onAdsLoaded()
                    _binding?.mainAdsNative?.visibility = View.VISIBLE
                }

                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.mainAdsNative?.visibility = View.GONE
                }
            }
        )

    }

    fun loadNewNative2() {
        val adLayout = LayoutInflater.from(context).inflate(
            getNativeLayout(onboarding2_bottom,_binding?.mainAdsNative!!,context?:return),
            null, false
        ) as? IkmWidgetAdLayout
        adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
        adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
        adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
        adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
        adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
        _binding?.mainAdsNative?.loadAd(
            activity ?: return,  getNativeLayoutShimmer(onboarding2_bottom),
            adLayout!!, "onboarding2_bottom",
            "onboarding2_bottom", object : CustomSDKAdsListenerAdapter() {
                override fun onAdsLoaded() {
                    super.onAdsLoaded()
                    _binding?.mainAdsNative?.visibility = View.VISIBLE
                }

                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.mainAdsNative?.visibility = View.GONE
                }
            }
        )

    }

    fun loadNewNative3() {
        val adLayout = LayoutInflater.from(context).inflate(
            getNativeLayout(onboarding3_bottom,_binding?.mainAdsNative!!,context?:return),
            null, false
        ) as? IkmWidgetAdLayout
        adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
        adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
        adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
        adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
        adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
        _binding?.mainAdsNative?.loadAd(
            activity ?: return,  getNativeLayoutShimmer(onboarding3_bottom),
            adLayout!!, "onboarding3_bottom",
            "onboarding3_bottom", object : CustomSDKAdsListenerAdapter() {

                override fun onAdsLoaded() {
                    super.onAdsLoaded()
                    _binding?.mainAdsNative?.visibility = View.VISIBLE
                }

                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.mainAdsNative?.visibility = View.GONE
                }

            }
        )
    }

}
