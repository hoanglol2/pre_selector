package com.example.picker_selector_app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.picker_selector_app.databinding.FragmentImagePickerScreenBinding
import com.example.picker_selector_app.models.ImagePickerConfig
import com.example.picker_selector_app.viewmodels.ImagePickerViewModel
import timber.log.Timber

class ImagePickerScreen : Fragment() {
    private var mBinding: FragmentImagePickerScreenBinding? = null
    private lateinit var mSafeContext: Context
    private val mViewModelImagePicker: ImagePickerViewModel by viewModels {
        ImagePickerViewModel.factory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mSafeContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentImagePickerScreenBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        fetchDataWithPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> fetchDataWithPermission()
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun initObserver() {
        mViewModelImagePicker.imageResultLiveData.observe(viewLifecycleOwner) {
            Timber.d("Data Image External: $it")
        }
    }

    private fun fetchDataWithPermission() {
        if (!isAllPermissionGranted(mSafeContext)) {
            ActivityCompat.requestPermissions(
                activity ?: return,
                PERMISSIONS_REQUIRED,
                PERMISSIONS_REQUEST_CODE
            )
        } else {
            fetchData()
        }
    }

    private fun fetchData() {
        mViewModelImagePicker.fetchImages()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    companion object {
        private const val IMAGE_PICKER_CONFIG_ARG = "IMAGE_PICKER_CONFIG_ARG"
        private const val PERMISSIONS_REQUEST_CODE = 10
        private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        fun openScreen(
            fragment: BaseFragment,
            config: ImagePickerConfig
        ) {
            fragment.findNavController().navigate(
                R.id.imagePickerScreen,
                bundleOf(IMAGE_PICKER_CONFIG_ARG to config)
            )
        }

        fun isAllPermissionGranted(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}