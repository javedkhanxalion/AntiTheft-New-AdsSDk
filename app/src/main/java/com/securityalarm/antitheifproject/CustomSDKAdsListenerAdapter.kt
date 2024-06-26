package com.securityalarm.antitheifproject

interface CustomSDKAdsListenerAdapter {
    fun onAdLoaded()
    fun onAdFailedToLoad(error: String)
}