package com.do_not_douch.antitheifproject.ads_manager

import android.app.Activity
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.do_not_douch.antitheifproject.ads_manager.FunctionClass.firebaseAnalytics
import com.do_not_douch.antitheifproject.ads_manager.interfaces.AdMobAdListener
import com.do_not_douch.antitheifproject.ads_manager.interfaces.AdsListener
import com.do_not_douch.antitheifproject.utilities.id_inter_main_medium

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
            firebaseAnalytics("inter_normal_show_$tagClass", "inter_Show")
            function.invoke()
        }

        override fun fullScreenAdDismissed() {
            Log.d(TAGGED, "fullScreenAdDismissed: normal inter dismiss")
            firebaseAnalytics("inter_normal_dismisss_$tagClass", "inter_Show")
            layout.visibility = View.GONE
            iS_SPLASH_AD_DISMISS = true
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
    }, 1000)
}
