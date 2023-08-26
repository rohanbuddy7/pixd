package com.rohan.pixd.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import com.rohan.pixd.ui.main.MainActivity

class StickerHelper {

    fun combineBitmaps(backgroundBitmap: Bitmap, overlayBitmap: Bitmap, overlayX: Int, overlayY: Int): Bitmap? {
        val combinedBitmap = Bitmap.createBitmap(
            backgroundBitmap.width,
            backgroundBitmap.height,
            backgroundBitmap.config
        )
        val canvas = Canvas(combinedBitmap)

        // Draw the background bitmap
        canvas.drawBitmap(backgroundBitmap, 0f, 0f, null)

        // Resize the overlayBitmap to the desired width and height
        val scaledOverlayBitmap = Bitmap.createScaledBitmap(overlayBitmap, overlayBitmap.width, overlayBitmap.height, true)

        // Draw the overlay bitmap on top of the background
        canvas.drawBitmap(scaledOverlayBitmap!!, overlayX.toFloat(), overlayY.toFloat(), null)
        return combinedBitmap
    }


}