package com.do_not_douch.antitheifproject.ads_manager

import android.app.Activity
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.do_not_douch.antitheifproject.ads_manager.FullScreenAdsTwo.mInterstitialAd
import com.do_not_douch.antitheifproject.ads_manager.interfaces.AdMobAdListener
import com.do_not_douch.antitheifproject.ads_manager.interfaces.AdsListener
import com.do_not_douch.antitheifproject.utilities.counter
import com.do_not_douch.antitheifproject.utilities.firebaseAnalytics
import com.do_not_douch.antitheifproject.utilities.id_frequency_counter
import com.do_not_douch.antitheifproject.utilities.id_inter_counter
import com.do_not_douch.antitheifproject.utilities.inter_frequency_count


const val TAG = "New_TwoInterAds"
fun loadTwoInterAds(
    ads: AdsManager,
    activity: Activity,
    remoteConfigNormal: Boolean,
    adIdNormal: String,
    tagClass: String,
) {
    Log.d(TAG, "loadTwoInterAds->adIdNormal: $adIdNormal")
    Log.d(TAG, "loadTwoInterAds: counter $inter_frequency_count $id_frequency_counter")
    if (!AdsManager.isNetworkAvailable(activity)) {
        return
    }
    if (inter_frequency_count >= id_frequency_counter) {
        return
    }
    ads.fullScreenAdsTwo().loadFullScreenAdTwo(activity = activity,
        addConfig = remoteConfigNormal,
        fullScreenAdId = adIdNormal,
        adsListener = object : AdsListener {
            override fun adFailed() {
                Log.d(TAG, "adFailed: normal inter failed")
                firebaseAnalytics("inter_normal_failed_$tagClass", "interLoaded")
            }

            override fun adLoaded() {
                Log.d(TAG, "adLoaded: normal inter load")
                firebaseAnalytics("inter_normal_loaded_$tagClass", "interLoaded")
            }

            override fun adNotFound() {
                Log.d(TAG, "adNotFound: normal not found")
                firebaseAnalytics("inter_normal_not_found_$tagClass", "interLoaded")
            }

            override fun adAlreadyLoaded() {
            }
        })

}


fun showTwoInterAd(
    ads: AdsManager,
    activity: Activity,
    remoteConfigNormal: Boolean,
    adIdNormal: String,
    tagClass: String,
    isBackPress: Boolean,
    layout: ConstraintLayout,
    function: (Int) -> Unit,
) {
    Log.d(TAG, "showTwoInterAd->adIdNormal: $adIdNormal")
    Log.d(TAG, "showTwoInterAd->id_inter_counter: $id_inter_counter")
    Log.d(TAG, "showTwoInterAd->id_frequency_counter: $mInterstitialAd")
    Log.d(TAG, "showTwoInterAd->remoteConfigNormal: $remoteConfigNormal")
    if (mInterstitialAd == null || (!remoteConfigNormal)) {
        loadTwoInterAds(
            ads = ads,
            activity = activity,
            remoteConfigNormal = remoteConfigNormal,
            adIdNormal = adIdNormal,
            tagClass = tagClass
        )
        function.invoke(0)
        return
    }
    if (!AdsManager.isNetworkAvailable(activity)) {
        function.invoke(0)
        return
    }

    if (inter_frequency_count >= id_frequency_counter) {
        function.invoke(0)
        return
    }

    if (id_inter_counter != counter) {
        counter++
        Log.d(TAG, "showTwoInterAd->adIdNormalSkip: $counter")
        function.invoke(0)
        return
    } else {
        counter = 0
        Log.d(TAG, "showTwoInterAd->adIdNormalCounter: $counter")
    }
    layout.visibility = View.VISIBLE
    Handler().postDelayed({
        ads.fullScreenAdsTwo().showAndLoadTwo(activity, remoteConfigNormal, object :
            AdMobAdListener {
            override fun fullScreenAdShow() {
                inter_frequency_count++
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdShow: normal inter ad show")
                firebaseAnalytics("inter_normal_show_$tagClass", "inter_Show")
                function.invoke(1)
            }

            override fun fullScreenAdDismissed() {
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdDismissed: normal inter dismiss")
                firebaseAnalytics("inter_normal_dismisss_$tagClass", "inter_Show")
            }

            override fun fullScreenAdFailedToShow() {
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdFailedToShow: normal inter failed to show")
                firebaseAnalytics("inter_normal_failed_show_$tagClass", "inter_Show")
                function.invoke(0)

            }

            override fun fullScreenAdNotAvailable() {
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdNotAvailable: normal inter not available")
                firebaseAnalytics("inter_normal_not_Found_$tagClass", "inter_Show")
                function.invoke(0)
            }

        }, adIdNormal, object : AdsListener {

        })

    }, 500)
}

