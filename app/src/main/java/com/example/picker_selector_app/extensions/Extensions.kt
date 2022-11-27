package com.example.picker_selector_app.extensions

import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import androidx.annotation.ColorInt
import com.example.picker_selector_app.consts.Permission
import com.google.android.material.snackbar.Snackbar

fun Snackbar.withColor(@ColorInt colorInt: Int): Snackbar {
    this.view.setBackgroundColor(colorInt)
    return this
}

fun ViewGroup.success(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .withColor(Color.parseColor("#09AF00"))
        .show()
}

fun ViewGroup.error(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .withColor(Color.parseColor("#B00020"))
        .show()
}

fun Permission.getErrorMsg() = when (this) {
    Permission.Camera -> "Camera permission: "
    Permission.Location -> "Location permission: "
    Permission.Storage -> "Storage permission: "
    else -> "Unknown permission"
}

fun Context?.clearApp() {
    (this?.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)
        ?.clearApplicationUserData()
}