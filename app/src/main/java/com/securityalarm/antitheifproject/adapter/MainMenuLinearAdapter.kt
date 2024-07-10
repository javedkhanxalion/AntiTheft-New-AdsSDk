package com.securityalarm.antitheifproject.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.AdsItemBinding
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.MenuItemLinearLayoutBinding
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.widgets.IkmWidgetAdLayout
import com.securityalarm.antitheifproject.model.MainMenuModel
import com.securityalarm.antitheifproject.utilities.clickWithThrottle
import com.securityalarm.antitheifproject.utilities.getNativeLayout
import com.securityalarm.antitheifproject.utilities.getNativeLayoutShimmer
import com.securityalarm.antitheifproject.utilities.home_native
import com.securityalarm.antitheifproject.utilities.loadImage

class MainMenuLinearAdapter(
    private val context: Activity,
    private val menuItems: List<MainMenuModel>
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
            binding.title.text = menuItem.maniTextTitle
            binding.bottomView.clickWithThrottle {
                onClick?.invoke(menuItem, adapterPosition)
            }
        }
    }

    inner class MenuItemTwoViewHolder(private val binding: AdsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val adLayout = LayoutInflater.from(context).inflate(
                getNativeLayout(home_native,binding?.nativeExitAd!!,context?:return),
                null, false
            ) as NativeAdView
            adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
            adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
            adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
            adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
            adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
            binding?.nativeExitAd?.loadAd(
                context ?: return,  getNativeLayoutShimmer(home_native),
                adLayout!!, "intruder_native",
                "intruder_native", object : CustomSDKAdsListenerAdapter() {

                    override fun onAdsLoadFail() {
                        super.onAdsLoadFail()
                        binding.nativeExitAd.visibility = View.GONE
                    }
                }
            )
        }
    }


}
