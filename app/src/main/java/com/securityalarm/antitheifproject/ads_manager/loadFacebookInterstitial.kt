package com.securityalarm.antitheifproject.ads_manager

import android.content.Context
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener

private fun loadFacebookInterstitial(context : Context) {
    val facebookInterstitialAd = InterstitialAd(context, "YOUR_FACEBOOK_PLACEMENT_ID")
    facebookInterstitialAd.loadAd(
        facebookInterstitialAd.buildLoadAdConfig()
            .withAdListener(object : InterstitialAdListener {
                override fun onInterstitialDisplayed(ad: Ad) {
                    // Interstitial ad displayed callback
                }

                override fun onInterstitialDismissed(ad: Ad) {
                    // Interstitial dismissed callback
                }

                override fun onError(ad: Ad, adError: AdError) {
                    // Ad error callback
                }

                override fun onAdLoaded(ad: Ad) {
                    // Show the ad when it's loaded
                    facebookInterstitialAd.show()
                }

                override fun onAdClicked(ad: Ad) {
                    // Ad clicked callback
                }

                override fun onLoggingImpression(ad: Ad) {
                    // Ad impression logged callback
                }
            })
            .build()
    )
}
