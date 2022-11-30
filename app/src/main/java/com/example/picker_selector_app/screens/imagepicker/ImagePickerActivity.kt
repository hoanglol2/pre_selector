package com.example.picker_selector_app.screens.imagepicker

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.commit
import com.example.picker_selector_app.R
import com.example.picker_selector_app.common.PermissionManager
import com.example.picker_selector_app.consts.EXTRA_CONFIG
import com.example.picker_selector_app.consts.Permission
import com.example.picker_selector_app.databinding.ActivityImagePickerBinding
import com.example.picker_selector_app.models.ImagePickerConfig
import com.example.picker_selector_app.screens.images.FolderFragment
import com.example.picker_selector_app.viewmodels.ImagePickerViewModel

class ImagePickerActivity : AppCompatActivity() {

    private lateinit var config: ImagePickerConfig
    private val permissionManager = PermissionManager.from(this)
    private lateinit var binding: ActivityImagePickerBinding
    private val imagePickerVM: ImagePickerViewModel by viewModels {
        ImagePickerViewModel.factory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent == null) {
            finish()
            return
        }
        config = intent.getParcelableExtra(EXTRA_CONFIG) ?: ImagePickerConfig()
        config.initDefaultValues(this)
        setupBarTheme()
        binding = ActivityImagePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        fetchDataWithPermission()
    }

    private fun setupBarTheme() {
        window.apply {
            // Setup status bar theme
            statusBarColor = Color.parseColor(config.statusBarColor)
            WindowCompat.setDecorFitsSystemWindows(this, false)
            WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars =
                config.isLightStatusBar
        }
    }

    private fun initViews() {
        binding.apply {
            val initialFragment =
                if (config.isFolderMode) FolderFragment.newInstance(config.folderGridCount)
                else FolderFragment.newInstance(config.folderGridCount)
            toolbarImagePicker.config(config)
            supportFragmentManager.commit {
                replace(R.id.frgContainerImagePicker, initialFragment)
            }
        }
    }

    private fun fetchDataWithPermission() {
        binding.apply {
            permissionManager
                .request(Permission.Storage)
                .rationale("We need permission to see your beautiful face")
                .isShowAlertDialog(false)
                .checkPermission { isGranted ->
                    if (isGranted) {
                        fetchData()
                    }
                }
        }
    }

    private fun fetchData() {
        imagePickerVM.fetchImages()
    }
}