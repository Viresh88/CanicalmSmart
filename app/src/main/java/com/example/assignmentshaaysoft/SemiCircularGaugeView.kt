package com.example.assignmentshaaysoft


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class SemiCircularGaugeView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val outerArcPaint = Paint().apply {
        color = Color.parseColor("#494846")
        style = Paint.Style.STROKE
        strokeWidth = 90f
        isAntiAlias = true
    }

    private val progressArcPaint = Paint().apply {
        color = Color.parseColor("#FFA726")
        style = Paint.Style.STROKE
        strokeWidth = 90f
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

//    private val innerCirclePaint = Paint().apply {
//        color = Color.WHITE
//        style = Paint.Style.FILL
//        isAntiAlias = true
//    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val rectF = RectF()
    private var sweepAngle = 180f // Set this dynamically based on your data

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val radius = Math.min(width, height) / 2 * 0.8f

        val centerX = width / 2
        val centerY = height / 2

        // Draw the outer semi-circle
        rectF.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )
        canvas.drawArc(rectF, 180f, 180f, false, outerArcPaint)

        // Draw the progress arc
        canvas.drawArc(rectF, 180f, sweepAngle, false, progressArcPaint)



    }

    fun setProgress(progress: Float) {
        sweepAngle = progress
        invalidate()
    }
}
