package com.securityalarm.antitheifproject.ads_manager

import android.app.Activity
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.securityalarm.antitheifproject.ads_manager.FullScreenAdsTwo.mInterstitialAd
import com.securityalarm.antitheifproject.ads_manager.interfaces.AdMobAdListener
import com.securityalarm.antitheifproject.ads_manager.interfaces.AdsListener
import com.securityalarm.antitheifproject.utilities.counter
import com.securityalarm.antitheifproject.utilities.firebaseAnalytics
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

            }

            override fun fullScreenAdDismissed() {
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdDismissed: normal inter dismiss")
                firebaseAnalytics("inter_normal_dismisss_$tagClass", "inter_Show")
                loadTwoInterAds(
                    ads = ads,
                    activity = activity,
                    remoteConfigNormal = remoteConfigNormal,
                    adIdNormal = adIdNormal,
                    tagClass = tagClass
                )
                function.invoke(1)
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

    layout.visibility = View.VISIBLE
    Handler().postDelayed({
        ads.fullScreenAdsTwo().showAndLoadTwo(activity, remoteConfigNormal, object :
            AdMobAdListener {
            override fun fullScreenAdShow() {
                inter_frequency_count++
                function.invoke(1)
                Log.d(TAG, "fullScreenAdShow: normal inter ad show")
                firebaseAnalytics("inter_normal_show_$tagClass", "inter_Show")

            }

            override fun fullScreenAdDismissed() {
                layout.visibility = View.GONE
                Log.d(TAG, "fullScreenAdDismissed: normal inter dismiss")
                firebaseAnalytics("inter_normal_dismisss_$tagClass", "inter_Show")
                loadTwoInterAds(
                    ads = ads,
                    activity = activity,
                    remoteConfigNormal = remoteConfigNormal,
                    adIdNormal = adIdNormal,
                    tagClass = tagClass
                )
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

