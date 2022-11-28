package com.example.picker_selector_app.viewmodels

import android.app.Application
import android.content.ContentUris
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import androidx.lifecycle.*
import com.example.picker_selector_app.MyApplication
import com.example.picker_selector_app.consts.CallbackStatus
import com.example.picker_selector_app.models.Folder
import com.example.picker_selector_app.models.Image
import com.example.picker_selector_app.models.ImagePickerConfig
import com.example.picker_selector_app.models.Result
import com.example.picker_selector_app.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.ref.WeakReference

class ImagePickerViewModel(application: Application): AndroidViewModel(application) {
    private val contextRef = WeakReference(application.applicationContext)
    private var jobImageExternal: Job? = null
    private val _imageResultLiveData = MutableLiveData(Result(CallbackStatus.IDLE, arrayListOf()))
    val imageResultLiveData: LiveData<Result> = _imageResultLiveData
    private var config: ImagePickerConfig? = null
    lateinit var selectedImages: MutableLiveData<ArrayList<Image>>
    val folderLiveEvent = SingleLiveEvent<Folder>()

    fun setConfig(config: ImagePickerConfig) {
        this.config = config
        selectedImages = MutableLiveData(config.selectedImages)
    }

    fun getConfig() = config ?: ImagePickerConfig()

    fun fetchImages() {
        if (jobImageExternal != null) return

        _imageResultLiveData.postValue(Result(CallbackStatus.FETCHING, arrayListOf()))
        jobImageExternal = viewModelScope.launch {
            try {
                val images = fetchImagesFromExternalStorage()
                _imageResultLiveData.postValue(Result(CallbackStatus.SUCCESS, images))
            } catch (e: Exception) {
                _imageResultLiveData.postValue(Result(CallbackStatus.SUCCESS, arrayListOf()))
            } finally {
                jobImageExternal = null
            }
        }
    }

    private fun getImageCollectionUri() =
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    private suspend fun fetchImagesFromExternalStorage(): ArrayList<Image> {
        if (contextRef.get() == null) return arrayListOf()

        return withContext(Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            )
            val imageCollectionUri = getImageCollectionUri()
            contextRef.get()?.contentResolver?.query(
                imageCollectionUri,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " DESC"
            )?.use { cursor ->
                val images = arrayListOf<Image>()
                val idColumn = cursor.getColumnIndexOrThrow(
                    MediaStore.Images.Media._ID)
                val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val bucketIdColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
                val bucketNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val bucketId = cursor.getLong(bucketIdColumn)
                    val bucketName = cursor.getStringOrNull(bucketNameColumn) ?: ""
                    val uri = ContentUris.withAppendedId(imageCollectionUri, id)
                    val image = Image(uri, name, bucketId, bucketName)
                    images.add(image)
                }
                cursor.close()
                images
            } ?: throw IOException()
        }
    }

    companion object {
        fun factory(application: Application = MyApplication.getInstance()) =
            object : ViewModelProvider.AndroidViewModelFactory() {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return if (modelClass.isAssignableFrom(ImagePickerViewModel::class.java)) {
                        ImagePickerViewModel(application) as T
                    } else {
                        throw IllegalArgumentException("ViewModel unknown class!")
                    }
                }
            }
    }
}