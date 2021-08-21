package com.example.clearquoteassignment.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@RequiresApi(Build.VERSION_CODES.Q)
fun Context.savePhotoToExternalStorage(displayName: String, bmp: Bitmap): Boolean {
    val imageCollection =   MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

    val contentValue = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.WIDTH, bmp.width)
        put(MediaStore.Images.Media.HEIGHT, bmp.height)
    }
    return try {
        contentResolver.insert(imageCollection, contentValue)?.also { uri ->
            contentResolver.openOutputStream(uri).use { outputStream ->
                if(!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                    throw IOException("Couldn't save bitmap")
                }
            }
        }?: throw IOException("Couldn't create MediaStore entry")
        true
    }catch (e: Exception) {
        e.printStackTrace()
        false
    }

}

fun Context.saveCache(image: Bitmap) : Uri? {
    var uri: Uri? = null
    val imageFolder = File(cacheDir, "images")
    try {
        imageFolder.mkdir()
        val file = File(imageFolder, "shared_image.jpg")
        val stream = FileOutputStream(file)
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
        uri = FileProvider.getUriForFile(this, "com.example.clearquoteassignment.fileProvider", file)
    }catch (e: IOException) {
        Log.d("ImageFragment", "IOException while trying to write file for sharing: " + e.message)
    }
    return uri
}



