package com.securityalarm.antitheifproject.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentLoadingBinding
import com.bmik.android.sdk.SDKBaseController
import com.bmik.android.sdk.listener.CommonAdsListenerAdapter
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.listener.SDKDialogLoadingCallback
import com.bmik.android.sdk.model.dto.CommonAdsAction
import com.bmik.android.sdk.model.dto.ScreenAds
import com.securityalarm.antitheifproject.SampleDialogLoading
import com.securityalarm.antitheifproject.helper_class.DbHelper
import com.securityalarm.antitheifproject.utilities.BaseFragment
import com.securityalarm.antitheifproject.utilities.IS_FIRST
import com.securityalarm.antitheifproject.utilities.IS_INTRO
import com.securityalarm.antitheifproject.utilities.LANG_SCREEN
import com.securityalarm.antitheifproject.utilities.firebaseAnalytics
import com.securityalarm.antitheifproject.utilities.isIntroLanguageShow
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback

class LoadingScreenFragment :
    BaseFragment<FragmentLoadingBinding>(FragmentLoadingBinding::inflate) {

    private var sharedPrefUtils: DbHelper? = null
    private var mOpenMainAction: (() -> Unit)? = null
    private var timerWaitAds: CountDownTimer? = null
    val dialogLoading = SampleDialogLoading.newInstance()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefUtils = DbHelper(context ?: return)
        firebaseAnalytics("loading_fragment_open", "loading_fragment_open -->  Click")
    /*    if (isFlowOne) {
            lifecycleScope.launchWhenCreated {
                delay(3000)
                loadNewNative()
                if (isAdded && isVisible && !isDetached) {
                    _binding?.next?.visibility = View.VISIBLE
                    _binding?.animationView?.visibility = View.INVISIBLE
                }
            }
        } else {
            _binding?.next?.visibility = View.INVISIBLE
            _binding?.nativeExitAd?.visibility = View.INVISIBLE
            _binding?.shimmerLayout?.visibility = View.INVISIBLE
            _binding?.animationView?.visibility = View.VISIBLE
            lifecycleScope.launchWhenCreated {
                delay(3000)
                getIntentMove()
            }

        }*/
        initView()
        SDKBaseController.getInstance()
            .loadInterstitialAds(
                activity,
                screenAds = ScreenAds.START.value,
                trackingScreen = ScreenAds.START.value
            )
        _binding?.next?.setOnClickListener {
            /*  adsManager?.let {
                  showNormalInterAdSingle(
                      it,
                      activity ?: return@let,
                      remoteConfigNormal = true,
                      tagClass = "splash"
                  ) {
                  }
              }*/
//            getIntentMove()
            SDKBaseController.getInstance().showInterstitialAds(
                activity ?: return@setOnClickListener,
                screen = ScreenAds.START.value,
                trackingScreen = ScreenAds.START.value,
                showLoading = true,
                loadingCallback = object : SDKDialogLoadingCallback {
                    override fun onClose() {
                        dialogLoading.closeDialog()
                    }

                    override fun onShow() {
                        dialogLoading.show(fragmentManager)
                    }
                },
                adsListener = object : CommonAdsListenerAdapter() {
                    override fun onAdsShowFail(errorCode: Int) {
                        getIntentMove()
                    }

                    override fun onAdsDismiss() {
                        getIntentMove()
                    }
                }
            )
        }
        setupBackPressedCallback {
        }
        loadBanner()
    }

    private fun getIntentMove() {
        if (sharedPrefUtils?.getBooleanData(
                requireContext(),
                IS_INTRO,
                false
            ) == false && isIntroLanguageShow
        ) {
            firebaseAnalytics(
                "loading_fragment_load_next_btn_intro",
                "loading_fragment_load_next_btn_intro -->  Click"
            )

            return findNavController().navigate(R.id.OnBordScreenNewScreen)
        } else if (sharedPrefUtils?.getBooleanData(
                requireContext(),
                IS_FIRST,
                false
            ) == false && isIntroLanguageShow
        ) {
            firebaseAnalytics(
                "loading_fragment_load_next_btn_language",
                "loading_fragment_load_next_btn_language -->  Click"
            )
            return findNavController().navigate(
                R.id.LanguageFragment,
                bundleOf(LANG_SCREEN to true)
            )
        } else {
            firebaseAnalytics(
                "loading_fragment_load_next_btn_main",
                "loading_fragment_load_next_btn_main -->  Click"
            )
//            return findNavController().navigate(
//                R.id.FragmentInAppScreen,
//                bundleOf("Is_From_Splash" to true)
//            )
            return   findNavController().navigate(
                R.id.myMainMenuFragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    private fun initView() {
        SDKBaseController.getInstance().onDataInitSuccessListener = CommonAdsAction {
            //do something
        }
        SDKBaseController.getInstance().onDataGetSuccessListener = {
            //do something
        }
        mOpenMainAction = {
            timerWaitAds?.cancel()
            moveToMain()
        }
        timerWaitAds =
            SDKBaseController.getInstance().showFirstOpenAppAds(activity ?: return,
                object : CommonAdsListenerAdapter() {
                    override fun onAdsDismiss() {
                    }

                    override fun onAdsShowFail(errorCode: Int) {
                    }

                    override fun onAdsShowTimeout() {
                        super.onAdsShowTimeout()
                        Toast.makeText(context, "Ads show timeout", Toast.LENGTH_SHORT)
                            .show()
                    }

                })

    }

    private fun moveToMain() {
        SDKBaseController.getInstance()
            .loadInterstitialAds(
                activity ?: return,
                screenAds = ScreenAds.START.value,
                trackingScreen = ScreenAds.START.value
            )
        mOpenMainAction?.invoke()
        mOpenMainAction = null

    }

    fun loadBanner() {
        _binding?.adsView?.loadAd(
            activity, "bn_s_adw",
            "bn_s_adw", object : CustomSDKAdsListenerAdapter() {

                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.adsView?.visibility = View.GONE
                }
            }
        )


        /*  SDKBaseController.getInstance().onDataInitSuccessListener = CommonAdsAction {
              //do something
          }
          Log.d("remote_value", "loadNewNative: ${SDKBaseController.getInstance().remoteConfigData}")
          val adLayout = LayoutInflater.from(context).inflate(
              R.layout.layout_custom_ad_native,
              null, false
          ) as? IkmWidgetAdLayout
          adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
          adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
          adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
          adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
          adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
          _binding?.mainAdsNative?.loadAd(
              activity ?: return,  R.layout.shimmer_loding_native,
              adLayout!!, "home_native",
              "home_native", object : CustomSDKAdsListenerAdapter() {

                  override fun onAdsLoadFail() {
                      super.onAdsLoadFail()
                      _binding?.mainAdsNative?.visibility = View.GONE
                  }
              }
          )*/
    }
}