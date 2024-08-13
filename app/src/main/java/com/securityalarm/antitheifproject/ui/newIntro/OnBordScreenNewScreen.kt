package com.securityalarm.antitheifproject.ui.newIntro

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.IntroMainActivityBinding
import com.securityalarm.antitheifproject.ads_manager.AdsManager
import com.securityalarm.antitheifproject.ads_manager.showTwoInterAdFirst
import com.securityalarm.antitheifproject.helper_class.DbHelper
import com.securityalarm.antitheifproject.utilities.BaseFragment
import com.securityalarm.antitheifproject.utilities.Onboarding_Full_Native
import com.securityalarm.antitheifproject.utilities.firebaseAnalytics
import com.securityalarm.antitheifproject.utilities.id_inter_main_medium
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.isNetworkAvailable
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
import com.securityalarm.antitheifproject.utilities.val_inter_on_bord_screen

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

                if (isInternetAvailable || Onboarding_Full_Native == 1) {
                    when (currentpage) {
                        0 -> {
                            if (positionOffset.toDouble() == 0.0)
                                ImageFragment1.onAdVisibilityChanged(true)
                            else
                                ImageFragment1.onAdVisibilityChanged(false)
                        }

                        1 -> {
                            ImageFragment1.onAdVisibilityChanged(false)
                            ImageFragment2.onAdVisibilityChanged(false)
                        }

                        2 -> {
                            if (positionOffset.toDouble() == 0.0)
                                ImageFragment2.onAdVisibilityChanged(true)
                            else
                                ImageFragment2.onAdVisibilityChanged(false)
                        }

                        3 -> {
                            if (positionOffset.toDouble() == 0.0)
                                ImageFragment3.onAdVisibilityChanged(true)
                            else
                                ImageFragment3.onAdVisibilityChanged(false)
                        }
                    }
                } else {
                        when (currentpage) {
                            0 -> {
                                if (positionOffset.toDouble() == 0.0)
                                    ImageFragment1.onAdVisibilityChanged(true)
                                else
                                    ImageFragment1.onAdVisibilityChanged(false)
                            }

                            1 -> {
                                if (positionOffset.toDouble() == 0.0)
                                    ImageFragment2.onAdVisibilityChanged(true)
                                else
                                    ImageFragment2.onAdVisibilityChanged(false)
                            }

                            2 -> {
                                if (positionOffset.toDouble() == 0.0)
                                    ImageFragment3.onAdVisibilityChanged(true)
                                else
                                    ImageFragment3.onAdVisibilityChanged(false)
                            }
                        }
                }

            }

        })
        sharedPrefUtils = DbHelper(context ?: return)

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
                        findNavController().navigate(
                            R.id.myMainMenuFragment
                        )
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
                            findNavController().navigate(
                                R.id.myMainMenuFragment
                            )
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
                            findNavController().navigate(
                                R.id.myMainMenuFragment
                            )
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

}

