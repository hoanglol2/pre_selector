package com.example.picker_selector_app.utils.helper

import android.content.Context
import android.widget.Toast

class ToastHelper {
    companion object {
        var toast: Toast? = null

        fun show(context: Context, text: String, duration: Int = Toast.LENGTH_SHORT) {
            if (toast == null) {
                toast = Toast.makeText(context.applicationContext, text, duration)
            } else {
                toast?.cancel()
                toast?.setText(text)
            }
            toast?.show()
        }
    }
}