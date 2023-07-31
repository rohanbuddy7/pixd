package com.rohan.pixd.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView

class xd @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val movableTextView: AppCompatTextView
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var bitmap: Bitmap? = null

    init {
        movableTextView = AppCompatTextView(context)
        movableTextView.textSize = 30f
        movableTextView.setTextColor(resources.getColor(android.R.color.black))

        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        addView(movableTextView, layoutParams)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isTouchInsideTextView(x, y)) {
                    lastTouchX = x - movableTextView.x
                    lastTouchY = y - movableTextView.y
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val newLeft = x - lastTouchX
                val newTop = y - lastTouchY
                val newRight = newLeft + movableTextView.width
                val newBottom = newTop + movableTextView.height

                // Get the parent view's boundaries
                val parentRect = Rect(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom)

                // Limit the movement within the parent's boundaries
                if (parentRect.contains(newLeft.toInt(), newTop.toInt(), newRight.toInt(), newBottom.toInt())) {
                    movableTextView.x = newLeft
                    movableTextView.y = newTop
                }
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
        val bottom = movableTextView.bottom
        return x >= left && x < right && y >= top && y < bottom
    }

    fun setTextViewText(text: String) {
        movableTextView.text = text
    }

    fun getBitmapWithTextView(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }
}
