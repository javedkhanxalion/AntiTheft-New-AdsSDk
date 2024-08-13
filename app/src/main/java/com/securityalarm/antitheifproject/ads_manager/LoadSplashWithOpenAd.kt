package com.securityalarm.antitheifproject.ads_manager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.securityalarm.antitheifproject.ads_manager.FunctionClass.firebaseAnalytics
import com.securityalarm.antitheifproject.ads_manager.interfaces.AdMobAdListener
import com.securityalarm.antitheifproject.ads_manager.interfaces.AdsListener
import com.securityalarm.antitheifproject.utilities.id_inter_main_medium
import com.securityalarm.antitheifproject.utilities.id_inter_main_normal

var openAdForSplash: AppOpenForSplash? = null
var iS_SPLASH_AD_DISMISS = false
private const val TAGGED = "TwoInterAdsSplash"
var IS_OPEN_SHOW = false
fun loadTwoInterAdsSplash(
    ads: AdsManager,
    activity: Activity,
    remoteConfigNormal: Boolean,
    adIdNormal: String,
    tagClass: String
) {
    FullScreenAdsTwo.loadFullScreenAdTwo(activity = activity,
        addConfig = remoteConfigNormal,
        fullScreenAdId = adIdNormal,
        adsListener = object : AdsListener {
            override fun adFailed() {
                Log.d(TAGGED, "adFailed: normal inter failed")
                firebaseAnalytics("inter_normal_failed_$tagClass", "interLoaded")
            }

            override fun adLoaded() {
                Log.d(TAGGED, "adLoaded: normal inter load")
                firebaseAnalytics("inter_normal_loaded_$tagClass", "interLoaded")
            }

            override fun adNotFound() {
                Log.d(TAGGED, "adNotFound: normal not found")
                firebaseAnalytics("inter_normal_not_found_$tagClass", "interLoaded")
            }
        })
}


fun showNormalInterAdSingle(
    ads: AdsManager,
    activity: Activity,
    remoteConfigNormal: Boolean,
    tagClass: String,
    adIdNormal: String,
    layout: ConstraintLayout,
    function: ()->Unit
) {
    Log.d(TAGGED, "showNormalInterAd->adIdNormal: $adIdNormal")
    layout.visibility = View.VISIBLE
    Handler().postDelayed({
    FullScreenAdsTwo.showAndLoadTwo(activity, remoteConfigNormal, object : AdMobAdListener {
        override fun fullScreenAdShow() {
            Log.d(TAGGED, "fullScreenAdShow: normal inter ad show")
//            inter_frequency_count++
            firebaseAnalytics("inter_normal_show_$tagClass", "inter_Show")
            layout.visibility = View.GONE
//            ads.let {
//                loadTwoInterAds(
//                    ads = it,
//                    activity = activity,
//                    remoteConfigNormal = true,
//                    adIdNormal = id_inter_main_normal,
//                    tagClass = "home_pre_cache"
//                )
//            }
        }

        override fun fullScreenAdDismissed() {
            Log.d(TAGGED, "fullScreenAdDismissed: normal inter dismiss")
            firebaseAnalytics("inter_normal_dismisss_$tagClass", "inter_Show")
            layout.visibility = View.GONE
            iS_SPLASH_AD_DISMISS = true
            function.invoke()
        }

        override fun fullScreenAdFailedToShow() {
            Log.d(TAGGED, "fullScreenAdFailedToShow: normal inter failed to show")
            firebaseAnalytics("inter_normal_failed_show_$tagClass", "inter_Show")
            layout.visibility = View.GONE
            function.invoke()

        }

        override fun fullScreenAdNotAvailable() {
            Log.d(TAGGED, "fullScreenAdNotAvailable: normal inter not available")
            firebaseAnalytics("inter_normal_not_Found_$tagClass", "inter_Show")
            layout.visibility = View.GONE
            function.invoke()
        }

    }, id_inter_main_medium, object : AdsListener {
    })
    }, 500)
}

//fun loadOpenAdSplash(context: Context) {
//    openAdForSplash = AppOpenForSplash()
//    openAdForSplash?.loadAd(context, context.getString(R.string.app_open_splash))
//    Log.d(TAG, "loadOpenAdSplash: Load $openAdForSplash")
//}
//
//
//fun showOpenAd(activity: Activity) {
//    Log.d(TAG, "loadOpenAdSplash: SHow $openAdForSplash")
//    openAdForSplash?.showAdIfAvailable(activity)
//}
