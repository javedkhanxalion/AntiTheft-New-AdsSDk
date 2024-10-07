package com.do_not_douch.antitheifproject.ui.showIntruderScreen

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.ShowIntruderFragmentBinding
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.do_not_douch.antitheifproject.adapter.IntruderAdapter
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.interfaces.NativeListener
import com.do_not_douch.antitheifproject.helper_class.Constants.getAntiTheftDirectory
import com.do_not_douch.antitheifproject.model.IntruderModels
import com.do_not_douch.antitheifproject.utilities.BaseFragment
import com.do_not_douch.antitheifproject.utilities.PurchaseScreen
import com.do_not_douch.antitheifproject.utilities.clickWithThrottle
import com.do_not_douch.antitheifproject.utilities.firebaseAnalytics
import com.do_not_douch.antitheifproject.utilities.getNativeLayout
import com.do_not_douch.antitheifproject.utilities.id_native_screen
import com.do_not_douch.antitheifproject.utilities.intruderimage_bottom
import com.do_not_douch.antitheifproject.utilities.setupBackPressedCallback
import com.do_not_douch.antitheifproject.utilities.val_ad_native_intruder_list_screen
import com.do_not_douch.antitheifproject.utilities.val_inapp_frequency
import java.io.File

class FragmentShowIntruder :
    BaseFragment<ShowIntruderFragmentBinding>(ShowIntruderFragmentBinding::inflate) {

    private var adapter: IntruderAdapter? = null
    private var allIntruderList: ArrayList<IntruderModels> = ArrayList()
    private var dir: File? = null
    private var isInternetDialog: Boolean = false
    private var adsManager: AdsManager? = null
    companion object{
         var uriPic: Uri? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(++PurchaseScreen == val_inapp_frequency){
            PurchaseScreen =0
            findNavController().navigate(R.id.FragmentBuyScreen, bundleOf("isSplash" to false))
            return
        }
        adsManager = AdsManager.appAdsInit(activity ?: return)
        loadNative()
        setupViews()
        setupRecyclerView()
        setupBackPressedCallback {
            findNavController().navigateUp() }
        firebaseAnalytics(
            "show_intruder_fragment_load",
            "show_intruder_fragment_load_oncreate -->  Click"
        )

    }

    private fun setupViews() {
        dir = getAntiTheftDirectory()
        _binding?.backicon?.setOnClickListener {
            findNavController().navigateUp() }
    }

    private fun setupRecyclerView() {
        adapter = IntruderAdapter(allIntruderList, requireActivity()) { intruderModel ,uri_->
                    uriPic=uri_
                    findNavController().navigate(
                        R.id.ShowFullImageFragment,
                        bundleOf("obj" to intruderModel)
                    )
        }
        _binding?.intruderList?.adapter = adapter
    }

    private fun getFile(file: File) {
        _binding?.run {
            allIntruderList.clear()
            try {
                val listFiles = file.listFiles()
                listFiles?.let {
                    for (file2 in it) {
                        if (file2.isDirectory) {
                            getFile(file2)
                        } else if (file2.isFile && file2.name.endsWith(".jpg", true)
                            || file2.name.endsWith(".png", true) || file2.name.endsWith(
                                ".jpeg",
                                true
                            )
                        ) {
                            allIntruderList.add(IntruderModels(file2, false))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (allIntruderList.size > 0) {
                intruderList.visibility = View.VISIBLE
                noData.visibility = View.GONE
                adapter?.notifyDataSetChanged()
                nativeExitAd.visibility = View.GONE

            } else {
                intruderList.visibility = View.GONE
                noData.visibility = View.VISIBLE
                nativeExitAd.visibility = View.GONE
            }
        }

    }


    override fun onPause() {
        super.onPause()
        isInternetDialog=true
    }
    override fun onResume() {
        super.onResume()
        dir?.let { getFile(it) }
    }

    private fun loadNative() {
        val adLayout = LayoutInflater.from(context).inflate(
            getNativeLayout(intruderimage_bottom,_binding?.nativeExitAd!!,context?:return),
            null, false
        ) as NativeAdView
        adsManager?.nativeAds()?.loadNativeAd(
            activity ?: return,
            val_ad_native_intruder_list_screen,
            id_native_screen,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    _binding?.nativeExitAd?.visibility = View.VISIBLE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    if (isAdded && isVisible && !isDetached) {
                        adsManager?.nativeAds()?.nativeViewMediaSplashSplash(context?:return,currentNativeAd ?: return, adLayout)
                        _binding?.nativeExitAd?.removeAllViews()
                        _binding?.nativeExitAd?.addView(adLayout)
                    }
                    super.nativeAdLoaded(currentNativeAd)
                }

                override fun nativeAdFailed(loadAdError: LoadAdError) {
                    _binding?.nativeExitAd?.visibility = View.GONE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdFailed(loadAdError)
                }

                override fun nativeAdValidate(string: String) {
                    _binding?.nativeExitAd?.visibility = View.GONE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdValidate(string)
                }
            })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

}
