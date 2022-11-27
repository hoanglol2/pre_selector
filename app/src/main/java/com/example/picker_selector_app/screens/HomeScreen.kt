package com.example.picker_selector_app.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.picker_selector_app.databinding.FragmentHomeScreenBinding
import com.example.picker_selector_app.models.ImagePickerConfig
import com.example.picker_selector_app.screens.common.BaseFragment
import com.example.picker_selector_app.screens.images.ImagePickerScreen

class HomeScreen : BaseFragment() {
    private var mBinding: FragmentHomeScreenBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeScreenBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        mBinding?.apply {
            btnOpenPicker.setOnClickListener(this@HomeScreen)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    private fun openImagePickerScreen() {
        ImagePickerScreen.openScreen(
            this@HomeScreen,
            config = ImagePickerConfig()
        )
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        mBinding?.apply {
            when (v) {
                btnOpenPicker -> openImagePickerScreen()
            }
        }
    }
}