package com.example.picker_selector_app.wigets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import com.example.picker_selector_app.databinding.LayoutImagePickerToolbarBinding
import com.example.picker_selector_app.models.ImagePickerConfig

class ImagePickerToolbar @JvmOverloads constructor(
    context: Context,
    attrsSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrsSet, defStyleAttr) {

    private lateinit var binding: LayoutImagePickerToolbarBinding

    init {
        initViews()
    }

    private fun initViews() {
        val layoutInflater = LayoutInflater.from(context)
        binding = LayoutImagePickerToolbarBinding.inflate(
            layoutInflater, this
        )
        addView(binding.root)
    }

    fun config(config: ImagePickerConfig) {
        binding.apply {
            val toolbarColor = Color.parseColor(config.toolbarColor)
            setBackgroundColor(toolbarColor)
            tvToolbarTitle.apply {
                text = if (config.isFolderMode) config.folderTitle else config.imageTitle
                setTextColor(toolbarColor)
            }
            tvToolbarDone.apply {
                text = config.doneTitle
                setTextColor(toolbarColor)
                isVisible = config.isAlwaysShowDoneButton
            }
            ivToolbarBack.setColorFilter(toolbarColor)
            ivToolbarCamera.apply {
                setColorFilter(toolbarColor)
                isVisible = config.isShowCamera
            }
        }
    }

    fun setTitle(title: String?) {
        binding.tvToolbarTitle.text = title
    }

    fun showDoneButton(isShow: Boolean) {
        binding.tvToolbarDone.isVisible = isShow
    }

    fun setOnBackClickListener(listener: OnClickListener) {
        binding.ivToolbarBack.setOnClickListener(listener)
    }

    fun setOnCameraClickListener(listener: OnClickListener) {
        binding.ivToolbarCamera.setOnClickListener(listener)
    }

    fun setOnDoneClickListener(listener: OnClickListener) {
        binding.tvToolbarDone.setOnClickListener(listener)
    }
}