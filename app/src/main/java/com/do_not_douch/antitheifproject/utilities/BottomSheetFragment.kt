// BottomSheetFragment.kt
package com.do_not_douch.antitheifproject.utilities

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.interfaces.NativeListener

class BottomSheetFragment(val activity: Activity) : BottomSheetDialogFragment() {

    private var adsManager: AdsManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.exit_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adsManager = AdsManager.appAdsInit(activity)
        view.findViewById<TextView>(R.id.no).setOnClickListener {
            activity.finishAffinity()
        }
        view.findViewById<TextView>(R.id.yes).setOnClickListener {
            dismiss()
        }
        loadNative()
    }

    fun loadNative() {
        val adView = layoutInflater.inflate(
            getNativeLayout(
                exitdialog_bottom,
                view?.findViewById(R.id.nativeExitAd)!!, context ?: return
            ),
            null
        ) as NativeAdView
        adsManager?.nativeAdsMain()?.loadNativeAd(
            activity,
            val_exit_dialog_native,
            id_native_screen,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    if (isAdded && isVisible && !isDetached) {
                        view?.findViewById<FrameLayout>(R.id.nativeExitAd)?.visibility =
                            View.VISIBLE
                        view?.findViewById<ShimmerFrameLayout>(R.id.shimmerLayout)?.visibility =
                            View.GONE
                        adsManager?.nativeAdsMain()
                            ?.nativeViewMedia(currentNativeAd ?: return, adView)
                        view?.findViewById<FrameLayout>(R.id.nativeExitAd)?.removeAllViews()
                        view?.findViewById<FrameLayout>(R.id.nativeExitAd)?.addView(adView)
                    }
                    super.nativeAdLoaded(currentNativeAd)
                }

                override fun nativeAdFailed(loadAdError: LoadAdError) {
                    view?.findViewById<FrameLayout>(R.id.nativeExitAd)?.visibility = View.GONE
                    view?.findViewById<ShimmerFrameLayout>(R.id.shimmerLayout)?.visibility =
                        View.GONE
                    super.nativeAdFailed(loadAdError)
                }

                override fun nativeAdValidate(string: String) {
                    view?.findViewById<FrameLayout>(R.id.nativeExitAd)?.visibility = View.GONE
                    view?.findViewById<ShimmerFrameLayout>(R.id.shimmerLayout)?.visibility =
                        View.GONE
                    super.nativeAdValidate(string)
                }
            }
        )
    }

}
