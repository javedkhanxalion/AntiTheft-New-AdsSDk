package com.do_not_douch.antitheifproject.ui.newIntro

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.IntroMainActivityBinding
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.PurchasePrefs
import com.do_not_douch.antitheifproject.ads_manager.interfaces.NativeListener
import com.do_not_douch.antitheifproject.ads_manager.showTwoInterAdFirst
import com.do_not_douch.antitheifproject.helper_class.DbHelper
import com.do_not_douch.antitheifproject.utilities.BaseFragment
import com.do_not_douch.antitheifproject.utilities.Onboarding_Full_Native
import com.do_not_douch.antitheifproject.utilities.firebaseAnalytics
import com.do_not_douch.antitheifproject.utilities.getNativeLayout
import com.do_not_douch.antitheifproject.utilities.id_inter_main_medium
import com.do_not_douch.antitheifproject.utilities.id_native_intro_screen
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
    private var isInternetDialog: Boolean = false
    private var ads: AdsManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAnalytics("intro_fragment_open", "intro_fragment_open -->  Click")
        isInternetAvailable = isNetworkAvailable(context?:return)
        ads = AdsManager.appAdsInit(activity?:return)
        val viewPagerAdapter = ViewPagerAdapter(
            context ?: return,
            this,
            { onNextButtonClicked() },
            { onNextSkipClicked() },
            binding?.viewPager ?: return
        )
        _binding?.viewPager?.adapter = viewPagerAdapter
        _binding?.viewPager?.offscreenPageLimit = viewPagerAdapter.itemCount
        _binding?.viewPager?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentpage = position
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                currentpage = position
                Log.d(
                    "scroll_check_position",
                    "onPageScrolled: $currentpage $position $positionOffset $positionOffsetPixels"
                )

            }

        })
        sharedPrefUtils = DbHelper(context ?: return)
        loadNewNative1()
        setupBackPressedCallback {
            firebaseAnalytics(
                "intro_fragment_move_to_next",
                "intro_fragment_move_to_next -->  Click"
            )
            ads?.let { it1 ->
                showTwoInterAdFirst(ads = it1,
                    activity = activity?:return@let,
                    remoteConfigNormal = val_inter_on_bord_screen,
                    adIdNormal = id_inter_main_medium,
                    tagClass = "on_bording",
                    layout = _binding?.adsLayDialog!!,
                    isBackPress = true,
                    function = {
                        if (PurchasePrefs(context).getBoolean("inApp") || !val_is_inapp_splash) {
                            findNavController().navigate(R.id.myMainMenuFragment, bundleOf("is_splash" to true))
                        } else {
                            findNavController().navigate(R.id.FragmentBuyScreen, bundleOf("isSplash" to true))
                        }
                    }
                )
            }
        }

    }

    private fun getItem(i: Int): Int {
        return _binding?.viewPager?.currentItem!! + i
    }

    fun onNextButtonClicked() {
        Log.d("check_click", "onViewCreated: 1")
        if (!isInternetAvailable || Onboarding_Full_Native == 0) {
            if (currentpage == 2) {
                firebaseAnalytics(
                    "intro_fragment_move_to_next",
                    "intro_fragment_move_to_next -->  Click"
                )
                ads?.let { it1 ->
                    showTwoInterAdFirst(ads = it1,
                        activity = activity?:return@let,
                        remoteConfigNormal = val_inter_on_bord_screen,
                        adIdNormal = id_inter_main_medium,
                        tagClass = "on_bording",
                        layout = _binding?.adsLayDialog!!,
                        isBackPress = true,
                        function = {
                            if (PurchasePrefs(context).getBoolean("inApp") || !val_is_inapp_splash) {
                                findNavController().navigate(R.id.myMainMenuFragment, bundleOf("is_splash" to true))
                            } else {
                                findNavController().navigate(R.id.FragmentBuyScreen, bundleOf("isSplash" to true))
                            }
                        }
                    )
                }
            } else {
                _binding?.viewPager?.setCurrentItem(getItem(+1), true)
            }
        } else {
            if (currentpage == 3) {
                firebaseAnalytics(
                    "intro_fragment_move_to_next",
                    "intro_fragment_move_to_next -->  Click"
                )
                ads?.let { it1 ->
                    showTwoInterAdFirst(ads = it1,
                        activity = activity?:return@let,
                        remoteConfigNormal = val_inter_on_bord_screen,
                        adIdNormal = id_inter_main_medium,
                        tagClass = "on_bording",
                        layout = _binding?.adsLayDialog!!,
                        isBackPress = true,
                        function = {
                            if (PurchasePrefs(context).getBoolean("inApp") || !val_is_inapp_splash) {
                                findNavController().navigate(R.id.myMainMenuFragment, bundleOf("is_splash" to true))
                            } else {
                                findNavController().navigate(R.id.FragmentBuyScreen, bundleOf("isSplash" to true))
                            }
                        }
                    )
                }
            } else {
                _binding?.viewPager?.setCurrentItem(getItem(+1), true)
            }
        }
    }

    private fun onNextSkipClicked() {
        Log.d("check_click", "onViewCreated: 2")
        firebaseAnalytics(
            "intro_fragment_move_to_next",
            "intro_fragment_move_to_next -->  Click"
        )
        if (isInternetAvailable || Onboarding_Full_Native == 0) {
            _binding?.viewPager?.setCurrentItem(getItem(+2), true)
        } else {
            _binding?.viewPager?.setCurrentItem(getItem(+3), true)
        }
    }

    override fun onPause() {
        super.onPause()
        isInternetDialog = true
    }

    override fun onResume() {
        super.onResume()
    }

    fun loadNewNative1() {
        val adView = LayoutInflater.from(context).inflate(
            getNativeLayout(onboarding1_bottom, _binding?.mainAdsNative!!, context ?: return),
            null, false
        ) as NativeAdView

        ads?.nativeAdsMain()?.loadNativeAd(
            activity ?: return,
            val_native_intro_screen,
            id_native_intro_screen,
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

