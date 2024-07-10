package com.securityalarm.antitheifproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentExitScreenBinding
import com.google.android.gms.ads.nativead.NativeAdView
import com.securityalarm.antitheifproject.utilities.BaseFragment
import com.securityalarm.antitheifproject.utilities.getNativeLayout
import com.securityalarm.antitheifproject.utilities.getNativeLayoutShimmer
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
import com.securityalarm.antitheifproject.utilities.thankyou_bottom
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentExitScreen :
    BaseFragment<FragmentExitScreenBinding>(FragmentExitScreenBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCLickListener()
        loadNative()
        setupBackPressedCallback {
        }
        lifecycleScope.launch {
            delay(3000)
            activity?.finishAffinity()
        }
    }

    private fun loadNative() {
        val adLayout = LayoutInflater.from(context).inflate(
            getNativeLayout(thankyou_bottom,_binding?.nativeExitAd!!,context?:return),
            null, false
        ) as NativeAdView

    }

    private fun mCLickListener() {
        /*   _binding?.yesBtn?.setOnClickListener {
               activity?.finish()
           }
           _binding?.cancelBtn?.setOnClickListener {
               findNavController().navigateUp()
           }
           _binding?.rateImg?.setOnRatingBarChangeListener { ratingBar, fl, b ->
               if (fl > 3.5) {
                   startActivity(
                       Intent(
                           Intent.ACTION_VIEW,
                           Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
                       )
                   )
               }

           }*/
    }

}