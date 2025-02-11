package com.do_not_douch.antitheifproject.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.AdsItemBinding
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.LanguageAppItemBinding
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.interfaces.NativeListener
import com.do_not_douch.antitheifproject.model.LanguageAppModel
import com.do_not_douch.antitheifproject.utilities.getNativeLayout
import com.do_not_douch.antitheifproject.utilities.id_native_screen
import com.do_not_douch.antitheifproject.utilities.languageinapp_scroll
import com.do_not_douch.antitheifproject.utilities.val_ad_native_language_screen

class LanguageGridAdapter(private val items: List<LanguageAppModel>,
                          private  val ads : AdsManager,
                          private  val activity1 : Activity,
                          private var clickItem: ((LanguageAppModel) -> Unit)) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var context: Context? = null

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
            ) as NativeAdView
            ads.nativeAdsMain().loadNativeAd(
                activity1?:return,
                val_ad_native_language_screen,
                id_native_screen,
                object : NativeListener {
                    override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                        adHolder.bindingAds.nativeExitAd.visibility = View.VISIBLE
                        adHolder.bindingAds.shimmerLayout.visibility = View.GONE
                        ads.nativeAdsMain().nativeViewMedia(currentNativeAd ?: return, adLayout)
                        adHolder.bindingAds.nativeExitAd.removeAllViews()
                        adHolder.bindingAds.nativeExitAd.addView(adLayout)
                        super.nativeAdLoaded(currentNativeAd)
                    }

                    override fun nativeAdFailed(loadAdError: LoadAdError) {
                        adHolder.bindingAds.nativeExitAd.visibility = View.GONE
                        adHolder.bindingAds.shimmerLayout.visibility = View.GONE
                        super.nativeAdFailed(loadAdError)
                    }

                    override fun nativeAdValidate(string: String) {
                        adHolder.bindingAds.nativeExitAd.visibility = View.GONE
                        adHolder.bindingAds.shimmerLayout.visibility = View.GONE
                        super.nativeAdValidate(string)
                    }
                })
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
