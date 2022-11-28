package com.example.picker_selector_app.extensions

import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.picker_selector_app.R
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

fun NavController.navigateAnim(@IdRes resId: Int, args: Bundle? = null) {
    navigate(resId, args, navOptions {
        anim {
            enter = R.anim.slide_in
            exit = R.anim.slide_out
        }
    })
}