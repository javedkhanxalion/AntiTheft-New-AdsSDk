package com.securityalarm.antitheifproject.ui.newIntro

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.IntroMainActivityBinding
import com.bmik.android.sdk.IkmSdkController
import com.securityalarm.antitheifproject.helper_class.DbHelper
import com.securityalarm.antitheifproject.utilities.BaseFragment
import com.securityalarm.antitheifproject.utilities.IS_INTRO
import com.securityalarm.antitheifproject.utilities.Onboarding_Full_Native
import com.securityalarm.antitheifproject.utilities.firebaseAnalytics
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.openMobileDataSettings
import com.securityalarm.antitheifproject.utilities.openWifiSettings
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
import com.securityalarm.antitheifproject.utilities.showInternetDialog

class OnBordScreenNewScreen :
    BaseFragment<IntroMainActivityBinding>(IntroMainActivityBinding::inflate) {
    var currentpage = 0
    private var sharedPrefUtils: DbHelper? = null
    private var isInternetDialog: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAnalytics("intro_fragment_open", "intro_fragment_open -->  Click")

        val viewPagerAdapter = ViewPagerAdapter(
            context?:return,
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
            }

        })
        sharedPrefUtils = DbHelper(context ?: return)

        setupBackPressedCallback {
                firebaseAnalytics(
                    "intro_fragment_move_to_next",
                    "intro_fragment_move_to_next -->  Click"
                )
                sharedPrefUtils?.saveData(context ?: requireContext(), IS_INTRO, true)
                findNavController().navigate(
                    R.id.myMainMenuFragment
                )
        }

    }
    private fun getItem(i: Int): Int {
        return _binding?.viewPager?.currentItem!! + i
    }
    fun onNextButtonClicked() {
        Log.d("check_click", "onViewCreated: 1")
        if(isInternetAvailable && Onboarding_Full_Native ==0){
            if (currentpage == 2) {
                firebaseAnalytics(
                    "intro_fragment_move_to_next",
                    "intro_fragment_move_to_next -->  Click"
                )
                sharedPrefUtils?.saveData(context ?: requireContext(), IS_INTRO, true)
                IkmSdkController.setEnableShowResumeAds(true)
                findNavController().navigate(
                    R.id.myMainMenuFragment
                )
            } else {
                _binding?.viewPager?.setCurrentItem(getItem(+1), true)
            }
        }else
        {
            if (currentpage == 3) {
                firebaseAnalytics(
                    "intro_fragment_move_to_next",
                    "intro_fragment_move_to_next -->  Click"
                )
                sharedPrefUtils?.saveData(context ?: requireContext(), IS_INTRO, true)
                IkmSdkController.setEnableShowResumeAds(true)
                findNavController().navigate(
                    R.id.myMainMenuFragment
                )
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
        sharedPrefUtils?.saveData(context ?: requireContext(), IS_INTRO, true)
        findNavController().navigate(
            R.id.myMainMenuFragment
        )
    }

    override fun onPause() {
        super.onPause()
        isInternetDialog=true
        if(currentpage==1){
            IkmSdkController.setEnableShowResumeAds(false)
        }
    }
    override fun onResume() {
        super.onResume()
        if (isInternetDialog) {
            if (!isInternetAvailable(context ?: return)) {
                IkmSdkController.setEnableShowResumeAds(false)
     /*           showInternetDialog(
                    onPositiveButtonClick = {
                        isInternetDialog = true
                        openMobileDataSettings(context ?: requireContext())
                    },
                    onNegitiveButtonClick = {
                        isInternetDialog = true
                        openWifiSettings(context ?: requireContext())
                    },
                    onCloseButtonClick = {
                    }
                )*/
                return
            }else{
                if(currentpage==1){
                    IkmSdkController.setEnableShowResumeAds(false)
                }else{
                    IkmSdkController.setEnableShowResumeAds(true)
                }
            }
        }
    }

}

