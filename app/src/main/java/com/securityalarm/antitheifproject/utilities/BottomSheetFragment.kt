// BottomSheetFragment.kt
package com.securityalarm.antitheifproject.utilities

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.widgets.IkmWidgetAdLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment(val activity: Activity) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.exit_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.no).setOnClickListener {
            activity.finishAffinity()
        }
        view.findViewById<TextView>(R.id.yes).setOnClickListener {
            dismiss()
        }
        loadNative()
    }

    fun loadNative() {
        val adLayout = LayoutInflater.from(context).inflate(
            getNativeLayout(exitdialog_bottom, view?.findViewById<com.bmik.android.sdk.widgets.IkmWidgetAdView>(R.id.nativeExitAd)!!),
            null, false
        ) as? IkmWidgetAdLayout
        adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
        adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
        adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
        adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
        adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
        view?.findViewById<com.bmik.android.sdk.widgets.IkmWidgetAdView>(R.id.nativeExitAd)?.loadAd(
            context, R.layout.shimmer_loading_native,
            adLayout!!, "exitdialog_bottom",
            "exitdialog_bottom", object : CustomSDKAdsListenerAdapter() {

                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    view?.findViewById<com.bmik.android.sdk.widgets.IkmWidgetAdView>(R.id.nativeExitAd)?.visibility =
                        View.GONE
                }
            }
        )
    }
}
