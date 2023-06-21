package com.rohan.pixd.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rohan.pixd.R
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private var realBitmap: Bitmap? = null
    private var showBrightness: Boolean = false
    private lateinit var imageView: ImageView
    private lateinit var imageFilter: ImageView
    private lateinit var loadButton: Button
    private lateinit var filterButton: Button
    private lateinit var imageBrightness: ImageView
    private lateinit var seekBarBrightness: SeekBar
    private lateinit var frameControllerBrightness: FrameLayout
    private var seekBarValue: Int? = 0


    private val READ_STORAGE_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        loadButton = findViewById(R.id.loadButton)
        filterButton = findViewById(R.id.filterButton)
        imageFilter = findViewById(R.id.imageFilter)
        imageFilter = findViewById(R.id.imageFilter)
        imageBrightness = findViewById(R.id.imageBrightness)
        seekBarBrightness = findViewById(R.id.seekBarBrightness)
        frameControllerBrightness = findViewById(R.id.frameControllerBrightness)

        loadButton.setOnClickListener {
            if (checkPermissions()) {
                openGallery()
            } else {
                requestStoragePermission()
            }
        }

        imageFilter.setOnClickListener {
            applyFilter()
        }

        seekBarBrightness.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBarValue = seekBar!!.progress
                val bitmap = realBitmap
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
            showBrightness = !showBrightness
            if(showBrightness){
                frameControllerBrightness.visibility = View.VISIBLE;
            } else {
                frameControllerBrightness.visibility = View.GONE;
            }
        }

    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_STORAGE_PERMISSION_REQUEST_CODE
        )
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, READ_STORAGE_PERMISSION_REQUEST_CODE)
    }

    private fun applyFilter() {
        val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
        if (bitmap != null) {
            val filteredBitmap = applyFilterEffect(bitmap)
            imageView.setImageBitmap(filteredBitmap)
            saveImageToGallery(filteredBitmap)
        } else {
            Toast.makeText(this, "No image loaded", Toast.LENGTH_SHORT).show()
        }
    }

    private fun applyFilterEffect(bitmap: Bitmap): Bitmap {
        // Implement your image filter logic here
        // You can use libraries like RenderScript, OpenCV, or custom algorithms
        // This example applies a simple filter by converting the image to grayscale
        val grayscaleBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(grayscaleBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = filter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return grayscaleBitmap
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        val filePath = "${getExternalFilesDir(null)}/$filename"
        val file = File(filePath)

        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
        }

        // Add the image to the device's MediaStore gallery
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(file)
        mediaScanIntent.data = contentUri
        sendBroadcast(mediaScanIntent)
    }


    fun adjustBrightness(bitmap: Bitmap, brightness: Float): Bitmap? {
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
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                val inputStream = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                realBitmap = bitmap
                imageView.setImageBitmap(bitmap)
            }
        }
    }
}
