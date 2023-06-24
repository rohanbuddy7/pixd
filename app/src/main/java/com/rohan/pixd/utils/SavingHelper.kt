package com.rohan.pixd.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

class SavingHelper {

    private fun saveImageToGallery(context: Context, bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        val filePath = "${context.getExternalFilesDir(null)}/$filename"
        val file = File(filePath)

        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }

        // Add the image to the device's MediaStore gallery
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(file)
        mediaScanIntent.data = contentUri
        context.sendBroadcast(mediaScanIntent)
    }
}