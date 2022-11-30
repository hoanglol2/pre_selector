package com.example.picker_selector_app.models

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.picker_selector_app.consts.EXTRA_CONFIG
import com.example.picker_selector_app.consts.EXTRA_IMAGES
import com.example.picker_selector_app.screens.camera.CameraActivity
import com.example.picker_selector_app.screens.imagepicker.ImagePickerActivity

data class ImagePickerLauncher(
    private val onContext: () -> Context?,
    private val resultLauncher: ActivityResultLauncher<Intent>
) {
    companion object {
        fun getImages(intent: Intent?): List<Image>? {
            return if (intent != null) intent.getParcelableArrayListExtra(EXTRA_IMAGES)
            else listOf()
        }
    }

    fun launch(config: ImagePickerConfig = ImagePickerConfig()) {
        val intent = createImagePickerIntent(onContext(), config)
        resultLauncher.launch(intent)
    }

    private fun createImagePickerIntent(
        context: Context?,
        config: ImagePickerConfig
    ): Intent {
        val intent = if (config.isCameraOnly) {
            Intent(context, CameraActivity::class.java)
                .apply { addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) }
        } else {
            Intent(context, ImagePickerActivity::class.java)
        }
        intent.putExtra(EXTRA_CONFIG, config)
        return intent
    }
}
