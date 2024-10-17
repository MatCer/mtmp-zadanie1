package com.example.zadaniejava

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)

        val btnBack = findViewById<Button>(R.id.btn_back)
        val scrollView = findViewById<ScrollView>(R.id.scroll_view)

        displayResults(ProjectileData.results)

        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun displayResults(results: List<Triple<Double, Double, Double>>) {
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        tableLayout.isStretchAllColumns = true

        val header = TableRow(this)
        val headers = arrayOf("Čas (s)", "Pozícia x (m)", "Pozícia y (m)")
        for (headerText in headers) {
            val tv = TextView(this)
            tv.text = headerText
            tv.setPadding(16, 16, 16, 16)
            header.addView(tv)
        }
        tableLayout.addView(header)

        for ((index, result) in results.withIndex()) {
            val row = TableRow(this)

            val timeView = TextView(this)
            timeView.text = String.format("%.2f", result.first)
            timeView.setPadding(16, 16, 16, 16)

            val xView = TextView(this)
            xView.text = String.format("%.2f", result.second)
            xView.setPadding(16, 16, 16, 16)

            val yView = TextView(this)
            yView.text = String.format("%.2f", result.third)
            yView.setPadding(16, 16, 16, 16)

            // Nastavenie pozadia podľa indexu (párny alebo nepárny riadok)
            if (index % 2 == 0) {
                row.setBackgroundResource(android.R.drawable.divider_horizontal_bright)  // Párny riadok - tmavší odtieň
            }

            // Pridanie textových polí do riadku
            row.addView(timeView)
            row.addView(xView)
            row.addView(yView)
            tableLayout.addView(row)
        }
    }
}
