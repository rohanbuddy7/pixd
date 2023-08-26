package com.rohan.pixd.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.rohan.pixd.ui.main.MainActivity

class MoveableStickerForegroundView : View {
    private var backgroundBitmap: Bitmap? = null
    private var foregroundBitmap: Bitmap? = null
    private var originalForegroundBitmap: Bitmap? = null
    private var x = 100 // Initial X-coordinate
    private var y = 200 // Initial Y-coordinate
    private var isMoving = true // Flag to indicate if the foreground bitmap is being moved
    private var onMovementDoneListener: OnMovementDoneListener? = null // Flag to indicate if the foreground bitmap is being moved

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        // Load your background and foreground bitmaps here
    }

    fun initBitmaps(backgroundBitmap: Bitmap,
                    foregroundBitamp: Bitmap,
                    onMovementDoneListener: OnMovementDoneListener){
        this.backgroundBitmap = backgroundBitmap
        val scaledOverlayBitmap = Bitmap.createScaledBitmap(foregroundBitamp, MainActivity.stickerWidth, MainActivity.stickerHeight, true)
        this.foregroundBitmap = scaledOverlayBitmap
        this.originalForegroundBitmap = scaledOverlayBitmap
        this.onMovementDoneListener = onMovementDoneListener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw the background bitmap
        canvas.drawBitmap(backgroundBitmap!!, 0f, 0f, null)

        // Draw the foreground bitmap at the updated x and y coordinates
        canvas.drawBitmap(foregroundBitmap!!, x.toFloat(), y.toFloat(), null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {}                // Check if the touch event occurred within the boundaries of the foreground bitmap
                /*if (event.x >= x && event.x < x + foregroundBitmap!!.width && event.y >= y && event.y < y + foregroundBitmap!!.height) {
                    isMoving = true
                }*/

            MotionEvent.ACTION_MOVE ->{ //if (isMoving) {

                // Update the x and y coordinates based on the touch movement
                x = event.x.toInt() - foregroundBitmap!!.width / 2
                y = event.y.toInt() - foregroundBitmap!!.height / 2

                // Ensure the foreground bitmap stays within the boundaries of the background bitmap
                if (x < 0) x = 0
                if (x > backgroundBitmap!!.width - foregroundBitmap!!.width) {
                    x = backgroundBitmap!!.width - foregroundBitmap!!.width
                }
                if (y < 0) y = 0
                if (y > backgroundBitmap!!.height - foregroundBitmap!!.height) {
                    y = backgroundBitmap!!.height - foregroundBitmap!!.height
                }

                // Redraw the view with the updated position
                invalidate()
            }

            MotionEvent.ACTION_UP -> {               // Stop moving the foreground bitmap when the touch is released
                //isMoving = false
                onMovementDoneListener?.onMovementChanged(x, y);
            }
        }
        return true
    }

    fun getForegroundBitmap(): Bitmap?{
        return foregroundBitmap;
    }

    fun resizeForegroundBitmap(newWidth: Int, newHeight: Int){
        val scaledOverlayBitmap = Bitmap.createScaledBitmap(originalForegroundBitmap!!, newWidth, newHeight, true)
        foregroundBitmap = scaledOverlayBitmap
        invalidate() // Redraw the view with the resized bitmap
        onMovementDoneListener?.onForegroundBitmapChanged(scaledOverlayBitmap)
    }

    interface OnMovementDoneListener{
        fun onMovementChanged(x: Int, y: Int)
        fun onForegroundBitmapChanged(foreground: Bitmap)
    }
}
