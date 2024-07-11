//package com.securityalarm.antitheifproject.ads_manager
//
//import com.unity3d.ads.IUnityAdsInitializationListener
//import com.unity3d.ads.IUnityAdsLoadListener
//import com.unity3d.ads.IUnityAdsShowListener
//import com.unity3d.ads.UnityAds
//
//private fun loadUnityInterstitial() {
//    UnityAds.initialize(this, "YOUR_UNITY_GAME_ID", object : IUnityAdsInitializationListener {
//        override fun onInitializationComplete() {
//            UnityAds.load("YOUR_UNITY_PLACEMENT_ID", object : IUnityAdsLoadListener {
//                override fun onUnityAdsAdLoaded(placementId: String) {
//                    // Show the ad when it's loaded
//                    UnityAds.show(this@MainActivity, "YOUR_UNITY_PLACEMENT_ID", object : IUnityAdsShowListener {
//                        override fun onUnityAdsShowFailure(placementId: String, error: UnityAds.UnityAdsShowError, message: String) {
//                            // Ad show error callback
//                        }
//
//                        override fun onUnityAdsShowStart(placementId: String) {
//                            // Ad show start callback
//                        }
//
//                        override fun onUnityAdsShowClick(placementId: String) {
//                            // Ad click callback
//                        }
//
//                        override fun onUnityAdsShowComplete(placementId: String, state: UnityAds.UnityAdsShowCompletionState) {
//                            // Ad show complete callback
//                        }
//                    })
//                }
//
//                override fun onUnityAdsFailedToLoad(placementId: String, error: UnityAds.UnityAdsLoadError, message: String) {
//                    // Ad load error callback
//                }
//            })
//        }
//
//        override fun onInitializationFailed(error: UnityAds.UnityAdsInitializationError, message: String) {
//            // Initialization error callback
//        }
//    })
//}
