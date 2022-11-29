package com.example.picker_selector_app.screens.images

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.navGraphViewModels
import com.example.picker_selector_app.R
import com.example.picker_selector_app.adapters.ImageFolderAdapter
import com.example.picker_selector_app.consts.CallbackStatus
import com.example.picker_selector_app.models.GridCount
import com.example.picker_selector_app.models.Result
import com.example.picker_selector_app.screens.images.ImagePickerScreen.Companion.GRID_COUNT
import com.example.picker_selector_app.utils.helper.ImageHelper
import com.example.picker_selector_app.viewmodels.ImagePickerViewModel

class FolderFragment : BaseImageFragment() {

    private val folderAdapter by lazy { ImageFolderAdapter() }
    private val imagePickerShareVM: ImagePickerViewModel by navGraphViewModels(R.id.nav_graph_image) {
        ImagePickerViewModel.factory()
    }

    companion object {
        @JvmStatic
        fun newInstance(gridCount: GridCount): FolderFragment {
            val fragment = FolderFragment()
            val args = Bundle()
            args.putParcelable(GRID_COUNT, gridCount)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupRecyclerview()
        initObserver()
        initListener()
    }

    private fun initViews() {
        val config = imagePickerShareVM.getConfig()
        setupViewsWithConfig(config)
    }

    override fun setupRecyclerview() {
        super.setupRecyclerview()
        binding?.rvImage?.adapter = folderAdapter
    }

    private fun initObserver() {
        imagePickerShareVM.imageResultLiveData.observe(viewLifecycleOwner) {
            handleResult(it)
        }
    }

    override fun handleResult(result: Result) {
        binding?.apply {
            val folders = ImageHelper.folderListFromImages(result.images)
            val isSuccess = result.status is CallbackStatus.SUCCESS && folders.isNotEmpty()
            if (isSuccess) folderAdapter.setList(folders)
            rvImage.isVisible = isSuccess
            tvEmptyText.isVisible = result.status is CallbackStatus.SUCCESS && folders.isEmpty()
            prgIndicator.isVisible = result.status is CallbackStatus.FETCHING
        }
    }

    private fun initListener() {
        folderAdapter.setOnItemClickListener {
            imagePickerShareVM.folderLiveEvent.value = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}