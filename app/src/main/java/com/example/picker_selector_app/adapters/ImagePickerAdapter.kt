package com.example.picker_selector_app.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.picker_selector_app.databinding.ItemImagePickerBinding
import com.example.picker_selector_app.extensions.loadImage
import com.example.picker_selector_app.models.Image
import com.example.picker_selector_app.models.ImagePickerConfig
import com.example.picker_selector_app.utils.helper.ImageHelper
import com.example.picker_selector_app.utils.helper.ToastHelper

class ImagePickerAdapter : RecyclerView.Adapter<ImagePickerAdapter.ImagePickerVH>() {

    private var config: ImagePickerConfig? = null
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    private val selectedImages = mutableListOf<Image>()

    fun setConfig(config: ImagePickerConfig) {
        this.config = config
    }

    fun setList(list: List<Image>) {
        differ.submitList(list)
    }

    fun setListSelectedImage(list: List<Image>) {
        selectedImages.run {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.bucketId == newItem.bucketId
            }

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem == newItem
            }
        }
    }

    private fun setupItemForeground(view: View, isSelected: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.foreground = if (isSelected) ColorDrawable(
                Color.parseColor("#4D000000")
//                ContextCompat.getColor(
//                    view.context,
//                    R.color.imagepicker_black_alpha_30
//                )
            ) else null
        }
    }

    inner class ImagePickerVH(val viewBinding: ItemImagePickerBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data: Image, position: Int) {
            viewBinding.apply {
                val selectedIndex = ImageHelper.findImageIndex(data, selectedImages)
                val isSelected = config?.isMultipleMode == true && selectedIndex != -1

                ivThumbnail.loadImage(data.uri)
                setupItemForeground(ivThumbnail, isSelected)
                tvGifIndicator.isVisible = ImageHelper.isGifFormat(data)
                ivSelectedIcon.isVisible = isSelected && !(config?.isShowNumberIndicator ?: false)
                tvSelectedNumb.isVisible = isSelected && config?.isShowNumberIndicator == true
                tvSelectedNumb.text =
                    if (tvSelectedNumb.isVisible) (selectedIndex + 1).toString() else ""
                itemView.setOnClickListener {
                    selectOrRemoveImage(it, data, position)
                }
            }
        }
    }

    private fun selectOrRemoveImage(view: View, image: Image, position: Int) {
        if (config?.isMultipleMode == true) {
            val maxSize = config?.maxSize ?: Int.MAX_VALUE
            val selectedIndex = ImageHelper.findImageIndex(image, selectedImages)
            if (selectedIndex != -1) {
                selectedImages.removeAt(selectedIndex)
                notifyItemChanged(position, ImageUnselected())
                val indexes = ImageHelper.findImageIndexes(selectedImages, differ.currentList)
                for (index in indexes) {
                    notifyItemChanged(index, ImageSelectedOrUpdated())
                }
            } else {
                if (selectedImages.size >= maxSize) {
                    val message =
                        if (config?.limitMessage != null) config?.limitMessage else String.format(
                            "HEELLO",
                            maxSize
                        )
                    ToastHelper.show(view.context, message ?: "")
                    return
                } else {
                    selectedImages.add(image)
                    notifyItemChanged(position, ImageSelectedOrUpdated())
                }
            }
//            imageSelectListener.onSelectedImagesChanged(selectedImages)
        } else {
//            imageSelectListener.onSingleModeImageSelected(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePickerVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ImagePickerVH(
            ItemImagePickerBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImagePickerVH, position: Int) {
        if (position !in differ.currentList.indices) return
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    class ImageSelectedOrUpdated
    class ImageUnselected
}