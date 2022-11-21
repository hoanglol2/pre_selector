package com.example.picker_selector_app.consts

sealed class CallbackStatus {
    object IDLE : CallbackStatus()
    object FETCHING : CallbackStatus()
    object SUCCESS : CallbackStatus()
}