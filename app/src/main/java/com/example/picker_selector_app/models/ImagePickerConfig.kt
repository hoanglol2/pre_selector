package com.example.picker_selector_app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.ArrayList

@Parcelize
data class ImagePickerConfig(
    var statusBarColor: String = "#000000",
    var isLightStatusBar: Boolean = false,
    var toolbarColor: String = "#212121",
    var toolbarTextColor: String = "#FFFFFF",
    var toolbarIconColor: String = "#FFFFFF",
    var backgroundColor: String = "#424242",
    var progressIndicatorColor: String = "#009688",
    var selectedIndicatorColor: String = "#1976D2",
    var isCameraOnly: Boolean = false,
    var isMultipleMode: Boolean = false,
    var isFolderMode: Boolean = false,
    var isShowNumberIndicator: Boolean = false,
    var isShowCamera: Boolean = false,
    var maxSize: Int = Int.MAX_VALUE,
    var doneTitle: String? = null,
    var folderTitle: String? = null,
    var imageTitle: String? = null,
    var limitMessage: String? = null,
    var subDirectory: String? = null,
    var isAlwaysShowDoneButton: Boolean = false,
    var selectedImages: ArrayList<Image> = arrayListOf()
) : Parcelable
