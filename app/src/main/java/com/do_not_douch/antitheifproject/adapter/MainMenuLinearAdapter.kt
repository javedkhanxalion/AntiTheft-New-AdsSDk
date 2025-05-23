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
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.MenuItemLinearLayoutBinding
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.interfaces.NativeListener
import com.do_not_douch.antitheifproject.model.MainMenuModel
import com.do_not_douch.antitheifproject.utilities.clickWithThrottle
import com.do_not_douch.antitheifproject.utilities.getNativeLayout
import com.do_not_douch.antitheifproject.utilities.home_native
import com.do_not_douch.antitheifproject.utilities.id_native_screen
import com.do_not_douch.antitheifproject.utilities.loadImage
import com.do_not_douch.antitheifproject.utilities.val_ad_native_main_menu_screen

class MainMenuLinearAdapter(
    private val context: Activity,
    private val ads: AdsManager,
    private val menuItems: List<MainMenuModel>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onClick: ((MainMenuModel, Int) -> Unit)? = null
    val VIEW_TYPE_ONE = 1
    val VIEW_TYPE_ADS = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_ONE -> {
                val binding = MenuItemLinearLayoutBinding.inflate(layoutInflater, parent, false)
                MenuItemOneViewHolder(binding)
            }

            VIEW_TYPE_ADS -> {
                val binding = AdsItemBinding.inflate(layoutInflater, parent, false)
                MenuItemTwoViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        when (holder) {
            is MenuItemOneViewHolder -> {
                holder.bind(menuItems[position], onClick, context)
            }

            is MenuItemTwoViewHolder -> {
                holder.bind()
            }
        }
    }


    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 3) VIEW_TYPE_ADS else VIEW_TYPE_ONE
    }

    // First ViewHolder for the first layout
    inner class MenuItemOneViewHolder(private val binding: MenuItemLinearLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(
            menuItem: MainMenuModel,
            onClick: ((MainMenuModel, Int) -> Unit)?,
            context: Context,
        ) {
            binding.topImage.loadImage(context, menuItem.icon)
            if (menuItem.iconActive)
                binding.activeBtn.loadImage(context, R.drawable.active_icon)
            else
                binding.activeBtn.loadImage(context, R.drawable.un_active_icon)
            binding.title.text = menuItem.textTitle +" "+ menuItem.text
            binding.bottomView.clickWithThrottle {
                onClick?.invoke(menuItem, adapterPosition)
            }
        }
    }

    inner class MenuItemTwoViewHolder(private val binding: AdsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val adView = LayoutInflater.from(context).inflate(
                getNativeLayout(home_native, binding.nativeExitAd, context),
                null, false
            ) as NativeAdView
            ads.nativeAdsMain().loadNativeAd(
                context,
                val_ad_native_main_menu_screen,
                id_native_screen,
                object : NativeListener {
                    override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                        binding.nativeExitAd.visibility = View.VISIBLE
                        binding.shimmerLayout.visibility = View.GONE
                        ads.nativeAdsMain().nativeViewMedia(currentNativeAd ?: return, adView)
                        binding.nativeExitAd.removeAllViews()
                        binding.nativeExitAd.addView(adView)
                        super.nativeAdLoaded(currentNativeAd)
                    }

                    override fun nativeAdFailed(loadAdError: LoadAdError) {
                        binding.nativeExitAd.visibility = View.GONE
                        binding.shimmerLayout.visibility = View.GONE
                        super.nativeAdFailed(loadAdError)
                    }

                    override fun nativeAdValidate(string: String) {
                        binding.nativeExitAd.visibility = View.GONE
                        binding.shimmerLayout.visibility = View.GONE
                        super.nativeAdValidate(string)
                    }
                })
        }
    }


}
