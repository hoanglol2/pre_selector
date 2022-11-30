package com.example.picker_selector_app.extensions

import android.content.Context
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.picker_selector_app.consts.OnImagePickerCallback
import com.example.picker_selector_app.models.ImagePickerLauncher

fun Fragment.registerImagePicker(
    context: () -> Context? = { this.context },
    onImagePicker: OnImagePickerCallback
) = ImagePickerLauncher(context, createLauncher(onImagePicker))

fun Fragment.createLauncher(onImagePicker: OnImagePickerCallback) =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val image = ImagePickerLauncher.getImages(it.data)
        onImagePicker(image)
    }