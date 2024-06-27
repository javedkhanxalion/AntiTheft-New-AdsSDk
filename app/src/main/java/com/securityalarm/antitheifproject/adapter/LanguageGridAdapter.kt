package com.securityalarm.antitheifproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.AdsItemBinding
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.LanguageAppItemBinding
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.widgets.IkmNativeAdView
import com.bmik.android.sdk.widgets.IkmWidgetAdLayout
import com.securityalarm.antitheifproject.model.LanguageAppModel
import com.securityalarm.antitheifproject.utilities.getNativeLayout
import com.securityalarm.antitheifproject.utilities.languageinapp_scroll

class LanguageGridAdapter(private val items: List<LanguageAppModel>,
    private var clickItem: ((LanguageAppModel) -> Unit)) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var context: Context? = null
    var adNative: IkmNativeAdView? = null

    companion object {
        const val ITEM_TYPE = 0
        const val AD_TYPE = 1
    }

    class ViewHolder(val binding: LanguageAppItemBinding) : RecyclerView.ViewHolder(binding.root)
    class AdViewHolder(val bindingAds: AdsItemBinding) : RecyclerView.ViewHolder(bindingAds.root)


    override fun getItemViewType(position: Int): Int {
        return if (items[position].country_name == "Ad") AD_TYPE else ITEM_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return if (viewType == AD_TYPE) {
            AdViewHolder(
                AdsItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ViewHolder(
                LanguageAppItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_TYPE) {
            val item = items[position]
            (holder as ViewHolder).binding.flagIcon.setImageResource(item.country_flag)
            holder.binding.countryName.text = item.country_name
                        if (item.check) {
                holder.binding.mainItem.setBackgroundResource(R.drawable.rect_menu_language_w)
            } else {
                holder.binding.mainItem.setBackgroundResource(R.drawable.rect_menu_language_un)
            }
            holder.binding.mainItem.setOnClickListener {
                clickItem.invoke(item)
            }
        } else {
            val adHolder = holder as AdViewHolder
            val adLayout = LayoutInflater.from(context).inflate(
                getNativeLayout(languageinapp_scroll,holder.bindingAds?.nativeExitAd!!,context?:return),
                null, false
            ) as? IkmWidgetAdLayout
            adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
            adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
            adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
            adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
            adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
            if (adNative != null)
                adHolder.bindingAds.nativeExitAd.loadNativeWithAdView(
                    context, 0, adLayout!!, "languageinapp_scroll",
                    "languageinapp_scroll", adNative!!,
                    object : CustomSDKAdsListenerAdapter() {
                        override fun onAdsLoadFail() {
                            super.onAdsLoadFail()
                            adHolder.bindingAds.nativeExitAd.visibility = View.GONE
                        }

                        override fun onAdsLoaded() {
                            super.onAdsLoaded()
                            adHolder.bindingAds.nativeExitAd.visibility = View.VISIBLE
                        }
                    }
                )
            else adHolder.bindingAds.nativeExitAd.loadAd(
                context, 0, adLayout!!, "languageinapp_scroll",
                "languageinapp_scroll",
                object : CustomSDKAdsListenerAdapter() {
                    override fun onAdsLoadFail() {
                        super.onAdsLoadFail()
                        adHolder.bindingAds.nativeExitAd.visibility = View.GONE
                    }

                    override fun onAdsLoaded() {
                        super.onAdsLoaded()
                        adHolder.bindingAds.nativeExitAd.visibility = View.VISIBLE
                    }
                }
            )
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun selectLanguage(langPositionSelected: String) {
        for (i in items) {
            i.check = i.country_code == langPositionSelected
        }
        notifyDataSetChanged()
    }

}
