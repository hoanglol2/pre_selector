package com.example.picker_selector_app.screens.images

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.navGraphViewModels
import com.example.picker_selector_app.R
import com.example.picker_selector_app.adapters.ImagePickerAdapter
import com.example.picker_selector_app.consts.CallbackStatus
import com.example.picker_selector_app.models.GridCount
import com.example.picker_selector_app.models.Result
import com.example.picker_selector_app.screens.images.ImagePickerScreen.Companion.BUCKET_ID
import com.example.picker_selector_app.screens.images.ImagePickerScreen.Companion.GRID_COUNT
import com.example.picker_selector_app.utils.helper.ImageHelper
import com.example.picker_selector_app.viewmodels.ImagePickerViewModel

class ImageFragment : BaseImageFragment() {

    private var bucketId: Long? = null
    private val imagePickerAdapter by lazy { ImagePickerAdapter() }
    private val imagePickerShareVM: ImagePickerViewModel by navGraphViewModels(R.id.nav_graph_image) {
        ImagePickerViewModel.factory()
    }

    companion object {
        @JvmStatic
        fun newInstance(
            bucketId: Long,
            gridCount: GridCount
        ) = ImageFragment().apply {
            arguments = bundleOf(
                BUCKET_ID to bucketId,
                GRID_COUNT to gridCount
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bucketId = arguments?.getLong(BUCKET_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
    }

    private fun initViews() {
        val config = imagePickerShareVM.getConfig()
        imagePickerAdapter.setConfig(config)
        setupViewsWithConfig(config)
    }

    override fun setupRecyclerview() {
        super.setupRecyclerview()
        binding?.rvImage?.adapter = imagePickerAdapter
    }

    private fun initObserver() {
        imagePickerShareVM.selectedImages.observe(viewLifecycleOwner) {
            imagePickerAdapter.setListSelectedImage(it)
        }
        imagePickerShareVM.imageResultLiveData.observe(viewLifecycleOwner) {
            handleResult(it)
        }
    }

    override fun handleResult(result: Result) {
        binding?.apply {
            val images = ImageHelper.filterImages(result.images, bucketId)
            val isSuccess = result.status is CallbackStatus.SUCCESS && images.isNotEmpty()
            if (isSuccess) imagePickerAdapter.setList(images)
            rvImage.isVisible = isSuccess
            tvEmptyText.isVisible = result.status is CallbackStatus.SUCCESS && images.isEmpty()
            prgIndicator.isVisible = result.status is CallbackStatus.FETCHING
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}