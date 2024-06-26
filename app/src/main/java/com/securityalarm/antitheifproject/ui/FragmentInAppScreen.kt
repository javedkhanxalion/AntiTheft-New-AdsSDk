package com.securityalarm.antitheifproject.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.InAppDialogFirstBinding
import com.bmik.android.sdk.SDKBaseController
import com.bmik.android.sdk.billing.BillingHelper
import com.bmik.android.sdk.listener.SDKBillingPurchaseListener
import com.bmik.android.sdk.listener.SDKBillingValueListener
import com.bmik.android.sdk.tracking.SDKTrackingController
import com.bmik.android.sdk.utils.IkmSdkUtils
import com.securityalarm.antitheifproject.utilities.BaseFragment
import com.securityalarm.antitheifproject.utilities.clickWithThrottle
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class FragmentInAppScreen :
    BaseFragment<InAppDialogFirstBinding>(InAppDialogFirstBinding::inflate),
    ActivityCompat.OnRequestPermissionsResultCallback {

    var planId: String? = null
    var isSplashFrom: Boolean? = true
    private var mDayUseApp = 1L
    private var mIsBillingIabServiceAvailable = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            isSplashFrom = it.getBoolean("Is_From_Splash")
        }
        val billingHelper = BillingHelper.getInstance()
        updateUI(
            binding?.yearlyCheck!!,
            binding?.yearlyButton!!,
            binding?.monthlyCheck!!,
            binding?.monthlyButton!!
        )
        initView()
        planId = "gold-plan-yearly"
        /*        billingManager = BillingManager(context ?: return)
               val subsProductIdList =
                   listOf("gold_product")
               val inAppProductIdList = when (BuildConfig.DEBUG) {
                   true -> listOf(billingManager?.getDebugProductIDList())
                   false -> listOf("inapp_product_id_1", "inapp_product_id_2")
               }
               billingManager?.initialize(
                   productInAppPurchases = inAppProductIdList as List<String>,
                   productSubscriptions = subsProductIdList,
                   billingListener = object : BillingListener {
                       override fun onConnectionResult(isSuccess: Boolean, message: String) {
                           Log.d(
                               "in_app_TAG",
                               "Billing: initBilling: onConnectionResult: isSuccess = $isSuccess - message = $message"
                           )
                           if (!isSuccess) {
       //                        proceedApp()
                           }
                       }

                       override fun purchasesResult(purchaseDetailList: List<PurchaseDetail>) {
                           if (purchaseDetailList.isEmpty()) {
                               // No purchase found, reset all sharedPreferences (premium properties)
                           }
                           purchaseDetailList.forEachIndexed { index, purchaseDetail ->
                               Log.d(
                                   "in_app_TAG",
                                   "Billing: initBilling: purchasesResult: $index) $purchaseDetail "
                               )
                           }
       //                    proceedApp()
                       }
                   }
               )
               billingManager?.productDetailsLiveData?.observe(viewLifecycleOwner) { productDetailList ->
                   Log.d("in_app_TAG", "Billing: initObservers: $productDetailList")

                   productDetailList.forEach { productDetail ->
                       if (productDetail.productType == ProductType.subs) {
                           if (productDetail.productId == "gold_product" && productDetail.planId == "gold-plan-monthly") {
                               // productDetail (monthly)
                               _binding?.monthlyText?.text = "${productDetail.price}"
                               var originalString = getString(R.string.month_free_trail_second)
                               originalString =originalString.replace("3.0", productDetail.price)
                               _binding?.monthlyTextTDetail?.text = originalString
                               productDetail.freeTrialDays = 3
                           } else if (productDetail.productId == "subs_product_id_2" && productDetail.planId == "subs_plan_id_2") {
                               // productDetail (3 months)
                           } else if (productDetail.productId == "gold_product" && productDetail.planId == "gold-plan-yearly") {
                               // productDetail (yearly)
                               _binding?.yearlyText?.text = "${productDetail.price}"
                               var originalString = getString(R.string.annual_free_trail_second)
                               originalString =originalString.replace("12.0", productDetail.price)
                               _binding?.yearlyTextTDetail?.text = originalString
                               productDetail.freeTrialDays = 3
                           }
                       }

                   }
               }*/

        setupBackPressedCallback {
            moveClose()
        }

        _binding?.premiumButton?.clickWithThrottle {
         /*   billingManager?.makeSubPurchase(
                activity,
                "gold_product",
                planId ?: return@clickWithThrottle,
                object : OnPurchaseListener {
                    override fun onPurchaseResult(isPurchaseSuccess: Boolean, message: String) {
                        Log.d("in_app_TAG", "makeSubPurchase: $isPurchaseSuccess - $message")
                        if (isPurchaseSuccess) {
                            PurchasePrefs(context).putBoolean("inApp", true)
                        }
                    }
                })*/
            startPay(billingHelper)
        }
        _binding?.closeIcon?.clickWithThrottle {
            moveClose()
        }
        _binding?.closeTop?.clickWithThrottle {
            moveClose()
        }
        _binding?.monthlyButton?.clickWithThrottle {
            planId = "gold-plan-monthly"
            updateUI(
                binding?.monthlyCheck!!,
                binding?.monthlyButton!!,
                binding?.yearlyCheck!!,
                binding?.yearlyButton!!
            )
        }
        _binding?.yearlyButton?.clickWithThrottle {
            planId = "gold-plan-yearly"
            updateUI(
                binding?.yearlyCheck!!,
                binding?.yearlyButton!!,
                binding?.monthlyCheck!!,
                binding?.monthlyButton!!
            )
        }

    }


    val TAG = "PremiumActivity"
    val TAG_ONE_MONTH = "OneMonth"
    val TAG_ONE_YEAR = "OneYear"

    private fun startPay(billingHelper: BillingHelper) {
        when (planId) {
            "gold-plan-monthly" -> {
                SDKTrackingController.logScreenAction(
                    context?.applicationContext,
                    TAG,
                    TAG,
                    TAG_ONE_MONTH,
                    "click"
                )
                if (checkService())
                    return

                billingHelper.purchase(activity,"gold-plan-monthly", object :
                    SDKBillingPurchaseListener {
                    override fun onProductIsBilling(productId: String) {
                        showDialogSuccess("IsBilling")
                    }

                    override fun onBillingFail(productId: String, errorCode: Int) {
                        showDialogError("error")
                    }
                    override fun onBillingSuccess(productId: String) {
                        showDialogSuccess("onBillingSuccess")
                    }

                })
            }
            "gold-plan-yearly" -> {
                SDKTrackingController.logScreenAction(
                    context?.applicationContext,
                    TAG,
                    TAG,
                    TAG_ONE_YEAR,
                    "click"
                )
                if (checkService())
                    return

                billingHelper.purchase(activity,"gold-plan-yearly", object :
                    SDKBillingPurchaseListener {
                    override fun onProductIsBilling(productId: String) {
                        showDialogSuccess("IsBilling")
                    }

                    override fun onBillingFail(productId: String, errorCode: Int) {
                        showDialogError("error")
                    }
                    override fun onBillingSuccess(productId: String) {
                        showDialogSuccess("onBillingSuccess")
                    }

                })
            }else->{
            SDKTrackingController.logScreenAction(
                context?.applicationContext,
                TAG,
                TAG,
                TAG_ONE_MONTH,
                "click"
            )
            if (checkService())
                return

            billingHelper.purchase(activity,"gold-plan-yearly", object :
                SDKBillingPurchaseListener {
                override fun onProductIsBilling(productId: String) {
                    showDialogSuccess("IsBilling")
                }

                override fun onBillingFail(productId: String, errorCode: Int) {
                    showDialogError("error")
                }
                override fun onBillingSuccess(productId: String) {
                    showDialogSuccess("onBillingSuccess")
                }

            })

            }
        }
    }

    private fun checkService(): Boolean {
        if (!mIsBillingIabServiceAvailable) {
            showDialogError(
                R.string.no_internet
            )
            return true
        }
        return false
    }
    fun showDialogSuccess(message: String) {
        try {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
        }
    }

    fun moveClose() {
        if (isSplashFrom == true) {
            findNavController().navigate(R.id.myMainMenuFragment)
        } else {
            findNavController().navigateUp()
        }
    }

    fun updateUI(image: View, layout: View, image1: View, layout1: View) {
        image.setBackgroundResource(R.drawable.in_app_ss)
        layout.setBackgroundResource(R.drawable.premium_button_selected)
        image1.setBackgroundResource(R.drawable.in_app_un)
        layout1.setBackgroundResource(R.drawable.premium_button_unselected)
    }

    private fun initView() {
        planId = SDKBaseController.getInstance().mOtherConfig["sale_percent"].toString()
        val billingHelper = BillingHelper.getInstance()
        if (!billingHelper.isIabServiceAvailable(context)) {
            showDialogError(
                R.string.error_message_text
            )
            mIsBillingIabServiceAvailable = false
        }
        lifecycleScope.launch(Dispatchers.Default) {
            try {
                val time = requireContext().packageManager.getPackageInfo(
                    context?.packageName ?: return@launch, 0
                ).firstInstallTime
                mDayUseApp = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - time)
            } catch (e: Exception) {

            }
        }
        BillingHelper.getInstance().initBilling(activity?.applicationContext ?: return)
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                if (BillingHelper.getInstance().isConnected() == false)
                    delay(500)
                if (BillingHelper.getInstance().isConnected() == false)
                    delay(500)
            }
            getAmountSubscribe()
        }
        if (!IkmSdkUtils.isConnectionAvailable()) {
            showDialogError(R.string.no_internet)
        }
    }
    private fun getAmountSubscribe() {
        val billingHelper = BillingHelper.getInstance()
        billingHelper.getPricePurchase("gold-plan-monthly", 0, object :
            SDKBillingValueListener {
            override fun onResult(price: String, salePrice: String) {
                price.let {
                    _binding?.monthlyTextTDetail?.text = it
                }
            }

        })
        billingHelper.getPricePurchase("gold-plan-yearly", 0, object :
            SDKBillingValueListener {
            override fun onResult(price: String, salePrice: String) {
                price.let {
                    _binding?.yearlyTextTDetail?.text = it
                }
            }

        })
    }
    private fun showDialogError(@StringRes message: Int) {
        try {
            Toast.makeText(context?:return, message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
        }
    }
    fun showDialogError(message: String) {
        try {
            Toast.makeText(context?:return, message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
        }
    }

}