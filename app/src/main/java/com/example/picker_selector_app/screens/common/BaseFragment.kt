package com.example.picker_selector_app.screens.common

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment: Fragment(), View.OnClickListener {

    lateinit var safeContext: Context
    override fun onClick(v: View?) = Unit

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    open fun onBackPressed() {
        activity?.onBackPressed()
    }
}