package com.do_not_douch.antitheifproject.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.BuildConfig
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.BuildConfig.DEBUG
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.InAppDialogFirstBinding
import com.do_not_douch.antitheifproject.ads_manager.PurchasePrefs
import com.do_not_douch.antitheifproject.utilities.BaseFragment
import com.do_not_douch.antitheifproject.utilities.clickWithThrottle
import com.do_not_douch.antitheifproject.utilities.isSplash
import com.do_not_douch.antitheifproject.utilities.setupBackPressedCallback
import com.hypersoft.billing.BillingManager
import com.hypersoft.billing.dataClasses.ProductType
import com.hypersoft.billing.dataClasses.PurchaseDetail
import com.hypersoft.billing.interfaces.BillingListener
import com.hypersoft.billing.interfaces.OnPurchaseListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentInAppScreen :
    BaseFragment<InAppDialogFirstBinding>(InAppDialogFirstBinding::inflate),
    ActivityCompat.OnRequestPermissionsResultCallback {

    var isSplashFrom: Boolean? = true
    var billingManager: BillingManager? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isSplash = true
        arguments?.let {
            isSplashFrom = it.getBoolean("isSplash")
        }
        billingManager = BillingManager(context ?: return)
        val subsProductIdList =
            listOf("gold_product")
        val productInAppConsumable = when (DEBUG) {
            true -> listOf("inapp_product_id_1")
            false -> listOf("inapp_product_id_1", "inapp_product_id_1")
        }
        val inAppProductIdList = when (DEBUG) {
            true -> listOf(billingManager?.getDebugProductIDList())
            false -> listOf("inapp_product_id_1")
        }

        billingManager?.initialize(
            productInAppConsumable = productInAppConsumable,
            productInAppNonConsumable = inAppProductIdList as List<String>,
            productSubscriptions = subsProductIdList,
            billingListener = object : BillingListener {
                override fun onConnectionResult(isSuccess: Boolean, message: String) {
                    Log.d(
                        "BillingTAG",
                        "Billing: initBilling: onConnectionResult: isSuccess = $isSuccess - message = $message"
                    )
                }

                override fun purchasesResult(purchaseDetailList: List<PurchaseDetail>) {
                    if (purchaseDetailList.isEmpty()) {
                        // No purchase found, reset all sharedPreferences (premium properties)
                    }
                    purchaseDetailList.forEachIndexed { index, purchaseDetail ->
                        Log.d(
                            "BillingTAG",
                            "Billing: initBilling: purchasesResult: $index) $purchaseDetail "
                        )
                    }
                }
            }
        )
        billingManager?.productDetailsLiveData?.observe(viewLifecycleOwner) { productDetailList ->
            Log.d("in_app_TAG", "Billing: initObservers: $productDetailList")

            productDetailList.forEach { productDetail ->
                if (productDetail.productType == ProductType.inapp) {
                    // productDetail (monthly)
                    _binding?.premiumButton?.text =
                        "${getString(R.string.purchase)} : ${productDetail.price}"
                    productDetail.freeTrialDays = 3
                }

            }
        }

        setupBackPressedCallback {
            if (isSplashFrom == true) {
                findNavController().navigate(R.id.myMainMenuFragment, bundleOf("is_splash" to true))
            } else {
                findNavController().navigateUp()
            }
        }
        _binding?.premiumButton?.clickWithThrottle {
            billingManager?.makeInAppPurchase(
                activity = activity,
                productId = "inapp_product_id_1",
                object : OnPurchaseListener {
                    override fun onPurchaseResult(isPurchaseSuccess: Boolean, message: String) {
                        Log.d("in_app_TAG", "makeSubPurchase: $isPurchaseSuccess - $message")
                        if (isPurchaseSuccess) {
                            PurchasePrefs(context).putBoolean("inApp", true)
                        }
                    }
                })
        }
        _binding?.closeTop?.clickWithThrottle {
            if (isSplashFrom == true) {
                findNavController().navigate(R.id.myMainMenuFragment, bundleOf("is_splash" to true))
            } else {
                findNavController().navigateUp()
            }
        }
        _binding?.closeIcon?.clickWithThrottle {
            if (isSplashFrom == true) {
                findNavController().navigate(R.id.myMainMenuFragment, bundleOf("is_splash" to true))
            } else {
                findNavController().navigateUp()
            }
        }
        lifecycleScope.launch {
            delay(4000)
            _binding?.closeTop?.visibility = View.VISIBLE
            _binding?.closeIcon?.visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

}