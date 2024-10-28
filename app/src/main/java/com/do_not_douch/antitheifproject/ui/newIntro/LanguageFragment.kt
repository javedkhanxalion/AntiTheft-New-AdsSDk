package com.do_not_douch.antitheifproject.ui.newIntro

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.FragmentLanguageBinding
import com.do_not_douch.antitheifproject.adapter.LanguageGridAdapter
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.interfaces.NativeListener
import com.do_not_douch.antitheifproject.helper_class.DbHelper
import com.do_not_douch.antitheifproject.model.LanguageAppModel
import com.do_not_douch.antitheifproject.utilities.BaseFragment
import com.do_not_douch.antitheifproject.utilities.IS_FIRST
import com.do_not_douch.antitheifproject.utilities.LANG_CODE
import com.do_not_douch.antitheifproject.utilities.LANG_SCREEN
import com.do_not_douch.antitheifproject.utilities.clickWithThrottle
import com.do_not_douch.antitheifproject.utilities.firebaseAnalytics
import com.do_not_douch.antitheifproject.utilities.getNativeLayout
import com.do_not_douch.antitheifproject.utilities.id_native_screen
import com.do_not_douch.antitheifproject.utilities.language_bottom
import com.do_not_douch.antitheifproject.utilities.restartApp
import com.do_not_douch.antitheifproject.utilities.setLocaleMain
import com.do_not_douch.antitheifproject.utilities.setupBackPressedCallback
import com.do_not_douch.antitheifproject.utilities.val_ad_native_language_screen
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView


class LanguageFragment : BaseFragment<FragmentLanguageBinding>(FragmentLanguageBinding::inflate) {
    private var positionSelected: String = "en"
    private var isLangScreen: Boolean = false
    private var isValue: Int = 0
    private var sharedPrefUtils: DbHelper? = null
    private var adapter: LanguageGridAdapter? = null
    var list: ArrayList<LanguageAppModel> = ArrayList()
    private var adsManager: AdsManager? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            firebaseAnalytics("language_fragment_open", "language_fragment_open -->  Click")
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            initializeData()
            adsManager = AdsManager.appAdsInit(activity ?: return)
            arguments?.let {
                isLangScreen = it.getBoolean(LANG_SCREEN)
            }


