package com.rohan.pixd.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.rohan.pixd.R
import com.rohan.pixd.utils.FilterHelper
import com.rohan.pixd.utils.PermissionHelper

class MainActivity : AppCompatActivity() {

    private val brightness: String = "brightness"
    private val crop: String = "crop"
    private val filter: String = "filter"
    private val sticker: String = "sticker"
    private val text: String = "text"
    private var somethingChanged: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var showBrightness: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var showCrop: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var showFilter: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var showSticker: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var showText: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var filterNumber: Int = 0;
    private var realBitmap: Bitmap? = null
    private var workingBitmap: Bitmap? = null
    private lateinit var imageView: ImageView
    private lateinit var imageFilter: ImageView
    private lateinit var loadButton: Button
    private lateinit var applyButton: Button
    private lateinit var filterButton: Button
    private lateinit var imageBrightness: ImageView
    private lateinit var imageCrop: ImageView
    private lateinit var imageSticker: ImageView
    private lateinit var imageText: ImageView
    private lateinit var seekBarBrightness: SeekBar
    private lateinit var frameControllerBrightness: FrameLayout
    private var seekBarValue: Int? = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listener()

        imageView = findViewById(R.id.imageView)
        loadButton = findViewById(R.id.loadButton)
        applyButton = findViewById(R.id.applyButton)
        filterButton = findViewById(R.id.filterButton)
        imageFilter = findViewById(R.id.imageFilter)
        imageBrightness = findViewById(R.id.imageBrightness)
        imageCrop = findViewById(R.id.imageCrop)
        imageSticker = findViewById(R.id.imageSticker)
        imageText = findViewById(R.id.imageText)
        seekBarBrightness = findViewById(R.id.seekBarBrightness)
        frameControllerBrightness = findViewById(R.id.frameControllerBrightness)

        loadButton.setOnClickListener {
            if (PermissionHelper.checkPermissions(this)) {
                openGallery()
            } else {
                PermissionHelper.requestStoragePermission(this)
            }
        }

        imageFilter.setOnClickListener {
            applyFilter()
        }

        applyButton.setOnClickListener {
            workingBitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
            somethingChanged.postValue(false)
            resetThings()
        }

        seekBarBrightness.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBarValue = seekBar!!.progress
                val bitmap = workingBitmap
                if(bitmap != null && seekBarValue?.toFloat() != null) {
                    val adjustedBitmap = adjustBrightness(bitmap, seekBarValue!!.toFloat());
                    imageView.setImageBitmap(adjustedBitmap)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        imageBrightness.setOnClickListener {
            changingNavigation(brightness)
        }

        imageCrop.setOnClickListener {
            changingNavigation(crop)
        }

        imageFilter.setOnClickListener {
            changingNavigation(filter)
        }

        imageSticker.setOnClickListener {
            changingNavigation(sticker)
        }

        imageText.setOnClickListener {
            changingNavigation(text)
        }

    }


    private fun resetThings() {
        showBrightness.postValue(false)
        showCrop.postValue(false)
        showFilter.postValue(false)
        showSticker.postValue(false)
        showText.postValue(false)
        imageView.setImageBitmap(workingBitmap)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PermissionHelper.READ_STORAGE_PERMISSION_REQUEST_CODE)
    }

    private fun applyFilter() {
        //val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
        val bitmap = workingBitmap
        if (bitmap != null) {
            val filteredBitmap = applyFilterEffect(bitmap)
            imageView.setImageBitmap(filteredBitmap)
            //saveImageToGallery(filteredBitmap)
        } else {
            Toast.makeText(this, "No image loaded", Toast.LENGTH_SHORT).show()
        }
    }

    private fun applyFilterEffect(bitmap: Bitmap): Bitmap {
        somethingChanged.postValue(true)
        if(filterNumber == 8){
            print("original")
            filterNumber = 0;
        } else {
            filterNumber += 1;
        }
        val grayscaleBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(grayscaleBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = FilterHelper().getFilter(filterNumber);//filter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return grayscaleBitmap
    }


    fun adjustBrightness(bitmap: Bitmap, brightness: Float): Bitmap? {
        somethingChanged.postValue(true)
        val adjustedBitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(adjustedBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.set(
            floatArrayOf(
                1f, 0f, 0f, 0f, brightness,
                0f, 1f, 0f, 0f, brightness,
                0f, 0f, 1f, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            )
        )
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return adjustedBitmap
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PermissionHelper.READ_STORAGE_PERMISSION_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                val inputStream = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                realBitmap = bitmap
                workingBitmap = bitmap
                imageView.setImageBitmap(bitmap)
            }
        }
    }


    private fun listener() {
        somethingChanged.observe(this, Observer {
            if(it){
                applyButton.visibility = View.VISIBLE
                loadButton.visibility = View.GONE
            } else {
                applyButton.visibility = View.GONE
                loadButton.visibility = View.VISIBLE
            }
        })

        showBrightness.observe(this, Observer {
            if(it){
                frameControllerBrightness.visibility = View.VISIBLE;
            } else {
                frameControllerBrightness.visibility = View.GONE;
            }
        })

        showCrop.observe(this, Observer {
            if(it){

            } else {

            }
        })

        showFilter.observe(this, Observer {
            if(it){
                applyFilter()
            } else {

            }
        })

        showSticker.observe(this, Observer {
            if(it){

            } else {

            }
        })

        showText.observe(this, Observer {
            if(it){

            } else {

            }
        })
    }

    private fun changingNavigation(value: String){
        resetThings()
        when(value){
            brightness->{
                showBrightness.postValue(true)
            }
            crop->{
                showCrop.postValue(true)
            }
            filter->{
                showFilter.postValue(true)
            }
            sticker->{
                showSticker.postValue(true)
            }
            text->{
                showText.postValue(true)
            }
        }
    }
}
