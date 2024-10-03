package com.do_not_douch.antitheifproject.ui.showIntruderScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.FragmentShowFullImageScreenBinding
import com.bumptech.glide.Glide
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.interfaces.NativeListener
import com.do_not_douch.antitheifproject.model.IntruderModels
import com.do_not_douch.antitheifproject.utilities.BaseFragment
import com.do_not_douch.antitheifproject.utilities.firebaseAnalytics
import com.do_not_douch.antitheifproject.utilities.getNativeLayout
import com.do_not_douch.antitheifproject.utilities.id_native_screen
import com.do_not_douch.antitheifproject.utilities.intruderimage_bottom
import com.do_not_douch.antitheifproject.utilities.setupBackPressedCallback
import com.do_not_douch.antitheifproject.utilities.showToast
import com.do_not_douch.antitheifproject.utilities.val_ad_native_show_image_screen
import java.io.File

class ShowFullImageFragment :
    BaseFragment<FragmentShowFullImageScreenBinding>(FragmentShowFullImageScreenBinding::inflate) {

    var models: IntruderModels? = null
    var ratingDialogDelete: AlertDialog? = null
    private var isInternetDialog: Boolean = false
    private var adsManager: AdsManager? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAnalytics("show_image_fragment_load", "show_image_fragment_load -->  Click")

        arguments?.let {
            models = it.getSerializable("obj") as IntruderModels
        }
        adsManager = AdsManager.appAdsInit(activity ?: return)
        Glide.with(this).load(Uri.fromFile(models?.file)).into(binding?.intruderimage!!)
        binding?.deleteBtn?.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.delete_dialog, null)
            ratingDialogDelete = AlertDialog.Builder(requireContext()).create()
            ratingDialogDelete?.setView(dialogView)
            ratingDialogDelete?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val cancel = dialogView.findViewById<View>(R.id.cancl_btn)
            val yes = dialogView.findViewById<View>(R.id.cnfrm_del_btn)

            yes.setOnClickListener {
                ratingDialogDelete?.dismiss()
                delPics()
            }
            cancel.setOnClickListener {
                ratingDialogDelete?.dismiss()
            }

            if (isVisible && isAdded && !isDetached) {
                ratingDialogDelete?.show()
            }

        }

        binding?.shareBtn?.setOnClickListener { shareImage() }
        binding?.backicon?.setOnClickListener {
            findNavController().navigateUp()
        }
        setupBackPressedCallback {
            findNavController().navigateUp()
        }
        loadNative()
    }

    private fun shareImage() {

        try {
            if (models?.file?.exists() == true) {
                val intent = Intent("android.intent.action.SEND")
                intent.type = "image/*"
                val uriForFile = FileProvider.getUriForFile(
                    context ?: return,
                    context?.packageName + ".fileprovider",
                    models?.file ?: return
                )
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.putExtra("android.intent.extra.STREAM", uriForFile)
                if (intent.resolveActivity(context?.packageManager ?: return) != null) {
                    startActivity(Intent.createChooser(intent, "Share via"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun delPics() {
        try {
            if (models?.file?.exists() == true && models?.file?.delete() == true) {
                showToast(getString(R.string.image_deleted))
                findNavController().popBackStack()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadNative() {

        val adView = LayoutInflater.from(context).inflate(
            getNativeLayout(intruderimage_bottom,_binding?.nativeExitAd!!,context?:return),
            null, false
        ) as NativeAdView
        adsManager?.nativeAds()?.loadNativeAd(
            activity ?: return,
            val_ad_native_show_image_screen,
            id_native_screen,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    if (!isAdded && !isVisible && isDetached) {
                        _binding?.nativeExitAd?.visibility = View.GONE
                        _binding?.adView?.visibility = View.GONE
                        return
                    }
                    _binding?.nativeExitAd?.visibility = View.VISIBLE
                    _binding?.adView?.visibility = View.GONE

                    adsManager?.nativeAds()?.nativeViewPolicy(context?:return,currentNativeAd ?: return, adView)
                    if (isAdded && isVisible && !isDetached) {
                        _binding?.nativeExitAd?.removeAllViews()
                        _binding?.nativeExitAd?.addView(adView)
                    }
                    super.nativeAdLoaded(currentNativeAd)
                }

                override fun nativeAdFailed(loadAdError: LoadAdError) {
                    _binding?.nativeExitAd?.visibility = View.GONE
                    _binding?.adView?.visibility = View.GONE
                    super.nativeAdFailed(loadAdError)
                }

                override fun nativeAdValidate(string: String) {
                    _binding?.nativeExitAd?.visibility = View.GONE
                    _binding?.adView?.visibility = View.GONE
                    super.nativeAdValidate(string)
                }
            })
    }

    private fun File.shareFile(context: Context) {
        // Create intent for sharing file
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(this))
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        // Launch the share intent
        context.startActivity(Intent.createChooser(intent, "Share File"))
    }

    override fun onPause() {
        super.onPause()
        isInternetDialog=true
    }
    override fun onResume() {
        super.onResume()
    }

}