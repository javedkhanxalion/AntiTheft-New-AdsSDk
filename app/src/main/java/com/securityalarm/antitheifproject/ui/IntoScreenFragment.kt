//package com.securityalarm.antitheifproject.ui
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import androidx.core.os.bundleOf
//import androidx.navigation.fragment.findNavController
//import androidx.viewpager.widget.ViewPager.OnPageChangeListener
//import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
//import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentMainIntroBinding
//import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
//import com.bmik.android.sdk.widgets.IkmWidgetAdLayout
//import com.securityalarm.antitheifproject.adapter.IntroScreenAdapter
//import com.securityalarm.antitheifproject.helper_class.DbHelper
//import com.securityalarm.antitheifproject.utilities.BaseFragment
//import com.securityalarm.antitheifproject.utilities.IS_INTRO
//import com.securityalarm.antitheifproject.utilities.LANG_SCREEN
//import com.securityalarm.antitheifproject.utilities.clickWithThrottle
//import com.securityalarm.antitheifproject.utilities.firebaseAnalytics
//import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
//
//
//class IntoScreenFragment :
//    BaseFragment<FragmentMainIntroBinding>(FragmentMainIntroBinding::inflate) {
//
//    var currentpage = 0
//    private var introScreenAdapter: IntroScreenAdapter? = null
//    private var sharedPrefUtils: DbHelper? = null
//
//    private var viewListener: OnPageChangeListener = object : OnPageChangeListener {
//        override fun onPageScrolled(i: Int, v: Float, i1: Int) {
//            currentpage = i
//            when (i) {
//                0 -> {
//                    loadNewNative1()
//                }
//
//                1 -> {
//                    loadNewNative2()
//                }
//
//                2 -> {
//                    loadNewNative3()
//                }
//            }
//            _binding?.wormDotsIndicator?.attachTo(_binding?.mainSlideViewPager ?: return)
//        }
//
//        override fun onPageSelected(i: Int) {
//            currentpage = i
//        }
//
//        override fun onPageScrollStateChanged(i: Int) {}
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        firebaseAnalytics("intro_fragment_open", "intro_fragment_open -->  Click")
//        introScreenAdapter = IntroScreenAdapter(requireContext())
//        sharedPrefUtils = DbHelper(context ?: return)
//        _binding?.run {
//            skipApp.clickWithThrottle {
//                firebaseAnalytics(
//                    "intro_fragment_move_to_next",
//                    "intro_fragment_move_to_next -->  Click"
//                )
//                sharedPrefUtils?.saveData(requireContext(), IS_INTRO, true)
//                findNavController().navigate(R.id.LanguageFragment, bundleOf(LANG_SCREEN to true))
//            }
//            nextApp.clickWithThrottle {
//                if (currentpage == 2) {
//                    firebaseAnalytics(
//                        "intro_fragment_move_to_next",
//                        "intro_fragment_move_to_next -->  Click"
//                    )
//                    sharedPrefUtils?.saveData(requireContext(), IS_INTRO, true)
//                    findNavController().navigate(
//                        R.id.LanguageFragment,
//                        bundleOf(LANG_SCREEN to true)
//                    )
//                } else {
//                    mainSlideViewPager.setCurrentItem(getItem(+1), true)
//                }
//            }
//            mainSlideViewPager.adapter = introScreenAdapter
//            mainSlideViewPager.addOnPageChangeListener(viewListener)
//
//        }
//        setupBackPressedCallback {
//        }
//        loadNewNative1()
//    }
//
//    private fun getItem(i: Int): Int {
//        return _binding?.mainSlideViewPager?.currentItem!! + i
//    }
//
//    fun loadNewNative1() {
//        val adLayout = LayoutInflater.from(context).inflate(
//            R.layout.layout_custom_ad_native,
//            null, false
//        ) as? IkmWidgetAdLayout
//        adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
//        adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
//        adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
//        adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
//        adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
//        _binding?.mainAdsNative?.loadAd(
//            activity ?: return,  getNativeLayoutShimmer(),
//            adLayout!!, "onboarding1_bottom",
//            "onboarding1_bottom", object : CustomSDKAdsListenerAdapter() {
//
//                override fun onAdsLoaded() {
//                    super.onAdsLoaded()
//                    _binding?.mainAdsNative?.visibility = View.VISIBLE
//                }
//
//                override fun onAdsLoadFail() {
//                    super.onAdsLoadFail()
//                    _binding?.mainAdsNative?.visibility = View.GONE
//                }
//            }
//        )
//    }
//
//    fun loadNewNative2() {
//        val adLayout = LayoutInflater.from(context).inflate(
//            R.layout.layout_custom_ad_native,
//            null, false
//        ) as? IkmWidgetAdLayout
//        adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
//        adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
//        adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
//        adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
//        adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
//        _binding?.mainAdsNative?.loadAd(
//            activity ?: return,  getNativeLayoutShimmer(),
//            adLayout!!, "onboarding2_bottom",
//            "onboarding2_bottom", object : CustomSDKAdsListenerAdapter() {
//                override fun onAdsLoaded() {
//                    super.onAdsLoaded()
//                    _binding?.mainAdsNative?.visibility = View.VISIBLE
//                }
//                override fun onAdsLoadFail() {
//                    super.onAdsLoadFail()
//                    _binding?.mainAdsNative?.visibility = View.GONE
//                }
//            }
//        )
//    }
//
//    fun loadNewNative3() {
//        val adLayout = LayoutInflater.from(context).inflate(
//            R.layout.layout_custom_ad_native,
//            null, false
//        ) as? IkmWidgetAdLayout
//        adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
//        adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
//        adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
//        adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
//        adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
//        _binding?.mainAdsNative?.loadAd(
//            activity ?: return,  getNativeLayoutShimmer(),
//            adLayout!!, "onboarding3_bottom",
//            "onboarding3_bottom", object : CustomSDKAdsListenerAdapter() {
//
//                override fun onAdsLoaded() {
//                    super.onAdsLoaded()
//                    _binding?.mainAdsNative?.visibility = View.VISIBLE
//                }
//                override fun onAdsLoadFail() {
//                    super.onAdsLoadFail()
//                    _binding?.mainAdsNative?.visibility = View.GONE
//                }
//            }
//        )
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.clear()
//    }
//
//
//}