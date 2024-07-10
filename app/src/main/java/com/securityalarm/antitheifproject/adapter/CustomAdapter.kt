//package com.securityalarm.antitheifproject.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
//import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
//import com.bmik.android.sdk.widgets.IkmNativeAdView
//import com.bmik.android.sdk.widgets.IkmWidgetAdLayout
//import FrameLayout
//import com.securityalarm.antitheifproject.model.LanguageAppModel
//
//class CustomAdapter(private val mList: List<LanguageAppModel>) :
//    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
//    var adNative: IkmNativeAdView? = null
//
//    // create new views
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        // inflates the card_view_design view 
//        // that is used to hold list item
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.language_app_item, parent, false)
//
//        return ViewHolder(view)
//    }
//
//    // binds the list items to a view
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.onBind(mList[position].typeView)
//    }
//
//    // return the number of the items in the list
//    override fun getItemCount(): Int {
//        return mList.size
//    }
//
//    // Holds the views for adding it to image and text
//    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
//        val ads = itemView.findViewById<IkmWidgetAdView>(R.id.item_adsNative)
//        val content = itemView.findViewById<View>(R.id.item_content)
//        fun onBind(typeView: Int) {
//            if (typeView != 0) {
//                content?.visibility = View.VISIBLE
//                ads?.visibility = View.GONE
//            } else {
//                val adLayout = LayoutInflater.from(content.context).inflate(
//                    R.layout.layout_custom_ad_native,
//                    null, false
//                ) as NativeAdView
//                adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
//                adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
//                adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
//                adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
//                adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
//                content?.visibility = View.GONE
//                ads?.visibility = View.VISIBLE
//                if (adNative != null)
//                    ads.loadNativeWithAdView(
//                        content.context, 0, adLayout!!, "home_native",
//                        "home_native", adNative!!,
//                        object : CustomSDKAdsListenerAdapter() {
//                            override fun onAdsLoadFail() {
//                                super.onAdsLoadFail()
//                            }
//
//                            override fun onAdsLoaded() {
//                                super.onAdsLoaded()
//                            }
//                        }
//                    )
//                else ads.loadAd(
//                    content.context, 0, adLayout!!, "home_native",
//                    "home_native",
//                    object : CustomSDKAdsListenerAdapter() {
//                        override fun onAdsLoadFail() {
//                            super.onAdsLoadFail()
//                        }
//
//                        override fun onAdsLoaded() {
//                            super.onAdsLoaded()
//                        }
//                    }
//                )
//
//            }
//        }
//    }
//}