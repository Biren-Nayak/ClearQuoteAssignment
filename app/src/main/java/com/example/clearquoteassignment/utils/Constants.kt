package com.example.clearquoteassignment.utils

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA
import androidx.core.content.FileProvider
import com.example.clearquoteassignment.R
import java.io.File

const val REQUEST_CODE_PERMISSIONS = 10
val cameraValues = arrayOf(DEFAULT_FRONT_CAMERA, DEFAULT_BACK_CAMERA)
val REQUIRED_CAMERA_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
const val REQUIRED_REQUEST_CODE = 112
val cameraImages = arrayOf(
    R.drawable.ic_outline_camera_front_24,
    R.drawable.ic_outline_camera_rear_24
)





