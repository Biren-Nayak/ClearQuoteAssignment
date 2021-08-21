package com.example.clearquoteassignment.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)

fun Image.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    buffer.rewind()
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return mutableBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
}

fun mutableBitmap(bitmap: Bitmap): Bitmap {
    return bitmap.copy(bitmap.config, true)
}



suspend fun ImageCapture.takePicture(executor: Executor): ImageProxy {
    return suspendCoroutine { continuation ->

        takePicture(executor, object : ImageCapture.OnImageCapturedCallback() {

            override fun onCaptureSuccess(image: ImageProxy) {
                continuation.resume(image)
                super.onCaptureSuccess(image)
            }

            override fun onError(exception: ImageCaptureException) {
                continuation.resumeWithException(exception)
                super.onError(exception)
            }
        })
    }
}

fun buildPreview(previewView: PreviewView): Preview = Preview.Builder()
    .build()
    .apply {
        setSurfaceProvider(previewView.surfaceProvider)
    }

fun buildTakePicture(): ImageCapture = ImageCapture.Builder()
    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
    .build()


suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).apply {
            addListener({
                continuation.resume(get())
            }, executor)
        }
    }