package com.rohan.pixd.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat

class MovableTextviewContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val movableTextView: AppCompatTextView
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var bitmap: Bitmap? = null
    private var initialDistance = 0f
    private var movetext = false
    companion object {
        var defaultTextSize = 30
    }

    init {
        movableTextView = AppCompatTextView(context)
        movableTextView.textSize = defaultTextSize.toFloat()
        movableTextView.setTextColor(resources.getColor(android.R.color.black))
        movableTextView.setBackgroundColor(resources.getColor(android.R.color.black))

        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        addView(movableTextView, layoutParams)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    val dx = event.getX(0) - event.getX(1)
                    val dy = event.getY(0) - event.getY(1)
                    initialDistance = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
                }
            }
            MotionEvent.ACTION_DOWN -> {
                //if (isTouchInsideTextView(x, y)) {
                    lastTouchX = x - movableTextView.x
                    lastTouchY = y - movableTextView.y

            }
            MotionEvent.ACTION_MOVE -> {
                //if(movetext) {
                    val newLeft = x - lastTouchX
                    val newTop = y - lastTouchY
                    val newRight = newLeft + movableTextView.width
                    val newBottom = newTop + movableTextView.height

                    // Get the parent view's boundaries
                    val parentRect = Rect(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom)

                    // Limit the movement within the parent's boundaries
                    /*if (parentRect.contains(
                            newLeft.toInt(),
                            newTop.toInt(),
                            newRight.toInt(),
                            newBottom.toInt()
                        )
                    ) {*/
                        movableTextView.x = newLeft
                        movableTextView.y = newTop
                    //}
                //}
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                movableTextView.isSelected = false
                invalidate()
            }
        }

        return true
    }


    private fun isTouchInsideTextView(x: Float, y: Float): Boolean {
        val left = movableTextView.left
        val top = movableTextView.top
        val right = movableTextView.right
        val bottom = movableTextView.bottom + (parent as View).top
        return x >= left && x < right && y >= top && y < bottom
    }

    fun setTextViewText(text: String) {
        movableTextView.text = text
    }

    fun setFontFamily(context: Context, fonts: Int) {
        movableTextView.typeface = ResourcesCompat.getFont(context, fonts)
    }

    fun setColor(context: Context, color: Int) {
        movableTextView.setTextColor(context.resources.getColor(color));
    }

    fun setTextSize(context: Context, size: Float) {
        movableTextView.textSize = size
    }

    fun getBitmapWithText(bitmap: Bitmap): Bitmap {
        // Create a bitmap with the size of the MovableTextViewContainer
        //val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Create a canvas with the bitmap
        val canvas = Canvas(bitmap)

        // Draw the MovableTextViewContainer on the canvas
        draw(canvas)

        // Draw the movableTextView's text on the canvas at its current position
        val paint = Paint().apply {
            color = Color.BLACK
            //textSize = movableTextView.textSize
            textAlign = Paint.Align.LEFT
            //isAntiAlias = true
        }
        movableTextView.text = ""
        canvas.drawText(
            movableTextView.text.toString(),
            movableTextView.x + movableTextView.paddingLeft,
            movableTextView.y + movableTextView.paddingTop + paint.textSize,
            paint
        )

        return bitmap
    }

}
