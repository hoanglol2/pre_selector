package com.example.picker_selector_app.models

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Parcelable
import com.example.picker_selector_app.R
import kotlinx.parcelize.Parcelize
import java.util.*

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
    var folderGridCount: GridCount = GridCount(2, 4),
    var imageGridCount: GridCount = GridCount(3, 5),
    var doneTitle: String? = null,
    var folderTitle: String? = null,
    var imageTitle: String? = null,
    var limitMessage: String? = null,
    var subDirectory: String? = null,
    var isAlwaysShowDoneButton: Boolean = false,
    var selectedImages: ArrayList<Image> = arrayListOf()
) : Parcelable {

    fun initDefaultValues(context: Context) {
        context.apply {
            if (folderTitle == null) {
                folderTitle = getString(R.string.albumsLabel)
            }
            if (imageTitle == null) {
                imageTitle = getString(R.string.photoLabel)
            }
            if (doneTitle == null) {
                doneTitle = getString(R.string.doneLabel).uppercase()
            }
            if (subDirectory == null) {
                subDirectory = getDefaultSubDirectoryName(this)
            }
        }
    }
    private fun getDefaultSubDirectoryName(context: Context): String {
        val packageManager = context.packageManager
        val appInfo: ApplicationInfo? = try {
            packageManager.getApplicationInfo(context.applicationContext.packageName ?: "", 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
        return (if (appInfo != null) {
            packageManager.getApplicationLabel(appInfo).toString()
        } else {
            context.getString(R.string.cameraLabel)
        })
    }
}
