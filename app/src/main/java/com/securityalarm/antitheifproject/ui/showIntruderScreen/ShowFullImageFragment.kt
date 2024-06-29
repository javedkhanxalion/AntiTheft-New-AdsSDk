package com.securityalarm.antitheifproject.ui.showIntruderScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentShowFullImageScreenBinding
import com.bmik.android.sdk.IkmSdkController
import com.bumptech.glide.Glide
import com.securityalarm.antitheifproject.model.IntruderModels
import com.securityalarm.antitheifproject.utilities.BaseFragment
import com.securityalarm.antitheifproject.utilities.firebaseAnalytics
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
import com.securityalarm.antitheifproject.utilities.showToast
import java.io.File

class ShowFullImageFragment :
    BaseFragment<FragmentShowFullImageScreenBinding>(FragmentShowFullImageScreenBinding::inflate) {

    var models: IntruderModels? = null
    var ratingDialogDelete: AlertDialog? = null
    private var isInternetDialog: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAnalytics("show_image_fragment_load", "show_image_fragment_load -->  Click")

        arguments?.let {
            models = it.getSerializable("obj") as IntruderModels
        }

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
        if (!isInternetAvailable(context ?: return)) {
            IkmSdkController.setEnableShowResumeAds(false)
        }
    }
    override fun onResume() {
        super.onResume()
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

}