package com.example.clinic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.temperature)
        val diagramView = findViewById<CustomDiagramView>(R.id.diagramView)
    }
}

class CustomDiagramView(context: Context) : View(context) {
    private val hours = listOf("00:00", "06:00", "12:00", "18:00", "00:00")
    private val temperatures = (26..46 step 2).toList()

    private val diagramPaint = Paint().apply {
        color = Color.parseColor("#2ECC9C")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val columnWidth: Float = width / (hours.size * 2).toFloat() // ширина столбца
        val maxTemp = temperatures.maxOrNull() ?: 0
        val tempRatio = height.toFloat() / maxTemp
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 30f
            textAlign = Paint.Align.CENTER
        }

        for ((index, temp) in temperatures.withIndex()) {
            val x = index * columnWidth * 2 + columnWidth / 2
            val y = height - temp * tempRatio

            val left = x - columnWidth / 2
            val top = y
            val right = x + columnWidth / 2
            val bottom = height.toFloat()

            canvas.drawRect(left, top, right, bottom, diagramPaint)

            // текст с температурой справа от столбца
            val textX = x + columnWidth / 2 + 15 // 15 - смещение для выравнивания текста
            val textY = y - 10 // расположение текста по вертикали
            canvas.drawText(temp.toString(), textX, textY, textPaint)

            // текст с временем
            canvas.drawText(hours[index], x, height.toFloat() - 20, textPaint)
        }
    }

}

//class CustomDiagramViewHeartRate(context: Context) : View(context) {
//    private val hours = listOf("00:00", "06:00", "12:00", "18:00", "00:00")
//    private val temperatures = (26..46 step 2).toList()
//
//    private val diagramPaint = Paint().apply {
//        color = Color.parseColor("#2ECC9C")
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        val columnWidth: Float = width / (hours.size * 2).toFloat() // ширина столбца
//        val maxTemp = temperatures.maxOrNull() ?: 0
//        val tempRatio = height.toFloat() / maxTemp
//        val textPaint = Paint().apply {
//            color = Color.BLACK
//            textSize = 30f
//            textAlign = Paint.Align.CENTER
//        }
//
//        for ((index, temp) in temperatures.withIndex()) {
//            val x = index * columnWidth * 2 + columnWidth / 2
//            val y = height - temp * tempRatio
//
//            val left = x - columnWidth / 2
//            val top = y
//            val right = x + columnWidth / 2
//            val bottom = height.toFloat()
//
//            canvas.drawRect(left, top, right, bottom, diagramPaint)
//
//            // текст с температурой справа от столбца
//            val textX = x + columnWidth / 2 + 15 // 15 - смещение для выравнивания текста
//            val textY = y - 10 // расположение текста по вертикали
//            canvas.drawText(temp.toString(), textX, textY, textPaint)
//
//            // текст с временем
//            canvas.drawText(hours[index], x, height.toFloat() - 20, textPaint)
//        }
//    }
//
//}