package com.securityalarm.antitheifproject.ads_manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.appopen.AppOpenAd
import com.securityalarm.antitheifproject.ads_manager.NativeAds
import com.securityalarm.antitheifproject.utilities.isSplash
import java.util.*

/**
 * Created by
 * @Author: Javed Khan ,
 * @Company: Xpoosoft ,
 * @Email: xpoosoft@gmail.com ,
 * on 12/2/2021 , Thu .
 */


class AdOpenApp(private val myApplication: Application, private var openAppAdId: String) :
    Application.ActivityLifecycleCallbacks, LifecycleObserver {

    var appOpenAd: AppOpenAd? = null
    private var currentActivity: Activity? = null
    private var loadTime: Long = 0
    private var loadCallback: AppOpenAd.AppOpenAdLoadCallback? = null

    fun fetchAd() {
        if (isAdAvailable) {
            return
        }
        loadCallback = object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                openAdForSplash = appOpenAd
                this@AdOpenApp.appOpenAd = appOpenAd
                loadTime = Date().time
                super.onAdLoaded(appOpenAd)
            }

        }
        val request = adRequest
        if (openAppAdId == "" || NativeAds.isDebug()) {
            openAppAdId = "ca-app-pub-3940256099942544/3419835294"
        }
        AppOpenAd.load(
            myApplication, openAppAdId, request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback ?: return
        )
    }

    private val adRequest: AdRequest
        get() = AdRequest.Builder().build()

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    private val isAdAvailable: Boolean
        get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }


    fun showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable) {
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        fetchAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.d(LOG_TAG, "Will show ad. ${adError.message}")
                    }

                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }
                }
            if (isSplash) {
                appOpenAd?.show(currentActivity ?: return)
                appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
            }
        } else {
            Log.d(LOG_TAG, "Can not show ad.")
            fetchAd()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        showAdIfAvailable()
    }

    companion object {
        private const val LOG_TAG = "AppOpenManager"
        private var isShowingAd = false
        var openAdForSplash: AppOpenAd? = null
    }

    init {
        myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    /**
     * Init this class with in your Application class eg
    private lateinit var appOpenManager: AdOpenApp
    override fun onCreate() {
    super.onCreate()
    appOpenManager = AdOpenApp(this,"")
    }
     * */

}