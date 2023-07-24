package com.rohan.pixd.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

class MeasureCropView constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private val boxPaint: Paint
    private val handlePaint: Paint
    private var boxRect: RectF
    private val handleSize = 30f
    private var activeHandle: Handle? = null
    private var onMeasureChangeListener: OnMeasureChangeListener? = null

    init {
        boxPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 10f
        }

        handlePaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
        }

        boxRect = RectF()
    }

    interface OnMeasureChangeListener{
        fun onMeasureChanged()
    }

    fun setMeasureChangeListener(onMeasureChangeListener: OnMeasureChangeListener): RectF{
        this.onMeasureChangeListener = onMeasureChangeListener
        return RectF(boxRect.left, boxRect.top, boxRect.right, boxRect.bottom)
    }

    fun getCropCoordinates(): RectF{
        return boxRect//RectF(boxRect.left, boxRect.top, boxRect.right, boxRect.bottom)
    }

    fun setDefaultBoxSize(left: Float, top: Float, right: Float, bottom: Float){
        boxRect = RectF(left, top, right, bottom)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw the box rectangle
        canvas.drawRect(boxRect, boxPaint)

        // Draw handles at each corner of the box
        drawHandle(canvas, boxRect.left, boxRect.top)
        drawHandle(canvas, boxRect.right, boxRect.top)
        drawHandle(canvas, boxRect.right, boxRect.bottom)
        drawHandle(canvas, boxRect.left, boxRect.bottom)
    }

    private fun drawHandle(canvas: Canvas, x: Float, y: Float) {
        canvas.drawCircle(x, y, handleSize, handlePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                activeHandle = getTouchedHandle(x, y)
                if (activeHandle != null) {
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (activeHandle != null) {
                    println("rohan top=${boxRect.top} , left=${boxRect.left}, right=${boxRect.right} , bottom=${boxRect.bottom}")
                    adjustBox(activeHandle!!, x, y)
                    invalidate()
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                activeHandle = null
                onMeasureChangeListener?.onMeasureChanged()
                //onBoxChange.invoke()
                return true
            }
        }

        return super.onTouchEvent(event)
    }

    private fun getTouchedHandle(x: Float, y: Float): Handle? {
        if (isWithinHandle(x, y, boxRect.left, boxRect.top)) {
            return Handle.TOP_LEFT
        }
        if (isWithinHandle(x, y, boxRect.right, boxRect.top)) {
            return Handle.TOP_RIGHT
        }
        if (isWithinHandle(x, y, boxRect.right, boxRect.bottom)) {
            return Handle.BOTTOM_RIGHT
        }
        if (isWithinHandle(x, y, boxRect.left, boxRect.bottom)) {
            return Handle.BOTTOM_LEFT
        }
        return null
    }

    private fun isWithinHandle(x: Float, y: Float, handleX: Float, handleY: Float): Boolean {
        val dx = x - handleX
        val dy = y - handleY
        return dx * dx + dy * dy <= handleSize * handleSize
    }

    private fun adjustBox(handle: Handle, x: Float, y: Float) {
        when (handle) {
            Handle.TOP_LEFT -> {
                boxRect.left = x
                boxRect.top = y
            }
            Handle.TOP_RIGHT -> {
                boxRect.right = x
                boxRect.top = y
            }
            Handle.BOTTOM_RIGHT -> {
                boxRect.right = x
                boxRect.bottom = y
            }
            Handle.BOTTOM_LEFT -> {
                boxRect.left = x
                boxRect.bottom = y
            }
        }
    }

    enum class Handle {
        TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT
    }

}