fun showTwoInterAdExit(
    ads: AdsManager,
    activity: Activity,
    remoteConfigNormal: Boolean,
    adIdNormal: String,
    tagClass: String,
    isBackPress: Boolean,
    layout: ConstraintLayout,
    function: (Int) -> Unit,
) {
    Log.d(TAG, "showTwoInterAd->adIdNormal: $adIdNormal")
    Log.d(TAG, "showTwoInterAd->id_inter_counter: $id_inter_counter")
    Log.d(TAG, "showTwoInterAd->id_frequency_counter: $mInterstitialAd")
    Log.d(TAG, "showTwoInterAd->remoteConfigNormal: $remoteConfigNormal")
    if (mInterstitialAd == null || (!remoteConfigNormal)) {
//        loadTwoInterAds(
//            ads = ads,
//            activity = activity,
//            remoteConfigNormal = remoteConfigNormal,
//            adIdNormal = adIdNormal,
//            tagClass = tagClass
//        )
        function.invoke(0)
        return
    }
    if (!AdsManager.isNetworkAvailable(activity)) {
        function.invoke(0)
        return
    }

    layout.visibility = View.VISIBLE
    Handler().postDelayed({
        ads.fullScreenAdsTwo().showAndLoadTwo(activity, remoteConfigNormal, object :
            AdMobAdListener {
            override fun fullScreenAdShow() {
                inter_frequency_count++
                function.invoke(1)
                Log.d(TAG, "fullScreenAdShow: normal inter ad show")
                firebaseAnalytics("inter_normal_show_$tagClass", "inter_Show")
                function.invoke(0)
            }

            override fun fullScreenAdDismissed() {
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdDismissed: normal inter dismiss")
                firebaseAnalytics("inter_normal_dismisss_$tagClass", "inter_Show")
            }

            override fun fullScreenAdFailedToShow() {
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdFailedToShow: normal inter failed to show")
                firebaseAnalytics("inter_normal_failed_show_$tagClass", "inter_Show")
                function.invoke(0)

            }

            override fun fullScreenAdNotAvailable() {
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdNotAvailable: normal inter not available")
                firebaseAnalytics("inter_normal_not_Found_$tagClass", "inter_Show")
                function.invoke(0)
            }

        }, adIdNormal, object : AdsListener {

        })

    }, 1000)
}

fun showTwoInterAdFirst(
    ads: AdsManager,
    activity: Activity,
    remoteConfigNormal: Boolean,
    adIdNormal: String,
    tagClass: String,
    isBackPress: Boolean,
    layout: ConstraintLayout,
    function: () -> Unit,
) {
    Log.d(TAG, "showTwoInterAd->adIdNormal: $adIdNormal")
    Log.d(TAG, "showTwoInterAd->id_inter_counter: $id_inter_counter")
    Log.d(TAG, "showTwoInterAd->Adssssssss: $mInterstitialAd")
    Log.d(TAG, "showTwoInterAd->id_frequency_counter: $mInterstitialAd")
    if (mInterstitialAd == null || (!remoteConfigNormal)) {
        Log.d(TAG, "showTwoInterAd->remoteConfigNormal: 1")
        function.invoke()
        return
    }
    if (!AdsManager.isNetworkAvailable(activity)) {
        Log.d(TAG, "showTwoInterAd->remoteConfigNormal: 2")
        function.invoke()
        return
    }

    layout.visibility = View.VISIBLE
    Handler().postDelayed({
        ads.fullScreenAdsTwo().showAndLoadTwo(activity, remoteConfigNormal, object :
            AdMobAdListener {
            override fun fullScreenAdShow() {
                inter_frequency_count++
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdShow: normal inter ad show")
                firebaseAnalytics("inter_normal_show_$tagClass", "inter_Show")
                function.invoke()
            }

            override fun fullScreenAdDismissed() {
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdDismissed: normal inter dismiss")
                firebaseAnalytics("inter_normal_dismisss_$tagClass", "inter_Show")

            }

            override fun fullScreenAdFailedToShow() {
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdFailedToShow: normal inter failed to show")
                firebaseAnalytics("inter_normal_failed_show_$tagClass", "inter_Show")
                function.invoke()

            }

            override fun fullScreenAdNotAvailable() {
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdNotAvailable: normal inter not available")
                firebaseAnalytics("inter_normal_not_Found_$tagClass", "inter_Show")
                function.invoke()
            }

        }, adIdNormal, object : AdsListener {

        })

    }, 1000)
}


