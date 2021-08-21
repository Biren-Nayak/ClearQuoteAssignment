package com.example.clearquoteassignment.ui.fragments

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.clearquoteassignment.Image
import com.example.clearquoteassignment.databinding.FragmentHomeBinding
import com.example.clearquoteassignment.utils.*

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var index = 0
    private lateinit var image: Bitmap
    private lateinit var takePicture: ImageCapture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionPrompt()

    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener {
            index = (index + 1)%2
            binding.cameraFlipPosition.setImageDrawable(ContextCompat.getDrawable(requireContext(), cameraImages[index]))
            permissionPrompt()
        }

        binding.capture.setOnClickListener {

            binding.layout.visibility = GONE
            binding.imageView.visibility = VISIBLE
            binding.cancel.visibility = VISIBLE
            binding .done.visibility = VISIBLE
            lifecycle.coroutineScope.launchWhenResumed {
                val imageProxy = takePicture.takePicture(context?.executor!!)
                image = imageProxy.image?.toBitmap()!!
                binding.imageView.setImageBitmap(image)
            }

        }
        binding.cancel.setOnClickListener {
            binding.layout.visibility = VISIBLE
            binding.imageView.visibility = GONE
            binding.cancel.visibility = GONE
            binding .done.visibility = GONE
            binding.imageView.setImageDrawable(null)
        }

        binding.done.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToEditFragment(
                Image(image)
            ))
        }
        return binding.root
    }


    private fun permissionPrompt() {
        if (allPermissionsGranted()) {
            lifecycle.coroutineScope.launchWhenResumed {
                bindUseCases(requireContext().getCameraProvider())
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_CAMERA_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun bindUseCases(cameraProvider: ProcessCameraProvider) {
        cameraProvider.unbindAll()
        val preview = buildPreview(binding.previewView)
        takePicture = buildTakePicture()
        val cameraSelector = cameraValues[index]
        cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, takePicture, preview)
    }

    private fun allPermissionsGranted() = REQUIRED_CAMERA_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }
}