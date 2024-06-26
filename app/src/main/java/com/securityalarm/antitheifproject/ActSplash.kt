package com.securityalarm.antitheifproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.bmik.android.sdk.SDKBaseController
import com.bmik.android.sdk.listener.CommonAdsListenerAdapter
import com.bmik.android.sdk.model.dto.CommonAdsAction
import com.bmik.android.sdk.model.dto.ScreenAds


class ActSplash : AppCompatActivity() {
    private var mOpenMainAction: (() -> Unit)? = null
    private var timerWaitAds: CountDownTimer? = null
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_1)
        initView()
    }
    private fun initView() {
        SDKBaseController.getInstance().onDataInitSuccessListener = CommonAdsAction {
            //do something
        }
        SDKBaseController.getInstance().onDataGetSuccessListener = {
            //do something
        }
        mOpenMainAction = {
            startMainAtc()
        }
        timerWaitAds =
            SDKBaseController.getInstance().showFirstOpenAppAds(this,
                object : CommonAdsListenerAdapter() {
                    override fun onAdsDismiss() {
                        moveToMain()
                    }

                    override fun onAdsShowFail(errorCode: Int) {
                        moveToMain()
                    }

                    override fun onAdsShowTimeout() {
                        super.onAdsShowTimeout()
                        Toast.makeText(this@ActSplash, "Ads show timeout", Toast.LENGTH_SHORT)
                            .show()
                    }

                })

    }
    private fun moveToMain() {
        SDKBaseController.getInstance()
            .loadInterstitialAds(
                this,
                screenAds = ScreenAds.START.value,
                trackingScreen = ScreenAds.START.value
            )
        mOpenMainAction?.invoke()
        mOpenMainAction = null

    }

    private fun startMainAtc() {
        timerWaitAds?.cancel()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerWaitAds?.cancel()
    }

}