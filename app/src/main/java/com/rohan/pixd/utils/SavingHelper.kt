package com.rohan.pixd.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import java.io.OutputStream

class SavingHelper {

    fun saveImageToGallery(context: Context, bitmap: Bitmap) {
        val currentTimeMillis = System.currentTimeMillis()
        val filename = "$currentTimeMillis.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val contentResolver = context.contentResolver
        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let { uri ->
            try {
                val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
                outputStream?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    it.close()
                    Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        contentValues.clear()
                        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                        contentResolver.update(uri, contentValues, null, null)
                    }

                    // Optionally, you can share the image using a share intent
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "image/jpeg"
                        putExtra(Intent.EXTRA_STREAM, uri)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
                } ?: kotlin.run {
                    Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
            }
        }
    }

}