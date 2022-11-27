package com.example.picker_selector_app.screens.images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.picker_selector_app.R
import com.example.picker_selector_app.common.PermissionManager
import com.example.picker_selector_app.consts.EXTRA_CONFIG
import com.example.picker_selector_app.consts.Permission
import com.example.picker_selector_app.databinding.FragmentImagePickerScreenBinding
import com.example.picker_selector_app.extensions.error
import com.example.picker_selector_app.extensions.success
import com.example.picker_selector_app.models.Folder
import com.example.picker_selector_app.models.ImagePickerConfig
import com.example.picker_selector_app.screens.common.BaseFragment
import com.example.picker_selector_app.viewmodels.ImagePickerViewModel
import timber.log.Timber

class ImagePickerScreen : BaseFragment() {
    private var binding: FragmentImagePickerScreenBinding? = null
    private val permissionManager = PermissionManager.from(this)
    private lateinit var config: ImagePickerConfig
    private val imagePickerShareVM: ImagePickerViewModel by navGraphViewModels(R.id.nav_graph_image) {
        ImagePickerViewModel.factory()
    }

    companion object {
        private const val IMAGE_PICKER_CONFIG_ARG = "IMAGE_PICKER_CONFIG_ARG"
        const val BUCKET_ID = "BucketId"
        const val GRID_COUNT = "GridCount"

        fun openScreen(
            fragment: BaseFragment,
            config: ImagePickerConfig
        ) {
            fragment.findNavController().navigate(
                R.id.toNavGraphImage,
                bundleOf(IMAGE_PICKER_CONFIG_ARG to config)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        config = arguments?.getParcelable(EXTRA_CONFIG) ?: ImagePickerConfig()
        config.initDefaultValues(safeContext)
        imagePickerShareVM.setConfig(config)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImagePickerScreenBinding.inflate(inflater)
        fetchDataWithPermission()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
        initListener()
    }

    private fun initListener() {
        binding?.apply {
            toolbarImagePicker.apply {
                setOnBackClickListener {
                    onBackPressed()
                }
                setOnCameraClickListener {
                    captureImageWithPermission()
                }
                setOnDoneClickListener {
                    onDone()
                }
            }
        }
    }

    private fun initViews() {
        binding?.apply {
            val initialFragment =
                if (config.isFolderMode) FolderFragment.newInstance(config.folderGridCount)
                else FolderFragment.newInstance(config.folderGridCount)
            toolbarImagePicker.config(config)
            activity?.supportFragmentManager?.commit {
                replace(R.id.frgContainerImagePicker, initialFragment)
            }
        }
    }

    private fun initObserver() {
        imagePickerShareVM.imageResultLiveData.observe(viewLifecycleOwner) {
            Timber.d("Data Image External: $it")
        }
    }

    private fun fetchData() {
        imagePickerShareVM.fetchImages()
    }

    private fun fetchDataWithPermission() {
        binding?.apply {
            // Check one permission at a time
            permissionManager
                .request(Permission.Storage)
                .rationale("We need permission to see your beautiful face")
                .checkPermission { isGranted ->
                    if (isGranted) {
                        root.success("We can see your face :)")
                        fetchData()
                    } else {
                        root.error("We couldn't access the camera :(")
                    }
                }
        }
    }

    private fun captureImageWithPermission() {
        binding?.apply {
            // Check one permission at a time
            permissionManager
                .request(Permission.Storage, Permission.Camera)
                .rationale("We need permission to see your beautiful face")
                .checkPermission { isGranted ->
                    if (isGranted) {
                        root.success("We can see your face :)")
                        fetchData()
                    } else {
                        root.error("We couldn't access the camera :(")
                    }
                }
        }
    }

    private fun onDone() {
        val selectedImages = imagePickerShareVM.selectedImages.value
        Timber.d("selectedImages: $selectedImages")
        // finishPickImages(selectedImages ?: arrayListOf())
    }

    fun onFolderClick(folder: Folder) {
        binding?.apply {
            activity?.supportFragmentManager?.commit {
                add(
                    R.id.frgContainerImagePicker,
                    ImageFragment.newInstance(folder.bucketId, config.imageGridCount)
                )
                addToBackStack(null)
            }
            toolbarImagePicker.setTitle(folder.name)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding?.apply {
            val fragment = activity?.supportFragmentManager?.findFragmentById(R.id.frgContainerImagePicker)
            if (fragment != null && fragment is FolderFragment) {
                toolbarImagePicker.setTitle(config.folderTitle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}