package com.example.zadaniejava

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.concurrent.thread

class GraphActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        val chart = findViewById<LineChart>(R.id.chart)
        drawGraph(chart, ProjectileData.results)

        val animationView = findViewById<SurfaceView>(R.id.animationView)
        drawAnimation(animationView, ProjectileData.results)

        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun drawGraph(chart: LineChart, results: List<Triple<Double, Double, Double>>) {
        val entries = results.map { Entry(it.first.toFloat(), it.third.toFloat()) }
        val dataSet = LineDataSet(entries, "Výška (m) od času (s)")
        val lineData = LineData(dataSet)

        chart.data = lineData

        val xAxis = chart.xAxis
        xAxis.granularity = 0.2f // Nastavenie fixného kroku 0,2s
        xAxis.labelCount = 10

        chart.invalidate() // Refresh grafu
    }

    private fun drawAnimation(surfaceView: SurfaceView, results: List<Triple<Double, Double, Double>>) {
        val surfaceHolder = surfaceView.holder
        val paint = Paint().apply {
            color = Color.GREEN
            strokeWidth = 12f
        }
        val groundPaint = Paint().apply {
            color = Color.RED
            strokeWidth = 4f
        }

        val yOffset = 700

        thread {
            var running = true
            var frame = 0

            while (running && frame < results.size - 1) {
                val canvas = surfaceHolder.lockCanvas()
                if (canvas != null) {
                    try {
                        canvas.drawColor(Color.BLACK)

                        val groundY = yOffset
                        canvas.drawLine(0f, groundY.toFloat(), canvas.width.toFloat(), groundY.toFloat(), groundPaint)

                        val t1 = results[frame].first.toFloat() * 100
                        val y1 = yOffset - results[frame].third.toFloat() * 2

                        val t2 = results[frame + 1].first.toFloat() * 100
                        val y2 = yOffset - results[frame + 1].third.toFloat() * 2

                        canvas.drawLine(t1, y1, t2, y2, paint)
                    } finally {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    }

                    Thread.sleep(100)
                    frame++
                }
            }
        }
    }



}
