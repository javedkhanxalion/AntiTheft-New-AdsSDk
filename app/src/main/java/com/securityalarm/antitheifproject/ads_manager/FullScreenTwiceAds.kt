package com.securityalarm.antitheifproject.ads_manager

import android.app.Activity
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.securityalarm.antitheifproject.ads_manager.FunctionClass.firebaseAnalytics
import com.securityalarm.antitheifproject.ads_manager.interfaces.AdMobAdListener
import com.securityalarm.antitheifproject.ads_manager.interfaces.AdsListener
import com.securityalarm.antitheifproject.utilities.counter
import com.securityalarm.antitheifproject.utilities.id_frequency_counter
import com.securityalarm.antitheifproject.utilities.id_inter_counter
import com.securityalarm.antitheifproject.utilities.inter_frequency_count


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
//    if (inter_frequency_count >= id_frequency_counter) {
//        return
//    }
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
    function: () -> Unit,
) {
    Log.d(TAG, "showTwoInterAd->adIdNormal: $adIdNormal")
    Log.d(TAG, "showTwoInterAd->id_inter_counter: $id_inter_counter")
//    Log.d(TAG, "showTwoInterAd->id_frequency_counter: $id_frequency_counter")
    Log.d(TAG, "showTwoInterAd->remoteConfigNormal: $remoteConfigNormal")
    if (!remoteConfigNormal){
        function.invoke()
        return
    }
    if (!AdsManager.isNetworkAvailable(activity)) {
        function.invoke()
        return
    }

    if (inter_frequency_count >= id_frequency_counter) {
        function.invoke()
        return
    }

    if (id_inter_counter != counter) {
        counter++
        Log.d(TAG, "showTwoInterAd->adIdNormalSkip: $counter")
        function.invoke()
        return
    } else {
        counter = 0
        Log.d(TAG, "showTwoInterAd->adIdNormalCounter: $counter")
    }
    layout.visibility = View.VISIBLE
    Handler().postDelayed({
        ads.fullScreenAdsTwo().showAndLoadTwo(activity, remoteConfigNormal, object : AdMobAdListener {
            override fun fullScreenAdShow() {
                inter_frequency_count++
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdShow: normal inter ad show")
                firebaseAnalytics("inter_normal_show_$tagClass", "inter_Show")

            }

            override fun fullScreenAdDismissed() {
                Log.d(TAG, "fullScreenAdDismissed: normal inter dismiss")
                firebaseAnalytics("inter_normal_dismisss_$tagClass", "inter_Show")
                loadTwoInterAds(
                    ads = ads,
                    activity = activity,
                    remoteConfigNormal = remoteConfigNormal,
                    adIdNormal = adIdNormal,
                    tagClass = tagClass
                )
                function.invoke()
            }

            override fun fullScreenAdFailedToShow() {
                Log.d(TAG, "fullScreenAdFailedToShow: normal inter failed to show")
                firebaseAnalytics("inter_normal_failed_show_$tagClass", "inter_Show")
                function.invoke()

            }

            override fun fullScreenAdNotAvailable() {
                Log.d(TAG, "fullScreenAdNotAvailable: normal inter not available")
                firebaseAnalytics("inter_normal_not_Found_$tagClass", "inter_Show")
                function.invoke()
            }

        }, adIdNormal, object : AdsListener {

        })

    }, 200)
}

fun showTwoInterAdExit(
    ads: AdsManager,
    activity: Activity,
    remoteConfigNormal: Boolean,
    adIdNormal: String,
    tagClass: String,
    isBackPress: Boolean,
    layout: ConstraintLayout,
    function: () -> Unit,
    functionFF: () -> Unit,
) {
    Log.d(TAG, "showTwoInterAd->adIdNormal: $adIdNormal")
    Log.d(TAG, "showTwoInterAd->id_inter_counter: $id_inter_counter")
//    Log.d(TAG, "showTwoInterAd->id_frequency_counter: $id_frequency_counter")
    Log.d(TAG, "showTwoInterAd->remoteConfigNormal: $remoteConfigNormal")

    layout.visibility = View.VISIBLE
    Handler().postDelayed({
        ads.fullScreenAdsTwo().showAndLoadTwo(activity, remoteConfigNormal, object :
            AdMobAdListener {
            override fun fullScreenAdShow() {
                inter_frequency_count++
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdShow: normal inter ad show")
                firebaseAnalytics("inter_normal_show_$tagClass", "inter_Show")

            }

            override fun fullScreenAdDismissed() {
                Log.d(TAG, "fullScreenAdDismissed: normal inter dismiss")
                firebaseAnalytics("inter_normal_dismisss_$tagClass", "inter_Show")
                loadTwoInterAds(
                    ads = ads,
                    activity = activity,
                    remoteConfigNormal = remoteConfigNormal,
                    adIdNormal = adIdNormal,
                    tagClass = tagClass
                )
                function.invoke()
            }

            override fun fullScreenAdFailedToShow() {
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdFailedToShow: normal inter failed to show")
                firebaseAnalytics("inter_normal_failed_show_$tagClass", "inter_Show")
                functionFF.invoke()
            }

            override fun fullScreenAdNotAvailable() {
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdNotAvailable: normal inter not available")
                firebaseAnalytics("inter_normal_not_Found_$tagClass", "inter_Show")
                functionFF.invoke()
            }

        }, "", object : AdsListener {

        })

    }, 200)
}

private fun showNormalInterAd(
    ads: AdsManager,
    activity: Activity,
    remoteConfigNormal: Boolean,
    tagClass: String,
    remoteConfigMedium: Boolean,
    adIdMedium: String,
    adIdNormal: String,
    function: () -> Unit,
) {
    Log.d(TAG, "showNormalInterAd->adIdMedium: $adIdMedium")
    Log.d(TAG, "showNormalInterAd->adIdNormal: $adIdNormal")
    if (!AdsManager.isNetworkAvailable(activity)) {
        return
    }
    ads.fullScreenAdsTwo().showAndLoadTwo(activity, remoteConfigNormal, object : AdMobAdListener {
        override fun fullScreenAdShow() {
            inter_frequency_count++
            Log.d(TAG, "fullScreenAdShow: normal inter ad show")
            firebaseAnalytics("inter_normal_show_$tagClass", "inter_Show")

        }

        override fun fullScreenAdDismissed() {
            Log.d(TAG, "fullScreenAdDismissed: normal inter dismiss")
            firebaseAnalytics("inter_normal_dismisss_$tagClass", "inter_Show")
            loadTwoInterAds(
                ads = ads,
                activity = activity,
                remoteConfigNormal = remoteConfigNormal,
                adIdNormal = adIdNormal,
                tagClass = tagClass
            )
            function.invoke()
        }

        override fun fullScreenAdFailedToShow() {
            Log.d(TAG, "fullScreenAdFailedToShow: normal inter failed to show")
            firebaseAnalytics("inter_normal_failed_show_$tagClass", "inter_Show")
            function.invoke()

        }

        override fun fullScreenAdNotAvailable() {
            Log.d(TAG, "fullScreenAdNotAvailable: normal inter not available")
            firebaseAnalytics("inter_normal_not_Found_$tagClass", "inter_Show")
            function.invoke()
        }

    }, adIdNormal, object : AdsListener {

    })

}

