package com.example.picker_selector_app.models

import com.example.picker_selector_app.consts.CallbackStatus

data class Result(val status: CallbackStatus, val images: ArrayList<Image>)
