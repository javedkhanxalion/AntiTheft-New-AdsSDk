package com.securityalarm.antitheifproject

import com.bmik.android.sdk.IkmSdkController
import com.bmik.android.sdk.SDKBaseApplication
import com.bmik.android.sdk.listener.keep.SDKIAPProductIDProvider
import com.securityalarm.antitheifproject.utilities.registerAppLifecycleCallbacks

class MyApplication : SDKBaseApplication() {
    override fun configIAPData(): SDKIAPProductIDProvider {
        return object : SDKIAPProductIDProvider {
            override val enableIAPFunction: Boolean
                get() = true

            /**
             * Retrieves a list of product IDs for subscription products.
             *
             * @return An ArrayList of Strings containing the product IDs for subscription products.
             */
            override fun listProductIDsSubscription(): ArrayList<String> {
                return arrayListOf()
            }

            /**
             * Retrieves a list of product IDs for one-time purchase products.
             *
             * @return An ArrayList of Strings containing the product IDs for one-time purchase products.
             */
            override fun listProductIDsPurchase(): ArrayList<String> {
                return arrayListOf()
            }

            /**
             * Retrieves a list of product IDs for products that remove advertisements after purchase.
             *
             * @return An ArrayList of Strings containing the product IDs for ad-removal products.
             */
            override fun listProductIDsRemoveAd(): ArrayList<String> {
                return arrayListOf()
            }

            /**
             * Retrieves a list of product IDs for products that can be purchased multiple times.
             *
             * @return An ArrayList of Strings containing the product IDs for multi-purchase products.
             */
            override fun listProductIDsCanPurchaseMultiTime(): ArrayList<String> {
                return arrayListOf()
            }

        }
    }

    override fun onCreate() {
        super.onCreate()
        IkmSdkController.addActivityEnableShowResumeAd(MainActivity::class.java)
        IkmSdkController.setEnableShowResumeAds(true)
        IkmSdkController.setEnableShowLoadingResumeAds(false)
        registerAppLifecycleCallbacks()
    }
}
