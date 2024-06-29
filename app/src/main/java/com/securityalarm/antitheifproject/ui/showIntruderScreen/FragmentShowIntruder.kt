package com.securityalarm.antitheifproject.ui.showIntruderScreen

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.ShowIntruderFragmentBinding
import com.bmik.android.sdk.IkmSdkController
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.widgets.IkmWidgetAdLayout
import com.securityalarm.antitheifproject.adapter.IntruderAdapter
import com.securityalarm.antitheifproject.helper_class.Constants.getAntiTheftDirectory
import com.securityalarm.antitheifproject.model.IntruderModels
import com.securityalarm.antitheifproject.utilities.BaseFragment
import com.securityalarm.antitheifproject.utilities.firebaseAnalytics
import com.securityalarm.antitheifproject.utilities.getNativeLayout
import com.securityalarm.antitheifproject.utilities.getNativeLayoutShimmer
import com.securityalarm.antitheifproject.utilities.intruderimage_bottom
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
import java.io.File

class FragmentShowIntruder :
    BaseFragment<ShowIntruderFragmentBinding>(ShowIntruderFragmentBinding::inflate) {

    private var adapter: IntruderAdapter? = null
    private var allIntruderList: ArrayList<IntruderModels> = ArrayList()
    private var dir: File? = null
    private var isInternetDialog: Boolean = false
    companion object{
         var uriPic: Uri? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        if (!isInternetAvailable(context ?: return)) {
            IkmSdkController.setEnableShowResumeAds(false)
        }
    }
    override fun onResume() {
        super.onResume()

        dir?.let { getFile(it) }
        if (isInternetDialog) {
            if (!isInternetAvailable(context ?: return)) {
                IkmSdkController.setEnableShowResumeAds(false)
//                showInternetDialog(
//                    onPositiveButtonClick = {
//                        isInternetDialog = true
//                        openMobileDataSettings(context ?: requireContext())
//                    },
//                    onNegitiveButtonClick = {
//                        isInternetDialog = true
//                        openWifiSettings(context ?: requireContext())
//                    },
//                    onCloseButtonClick = {
//                    }
//                )
                return
            }else{
                IkmSdkController.setEnableShowResumeAds(true)
            }
        }
    }

    private fun loadNative() {
        val adLayout = LayoutInflater.from(context).inflate(
            getNativeLayout(intruderimage_bottom,_binding?.nativeExitAd!!,context?:return),
            null, false
        ) as? IkmWidgetAdLayout
        adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
        adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
        adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
        adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
        adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
        _binding?.nativeExitAd?.loadAd(
            activity ?: return,  getNativeLayoutShimmer(intruderimage_bottom),
            adLayout!!, "intruderimage_bottom",
            "intruderimage_bottom", object : CustomSDKAdsListenerAdapter() {

                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.nativeExitAd?.visibility = View.GONE
                }
            }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

}
