package com.example.picker_selector_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.picker_selector_app.consts.OnItemFolderListener
import com.example.picker_selector_app.databinding.ItemImagePickerFolderBinding
import com.example.picker_selector_app.extensions.loadImage
import com.example.picker_selector_app.models.Folder

class ImageFolderAdapter : RecyclerView.Adapter<ImageFolderAdapter.ImageFolderVH>() {

    private var onItemClickListener: OnItemFolderListener? = null
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    companion object {
        private const val ITEM_IMAGE_FIRST = 0
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Folder>() {
            override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean {
                return oldItem.bucketId == newItem.bucketId
            }

            override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun setList(list: List<Folder>) {
        differ.submitList(list)
    }

    fun setOnItemClickListener(listener: OnItemFolderListener) {
        onItemClickListener = listener
    }

    inner class ImageFolderVH(private val viewBinding: ItemImagePickerFolderBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data: Folder) {
            viewBinding.apply {
                val previewImage = data.images.getOrNull(ITEM_IMAGE_FIRST)
                ivThumbnail.loadImage(previewImage?.uri)
                tvFolderName.text = data.name
                tvPhotoCount.text = data.images.size.toString()
                itemView.setOnClickListener {
                    onItemClickListener?.invoke(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFolderVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ImageFolderVH(
            ItemImagePickerFolderBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageFolderVH, position: Int) {
        if (position !in differ.currentList.indices) return
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size
}