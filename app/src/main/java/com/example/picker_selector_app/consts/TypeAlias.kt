package com.example.picker_selector_app.consts

import com.example.picker_selector_app.models.Folder
import com.example.picker_selector_app.models.Image

typealias OnItemListener = () -> Unit
typealias OnItemFolderListener = (Folder) -> Unit
typealias OnImagePickerCallback = (List<Image>?) -> Unit