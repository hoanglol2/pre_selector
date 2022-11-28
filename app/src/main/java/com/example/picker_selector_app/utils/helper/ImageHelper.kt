package com.example.picker_selector_app.utils.helper

import com.example.picker_selector_app.models.Folder
import com.example.picker_selector_app.models.Image

object ImageHelper {

    fun singleListFromImage(image: Image): ArrayList<Image> {
        val images = arrayListOf<Image>()
        images.add(image)
        return images
    }

    fun folderListFromImages(images: List<Image>): List<Folder> {
        val folderMap: MutableMap<Long, Folder> = LinkedHashMap()
        for (image in images) {
            val bucketId = image.bucketId
            val bucketName = image.bucketName
            var folder = folderMap[bucketId]
            if (folder == null) {
                folder = Folder(bucketId, bucketName)
                folderMap[bucketId] = folder
            }
            folder.images.add(image)
        }
        return ArrayList(folderMap.values)
    }

    fun filterImages(images: ArrayList<Image>, bucketId: Long?): ArrayList<Image> {
        if (bucketId == null || bucketId == 0L) return images

        val filteredImages = arrayListOf<Image>()
        for (image in images) {
            if (image.bucketId == bucketId) {
                filteredImages.add(image)
            }
        }
        return filteredImages
    }

    fun findImageIndex(image: Image, images: List<Image>): Int {
        for (i in images.indices) {
            if (images[i].uri == image.uri) {
                return i
            }
        }
        return -1
    }

    fun findImageIndexes(subImages: List<Image>, images: List<Image>): ArrayList<Int> {
        val indexes = arrayListOf<Int>()
        for (image in subImages) {
            for (i in images.indices) {
                if (images[i].uri == image.uri) {
                    indexes.add(i)
                    break
                }
            }
        }
        return indexes
    }


    fun isGifFormat(image: Image): Boolean {
        val fileName = image.name;
        val extension = if (fileName.contains(".")) {
            fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length)
        } else ""

        return extension.equals("gif", ignoreCase = true)
    }
}