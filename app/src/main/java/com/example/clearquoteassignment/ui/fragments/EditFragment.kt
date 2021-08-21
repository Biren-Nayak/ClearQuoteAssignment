package com.example.clearquoteassignment.ui.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color.*
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.clearquoteassignment.R
import com.example.clearquoteassignment.databinding.FragmentEditBinding
import com.example.clearquoteassignment.utils.saveCache
import com.example.clearquoteassignment.utils.savePhotoToExternalStorage
import java.util.*


class EditFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentEditBinding
    private var penColor: Int = WHITE
    private lateinit var bmp: Bitmap
    private lateinit var alteredBitmap: Bitmap


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBinding.inflate(inflater, container, false)


        bmp = EditFragmentArgs.fromBundle(requireArguments()).image.image!!
        alteredBitmap = Bitmap.createBitmap(bmp)
        binding.imageView.setNewImage(alteredBitmap, bmp)
        togglePen(false)
        colorSetter()
        colorClickListeners()

        binding.back.setOnClickListener {
            findNavController().navigate(EditFragmentDirections.actionEditFragmentToHomeFragment())
            requireActivity().supportFragmentManager.popBackStack("EditFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        binding.share.setOnClickListener {
            shareImage()
        }

        //bottom Bar Buttons

        binding.pen.setOnClickListener {
            togglePen()
        }

        binding.color.setOnClickListener {
            binding.colorSelector.visibility =
                when (binding.colorSelector.visibility){
                    VISIBLE -> GONE
                    else -> VISIBLE
                }
        }

        binding.text.setOnClickListener {
            binding.editTextLayout.visibility = VISIBLE
            binding.textInput.hasFocus()
            togglePen(false)
        }

        binding.clear.setOnClickListener {
            binding.imageView.setNewImage(alteredBitmap, bmp)
            penColor = WHITE
            togglePen(false)
        }

        binding.circle.setOnClickListener {
            binding.imageView.drawCircle()
        }

        binding.square.setOnClickListener {
            binding.imageView.drawSquare()
        }

        binding.crop.setOnClickListener {
            binding.cropperView.visibility = VISIBLE
            binding.cropType.visibility = VISIBLE
            binding.cropImageView.setImageBitmap(binding.imageView.drawable.toBitmap())
        }

        binding.rotateClock.setOnClickListener {
            val bmp = binding.imageView.rotate(90F, binding.imageView.drawable.toBitmap())
            binding.imageView.setNewImage(bmp, Bitmap.createBitmap(bmp))

        }

        binding.rotateCounterClock.setOnClickListener {
            val bmp = binding.imageView.rotate(270F, binding.imageView.drawable.toBitmap())
            binding.imageView.setNewImage(bmp, Bitmap.createBitmap(bmp))
        }


        // Hidden Buttons

        binding.proceed.setOnClickListener {
            binding.imageView.addText(binding.textInput.text.toString())
            binding.editTextLayout.visibility = GONE

        }

        binding.decline.setOnClickListener {
            binding.editTextLayout.visibility = GONE
        }

        binding.grant.setOnClickListener {
            binding.cropperView.visibility = GONE
            binding.cropType.visibility = GONE
            val tempBmp = binding.cropImageView.croppedImage
            binding.imageView.setNewImage(tempBmp, Bitmap.createBitmap(tempBmp))
        }

        binding.deny.setOnClickListener {
            binding.cropperView.visibility = GONE
            binding.cropType.visibility = GONE
        }


        // Crop buttons

        binding.freeCrop.setOnClickListener {
            selectCropType(it)
            binding.cropImageView.clearAspectRatio()
        }

        binding.squareCrop.setOnClickListener {
            selectCropType(it)
            binding.cropImageView.setAspectRatio(1, 1)
        }

        binding.fourThreeCrop.setOnClickListener {
            selectCropType(it)
            binding.cropImageView.setAspectRatio(3, 4)
        }

        binding.sixteenNineCrop.setOnClickListener {
            selectCropType(it)
            binding.cropImageView.setAspectRatio(9, 16)
        }


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun shareImage() {
        val image = binding.imageView.drawable.toBitmap()
        context?.savePhotoToExternalStorage(UUID.randomUUID().toString(), image)

        val uri = context?.saveCache(image)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        startActivity(Intent.createChooser(intent, "Choose app to share Image"))
    }

    private fun selectCropType(it: View) {
        binding.freeCrop.isSelected = false
        binding.squareCrop.isSelected = false
        binding.fourThreeCrop.isSelected = false
        binding.sixteenNineCrop.isSelected = false
        it.isSelected = true
    }




    private fun togglePen(flag: Boolean = !binding.imageView.doodleEnabled) {
        binding.imageView.doodleEnabled = flag
        val color = if (flag) {
            penColor
        } else {
            LTGRAY
        }
        binding.pen.setColorFilter(color)

    }



    private fun colorSetter() {
        binding.blue.setColorFilter(BLUE)
        binding.cyan.setColorFilter(CYAN)
        binding.green.setColorFilter(GREEN)
        binding.magenta.setColorFilter(MAGENTA)
        binding.red.setColorFilter(RED)
        binding.white.setColorFilter(WHITE)
        binding.yellow.setColorFilter(YELLOW)
        binding.black.setColorFilter(BLACK)
    }

    private fun colorClickListeners() {

        binding.blue.setOnClickListener(this)
        binding.cyan.setOnClickListener(this)
        binding.green.setOnClickListener(this)
        binding.magenta.setOnClickListener(this)
        binding.red.setOnClickListener(this)
        binding.white.setOnClickListener(this)
        binding.yellow.setOnClickListener(this)
        binding.black.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        penColor = when(v?.id){
            R.id.blue -> BLUE
            R.id.cyan -> CYAN
            R.id.green -> GREEN
            R.id.magenta -> MAGENTA
            R.id.red -> RED
            R.id.white -> WHITE
            R.id.yellow -> YELLOW
            R.id.black -> BLACK
            else -> DKGRAY
        }
        binding.imageView.setPaintColor(penColor)
        binding.colorSelector.visibility = GONE
        togglePen(true)
    }





}