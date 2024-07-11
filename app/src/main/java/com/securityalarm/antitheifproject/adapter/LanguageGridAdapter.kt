package com.securityalarm.antitheifproject.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.AdsItemBinding
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.LanguageAppItemBinding
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.securityalarm.antitheifproject.ads_manager.AdsManager
import com.securityalarm.antitheifproject.ads_manager.interfaces.NativeListener
import com.securityalarm.antitheifproject.model.LanguageAppModel
import com.securityalarm.antitheifproject.utilities.getNativeLayout
import com.securityalarm.antitheifproject.utilities.id_language_scroll_screen_native
import com.securityalarm.antitheifproject.utilities.language_native_scroll
import com.securityalarm.antitheifproject.utilities.languageinapp_scroll

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
                language_native_scroll == 1,
                id_language_scroll_screen_native,
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
