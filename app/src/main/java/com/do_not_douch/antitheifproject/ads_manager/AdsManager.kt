package com.do_not_douch.antitheifproject.ads_manager

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.do_not_douch.antitheifproject.ads_manager.AdOpenApp.Companion.openAdForSplash
import com.do_not_douch.antitheifproject.utilities.id_app_open_screen


/**
 * Created by
 * @Author: Javed Khan ,
 */

object AdsManager {
    /*Ads*/
    private var appAds: AdsManager? = null
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    fun appAdsInit(activity: Activity): AdsManager {
        if (appAds == null) {
            appAds = AdsManager
            instance(activity)
            Log.d("adsInit", "   AdsClass")
        }
        return appAds as AdsManager
    }

    private fun instance(activity: Activity) {
        try {
            MobileAds.initialize(activity) {}
            MobileAds.setRequestConfiguration(
                RequestConfiguration.Builder()
                    .setTestDeviceIds(
                        listOf(
                            "6EEDFFFDB10991D90042D447C324A2C8",
                            "4C8533F42EF4302B2C5EBAD8342FED1E",
                            "3E11AE1541874059F31845BAC0C07141",
                            "22C89905A2F2191E022D6FDF3DEEA8AF",
                            "D082D453B2AD74DE2CA2D21AD93D892C",
                            "E5EB2E35D773FF290CEA242569D65135",
                            "6A38F63D0AF0EED8204DD6A830AFB817",
                            "0606A8A3B58F68943FF8F44212120F61",
                            "C4D52198B0C0E23A383A8C386E4DE7E5",
                            "0606A8A3B58F68943FF8F44212120F61",
                            "C4D52198B0C0E23A383A8C386E4DE7E5",
                            "2481431170D129B2BDCA7E933B99371B",
                            "9C6BF7A67630224C2115219E0B427854",
                            "54747ED837B721A1A1FDA0A58623AD30",
                            "4482FD1C9B039A733B15F74B5244B5E4",
                        )
                    )
                    .build()
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun adsBanners(): AdsBanners {
        return AdsBanners.adsBanner()
    }

    fun fullScreenAds(): FullScreenAds {
        return FullScreenAds.fullScreenAds()
    }

    fun fullScreenAdsTwo(): FullScreenAdsTwo {
        return FullScreenAdsTwo.fullScreenAdsTwo()
    }

    fun nativeAds(): NativeAds {
        return NativeAds.nativeAds()
    }

    fun nativeAdsMain(): NativeAdsSplash {
        return NativeAdsSplash.nativeAds()
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun showOpenAd(splashFragment: Activity, function: () -> Unit){
        Log.d(TAG, "loadOpenAdSplash: SHow $openAdForSplash")
        openAdForSplash?.show(splashFragment)
        splashFragment.let { AdOpenApp(it.application, id_app_open_screen) }
        openAdForSplash?.fullScreenContentCallback =  object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                function.invoke()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                function.invoke()
            }
        }
    }

//    fun loadOpenAdSplash(context: Context) {
//        openAdForSplash = AppOpenForSplash()
//        openAdForSplash?.loadAd(context, context.getString(R.string.app_open_splash))
//        Log.d(TAG, "loadOpenAdSplash: Load $openAdForSplash")
//    }
//    fun showOpenAd( splashFragment: Activity) {
//        Log.d(TAG, "loadOpenAdSplash: SHow $openAdForSplash")
//        openAdForSplash?.show(splashFragment)
//        splashFragment.let { AdOpenApp(it.application, splashFragment.getString(R.string.app_open_splash)) }
//    }

  /*  fun loadAdRewarded(context: Context, function: () -> Unit) {
        if(!val_show_reward_screen){
            return
        }
        MobileAds.initialize(context) {}
        RewardedInterstitialAd.load(context, if (FullScreenAds.isDebug())
            "ca-app-pub-3940256099942544/5354046379"
        else
            id_reward_screen,
            AdRequest.Builder().build(), object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    rewardedInterstitialAd = ad
                    rewardedInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdClicked() {
                                Log.d("rewarded_ads", "Ad was clicked.")
                            }

                            override fun onAdDismissedFullScreenContent() {
                                Log.d("rewarded_ads", "Ad dismissed fullscreen content.")
                                rewardedInterstitialAd = null
                                function.invoke()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                Log.e("rewarded_ads", "Ad failed to show fullscreen content.")
                                rewardedInterstitialAd = null
                                function.invoke()
                            }

                            override fun onAdImpression() {
                                Log.d("rewarded_ads", "Ad recorded an impression.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d("rewarded_ads", "Ad showed fullscreen content.")
                            }
                        }
                }

            })
    }
*/
    fun showRewardedAds(activity: Activity, function: () -> Unit) {
        rewardedInterstitialAd?.show(
            activity
        ) {
        }
    }

    /**
     * HELP
     *https://developers.google.com/admob/android/test-ads
     *
     *
     * implementation platform('com.google.firebase:firebase-bom:30.3.1')
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.android.gms:play-services-ads:21.1.0")
    implementation("com.android.billingclient:billing:4.1.0")

    OPEN APP SDK
    implementation "androidx.lifecycle:lifecycle-extensions:2.2.0"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0"
    annotationProcessor "androidx.lifecycle:lifecycle-compiler:2.2.0"


     * */


}