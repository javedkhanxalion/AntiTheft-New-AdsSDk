package com.securityalarm.antitheifproject.ui.newIntro

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.securityalarm.antitheifproject.utilities.Onboarding_Full_Native
import com.securityalarm.antitheifproject.utilities.isInternetAvailable

class ViewPagerAdapter(
    val context: Context,
    val fragmentActivity: OnBordScreenNewScreen,
    functionNext: (()->Unit),
    function: (()->Unit),
    viewPager2: ViewPager2
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        ImageFragment1.newInstance(0, false, viewPager2, functionNext, function),
        AdFragment(),
        ImageFragment2.newInstance(1, false, viewPager2, functionNext, function),
        ImageFragment3.newInstance(2, false, viewPager2, functionNext, function),
    )
    private val fragments1 = listOf(
        ImageFragment1.newInstance(0, false, viewPager2, functionNext, function),
        ImageFragment2.newInstance(1, false, viewPager2, functionNext, function),
        ImageFragment3.newInstance(2, false, viewPager2, functionNext, function),
    )

    override fun getItemCount(): Int = if(isInternetAvailable && Onboarding_Full_Native==1)
        fragments.size
    else
        fragments1.size

    override fun createFragment(position: Int): Fragment = if(isInternetAvailable && Onboarding_Full_Native==1)
        fragments[position]
    else
        fragments1[position]

//    fun setAdVisibility(visible: Boolean) {
//        if(isInternetAvailable && Onboarding_Full_Native==1){
//            fragments.forEach { fragment ->
//                if (fragment is AdVisibilityListener) {
//                    fragment.onAdVisibilityChanged(visible)
//                }
//            }
//        }else{
//            fragments1.forEach { fragment ->
//                if (fragment is AdVisibilityListener) {
//                    fragment.onAdVisibilityChanged(visible)
//                }
//            }
//        }
//    }

}
//interface AdVisibilityListener {
//    fun onAdVisibilityChanged(visible: Boolean)
//}
