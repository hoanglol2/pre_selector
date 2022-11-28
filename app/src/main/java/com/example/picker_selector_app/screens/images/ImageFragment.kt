package com.example.picker_selector_app.screens.images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.picker_selector_app.R
import com.example.picker_selector_app.adapters.ImagePickerAdapter
import com.example.picker_selector_app.databinding.FragmentImagePickerScreenBinding
import com.example.picker_selector_app.models.GridCount
import com.example.picker_selector_app.screens.images.ImagePickerScreen.Companion.BUCKET_ID
import com.example.picker_selector_app.screens.images.ImagePickerScreen.Companion.GRID_COUNT
import com.example.picker_selector_app.viewmodels.ImagePickerViewModel

class ImageFragment : Fragment() {

    private var binding: FragmentImagePickerScreenBinding? = null
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImagePickerScreenBinding.inflate(inflater)
        val config = imagePickerShareVM.getConfig()
        imagePickerAdapter.setConfig(config)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}