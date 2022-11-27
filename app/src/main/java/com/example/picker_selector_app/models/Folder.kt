package com.example.picker_selector_app.models

import java.util.ArrayList

data class Folder(
    var bucketId: Long,
    var name: String,
    var images: ArrayList<Image> = arrayListOf()
)