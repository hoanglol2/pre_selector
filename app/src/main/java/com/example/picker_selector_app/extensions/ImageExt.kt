package com.example.picker_selector_app.extensions

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.picker_selector_app.R

fun ImageView.loadImage(uri: Uri?) {
    val options: RequestOptions =
        RequestOptions().placeholder(R.drawable.image_placeholder_drawable)
            .error(R.drawable.image_error)
            .centerCrop()

    Glide.with(context)
        .load(uri)
        .apply(options)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}