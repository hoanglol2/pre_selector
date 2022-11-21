package com.example.picker_selector_app.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    var uri: Uri,
    var name: String,
    var bucketId: Long = 0,
    var bucketName: String = ""
) : Parcelable
