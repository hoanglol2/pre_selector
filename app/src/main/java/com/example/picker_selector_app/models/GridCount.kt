package com.example.picker_selector_app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GridCount(val portrait: Int, val landscape: Int) : Parcelable