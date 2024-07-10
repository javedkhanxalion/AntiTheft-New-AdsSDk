//package com.securityalarm.antitheifproject.adapter
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
//import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.AdsItemBinding
//import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.LanguageAppItemBinding
//import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
//import com.bmik.android.sdk.widgets.IkmNativeAdView
//import com.bmik.android.sdk.widgets.IkmWidgetAdLayout
//import com.bumptech.glide.Glide
//import com.securityalarm.antitheifproject.model.LanguageAppModel
//import com.securityalarm.antitheifproject.utilities.getNativeLayout
//import com.securityalarm.antitheifproject.utilities.languageinapp_scroll
//
//class LanguageAppAdapter(
//    private var clickItem: ((LanguageAppModel) -> Unit),
//    private var languageData: ArrayList<LanguageAppModel>,
//    private val firstAdPosition: Int,
//    private val repeatAdPosition: Int
//) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    companion object {
//        const val ITEM_TYPE = 0
//        const val AD_TYPE = 1
//    }
//
//    class ViewHolder(val binding: LanguageAppItemBinding) : RecyclerView.ViewHolder(binding.root)
//    class AdViewHolder(val bindingAds: AdsItemBinding) : RecyclerView.ViewHolder(bindingAds.root)
//
//    var context: Context? = null
//    var adNative: IkmNativeAdView? = null
//
//    override fun getItemViewType(position: Int): Int {
//        return if (isAdPosition(position)) {
//            AD_TYPE
//        } else {
//            ITEM_TYPE
//        }
//    }
//
//    private fun isAdPosition(position: Int): Boolean {
//        if (firstAdPosition == 0 && repeatAdPosition == 0)
//            return false
//        if (position == firstAdPosition) return true
//        if (position > firstAdPosition && (position - firstAdPosition) % repeatAdPosition == 0) return true
//        return false
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        context = parent.context
//        return if (viewType == AD_TYPE) {
//            AdViewHolder(AdsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//        } else {
//            ViewHolder(LanguageAppItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//        }
//    }
//
//    override fun getItemCount(): Int {
//        if (firstAdPosition == 0 && repeatAdPosition == 0)
//            return languageData.size
//        val adCount = (languageData.size - firstAdPosition + repeatAdPosition - 1) / repeatAdPosition
//        Log.d("check_language", "getItemCount: $adCount ${languageData.size}")
//        return languageData.size + adCount
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (getItemViewType(position) == AD_TYPE) {
//            val adHolder = holder as AdViewHolder
//            val adLayout = LayoutInflater.from(context).inflate(
//                getNativeLayout(languageinapp_scroll),
//                null, false
//            ) as NativeAdView
//            adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
//            adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
//            adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
//            adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
//            adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
//            if (adNative != null)
//                adHolder.bindingAds.nativeExitAd.loadNativeWithAdView(
//                    context, 0, adLayout!!, "languageinapp_scroll",
//                    "languageinapp_scroll", adNative!!,
//                    object : CustomSDKAdsListenerAdapter() {
//                        override fun onAdsLoadFail() {
//                            super.onAdsLoadFail()
//                            adHolder.bindingAds.nativeExitAd.visibility = View.GONE
//                        }
//
//                        override fun onAdsLoaded() {
//                            super.onAdsLoaded()
//                            adHolder.bindingAds.nativeExitAd.visibility = View.VISIBLE
//                        }
//                    }
//                )
//            else adHolder.bindingAds.nativeExitAd.loadAd(
//                context, 0, adLayout!!, "languageinapp_scroll",
//                "languageinapp_scroll",
//                object : CustomSDKAdsListenerAdapter() {
//                    override fun onAdsLoadFail() {
//                        super.onAdsLoadFail()
//                        adHolder.bindingAds.nativeExitAd.visibility = View.GONE
//                    }
//
//                    override fun onAdsLoaded() {
//                        super.onAdsLoaded()
//                        adHolder.bindingAds.nativeExitAd.visibility = View.VISIBLE
//                    }
//                }
//            )
//
//        } else {
//            val itemHolder = holder as ViewHolder
//            val actualPosition = getRealItemPosition(position)
//            itemHolder.binding.countryName.visibility = View.VISIBLE
//            itemHolder.binding.flagIcon.visibility = View.VISIBLE
//            itemHolder.binding.countryName.text = languageData[actualPosition].country_name
//            if (languageData[actualPosition].check) {
//                holder.binding.mainItem.setBackgroundResource(R.drawable.rect_menu_language_w)
//            } else {
//                holder.binding.mainItem.setBackgroundResource(R.drawable.rect_menu_language_un)
//            }
//            Glide.with(context ?: return).load(languageData[actualPosition].country_flag).into(itemHolder.binding.flagIcon)
//            itemHolder.binding.root.setOnClickListener {
//                checkSingleBox(actualPosition)
//                clickItem.invoke(languageData[actualPosition])
//            }
////            itemHolder.binding.radioButton.setOnClickListener {
////                checkSingleBox(actualPosition)
////                clickItem.invoke(languageData[actualPosition])
////            }
//        }
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun selectLanguage(langPositionSelected: String) {
//        for (i in languageData) {
//            i.check = i.country_code == langPositionSelected
//        }
//        notifyDataSetChanged()
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private fun checkSingleBox(pos: Int) {
//        for (i in languageData.indices) {
//            languageData[i].check = i == pos
//        }
//        notifyDataSetChanged()
//    }
//
//    private fun getRealItemPosition(position: Int): Int {
//        var adjustedPosition = position
//        if (firstAdPosition == 0 && repeatAdPosition == 0)
//            return adjustedPosition
//        if (position > firstAdPosition) {
//            adjustedPosition -= (position - firstAdPosition) / repeatAdPosition + 1
//        }
//        return adjustedPosition
//    }
//}