            loadNative()
            sharedPrefUtils = DbHelper(context ?: return)
            positionSelected =
                sharedPrefUtils?.getStringData(requireContext(), LANG_CODE, "en") ?: "en"
            adapter = LanguageGridAdapter(list, adsManager ?: return, activity ?: return,
                clickItem = {
                    positionSelected = it.country_code
                    adapter?.selectLanguage(positionSelected)
                    _binding?.forwardBtn?.setBackgroundColor(Color.parseColor("#44A0E3"))
                    _binding?.forwardBtn?.clickWithThrottle {
                        if (!isLangScreen) {
                            firebaseAnalytics(
                                "language_fragment_forward_btn_from",
                                "language_fragment_forward_btn_from -->  Click"
                            )
                            sharedPrefUtils?.saveData(requireContext(), LANG_CODE, positionSelected)
                                ?: "en"
                            setLocaleMain(positionSelected)
                            restartApp()
                        } else {
                            /*              firebaseAnalytics(
                                              "language_fragment_forward_btn_from",
                                              "language_fragment_forward_btn_from -->  Click"
                                          )
                                          sharedPrefUtils?.saveData(requireContext(), LANG_CODE, positionSelected) ?: "en"
                                          setLocaleMain(positionSelected)
                                          adsManager?.let { it1 ->
                                              showTwoInterAd(
                                                  ads = it1,
                                                  activity = activity ?: return@let,
                                                  remoteConfigNormal = val_inter_language_screen,
                                                  adIdNormal = id_inter_main_medium,
                                                  tagClass = "language",
                                                  layout = _binding?.adsLayDialog!!,
                                                  isBackPress = true,
                                                  function = {
                                                  }
                                              )
                                              when (sessionOnboarding) {
                                                  0 -> {
                                                      firebaseAnalytics(
                                                          "loading_fragment_load_next_btn_main",
                                                          "loading_fragment_load_next_btn_main -->  Click"
                                                      )

                                                      if (PurchasePrefs(context).getBoolean("inApp") || !val_is_inapp_splash) {
                                                          findNavController().navigate(
                                                              R.id.myMainMenuFragment,
                                                              bundleOf("is_splash" to true)
                                                          )
                                                      } else {
                                                          findNavController().navigate(
                                                              R.id.FragmentBuyScreen,
                                                              bundleOf("isSplash" to true)
                                                          )
                                                      }
                                                  }

                                                  1 -> {
                                                      findNavController().navigate(R.id.OnBordScreenNewScreen)

                                                  }

                                                  2 -> {
                                                      firebaseAnalytics(
                                                          "loading_fragment_load_next_btn_intro",
                                                          "loading_fragment_load_next_btn_intro -->  Click"
                                                      )
                                                      findNavController().navigate(R.id.OnBordScreenNewScreen)
                                                  }
                                              }
                                          }*/
                            firebaseAnalytics(
                                "language_fragment_forward_btn_from",
                                "language_fragment_forward_btn_from -->  Click"
                            )
                            sharedPrefUtils?.saveData(requireContext(), LANG_CODE, positionSelected)
                                ?: "en"
                            setLocaleMain(positionSelected)
                            sharedPrefUtils?.saveData(requireContext(), IS_FIRST, true)
                            findNavController().navigate(
                                R.id.OnBordScreenNewScreen
                            )
                        }
                    }
                }
            )
//            adapter?.selectLanguage(positionSelected)
//            _binding?.conversationDetail?.layoutManager = GridLayoutManager(context, 2).apply {
//                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//                    override fun getSpanSize(position: Int): Int {
//                        return if (adapter?.getItemViewType(position) == AD_TYPE) 2 else 1
//                    }
//                }
//            }
            _binding?.conversationDetail?.adapter = adapter
            _binding?.backBtn?.clickWithThrottle {
                if (!isLangScreen) {
                    firebaseAnalytics(
                        "language_fragment_back_press",
                        "language_fragment_back_press -->  Click"
                    )
                    findNavController().popBackStack()
                } else {
                    firebaseAnalytics(
                        "language_fragment_forward_btn_from",
                        "language_fragment_forward_btn_from -->  Click"
                    )
                    sharedPrefUtils?.saveData(requireContext(), LANG_CODE, positionSelected) ?: "en"
                    setLocaleMain(positionSelected)
                    sharedPrefUtils?.saveData(requireContext(), IS_FIRST, true)
                    findNavController().navigate(
                        R.id.OnBordScreenNewScreen
                    )
                }
            }
            setupBackPressedCallback {
                if (!isLangScreen) {
                    firebaseAnalytics(
                        "language_fragment_back_press",
                        "language_fragment_back_press -->  Click"
                    )
                    findNavController().popBackStack()
                } else {
                    firebaseAnalytics(
                        "language_fragment_forward_btn_from",
                        "language_fragment_forward_btn_from -->  Click"
                    )
                    sharedPrefUtils?.saveData(requireContext(), LANG_CODE, positionSelected) ?: "en"
                    setLocaleMain(positionSelected)
                    sharedPrefUtils?.saveData(requireContext(), IS_FIRST, true)
                    findNavController().navigate(
                        R.id.OnBordScreenNewScreen
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadNative() {
        _binding?.mainAdsNative?.visibility = View.INVISIBLE
        _binding?.shimmerLayout?.visibility = View.VISIBLE
        _binding?.adsView?.visibility = View.GONE
        val adView = LayoutInflater.from(context).inflate(
            getNativeLayout(language_bottom, _binding?.mainAdsNative!!, context ?: return),
            null, false
        ) as NativeAdView
        adsManager?.nativeAdsMain()?.loadNativeAd(
            activity ?: return,
            val_ad_native_language_screen,
            id_native_screen,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    _binding?.mainAdsNative?.visibility = View.VISIBLE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    adsManager?.nativeAdsMain()?.nativeViewMediaSplashSplash(
                        context ?: return,
                        currentNativeAd ?: return,
                        adView
                    )
                    _binding?.mainAdsNative?.removeAllViews()
                    _binding?.mainAdsNative?.addView(adView)
                    super.nativeAdLoaded(currentNativeAd)
                }

                override fun nativeAdFailed(loadAdError: LoadAdError) {
                    _binding?.mainAdsNative?.visibility = View.GONE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdFailed(loadAdError)
                }

                override fun nativeAdValidate(string: String) {
                    _binding?.mainAdsNative?.visibility = View.GONE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdValidate(string)
                }
            })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    private fun initializeData() {
        list.add(LanguageAppModel(getString(R.string.english), "en", R.drawable.usa, false))
        list.add(LanguageAppModel(getString(R.string.arabic), "ar", R.drawable.sudi, false))
        list.add(LanguageAppModel(getString(R.string.chinese), "zh", R.drawable.chinese, false))
        list.add(LanguageAppModel(getString(R.string.dutch), "nl", R.drawable.dutch, false))
        list.add(LanguageAppModel(getString(R.string.french), "fr", R.drawable.france, false))
        list.add(LanguageAppModel(getString(R.string.german), "de", R.drawable.germany, false))
        list.add(LanguageAppModel(getString(R.string.hindi), "hi", R.drawable.india, false))
        list.add(LanguageAppModel(getString(R.string.italian), "it", R.drawable.italian, false))
        list.add( LanguageAppModel(  getString(R.string.indonesian),  "id",  R.drawable.indonesian,  false))
        list.add(LanguageAppModel(getString(R.string.japanese), "ja", R.drawable.japan, false))
        list.add(LanguageAppModel(getString(R.string.korean), "ko", R.drawable.japanese, false))
        list.add(LanguageAppModel( getString(R.string.portuguese),"pt",R.drawable.portuguese,false))
        list.add(LanguageAppModel(getString(R.string.russian), "ru", R.drawable.russian, false))
        list.add(LanguageAppModel(getString(R.string.spanish), "es", R.drawable.spain, false))
        list.add(LanguageAppModel(getString(R.string.turkey), "tr", R.drawable.turkey, false))
        list.add(LanguageAppModel(getString(R.string.thai), "th", R.drawable.ukrainian, false))
        list.add(LanguageAppModel(getString(R.string.urdu), "ur", R.drawable.urdu, false))
        list.add( LanguageAppModel( getString(R.string.vietnamese),"vi", R.drawable.vietnamese,false ))
    }

}