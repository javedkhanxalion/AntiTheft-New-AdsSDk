package com.securityalarm.antitheifproject.ui.newIntro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentAdBinding
import com.bmik.android.sdk.SDKBaseController
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.widgets.IkmWidgetAdLayout

class AdFragment : Fragment() {

    private var _binding: FragmentAdBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val constraintLayout = view.findViewById<ConstraintLayout>(R.id.mainLayout)

        // Create a new View
        val myView = View(context).apply {
            id = View.generateViewId()
            setBackgroundColor(resources.getColor(android.R.color.white, null))
        }

        // Add the view to the ConstraintLayout
        constraintLayout.addView(myView, 0)

        // Apply constraints to the view
        val set = ConstraintSet().apply {
            clone(constraintLayout)
            connect(myView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(myView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(myView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(myView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            constrainWidth(myView.id, ConstraintSet.MATCH_CONSTRAINT)
            constrainHeight(myView.id, ConstraintSet.MATCH_CONSTRAINT)
        }

        set.applyTo(constraintLayout)
        loadNative()
        SDKBaseController.getInstance().preloadNativeAd(
            activity ?: return, "onboarding3_bottom",
            "onboarding3_bottom"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadNative() {
        val adLayout = LayoutInflater.from(context).inflate(
            R.layout.native_layout_full,
            null, false
        ) as? IkmWidgetAdLayout
        adLayout?.titleView = adLayout?.findViewById(R.id.headline)
        adLayout?.bodyView = adLayout?.findViewById(R.id.body)
        adLayout?.callToActionView = adLayout?.findViewById(R.id.call_to_action)
        adLayout?.mediaView = adLayout?.findViewById(R.id.media)
        _binding?.mainAdsNative?.loadAd(
            activity ?: return,  R.layout.shimmer_loding_native,
            adLayout!!, "onboarding_fullnative",
            "onboarding_fullnative", object : CustomSDKAdsListenerAdapter() {

                override fun onAdsLoaded() {
                    super.onAdsLoaded()
                    _binding?.mainAdsNative?.visibility = View.VISIBLE
                    _binding?.progressBar?.visibility = View.INVISIBLE
                }

                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.mainAdsNative?.visibility = View.GONE
                    _binding?.mainAdsNative?.visibility = View.GONE
                }
            }
        )
    }

}
