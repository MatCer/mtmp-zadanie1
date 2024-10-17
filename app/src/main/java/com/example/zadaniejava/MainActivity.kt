package com.example.zadaniejava

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etVelocity: EditText
    private lateinit var etAngle: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etVelocity = findViewById(R.id.et_velocity)
        etAngle = findViewById(R.id.et_angle)
        val btnCalculate = findViewById<Button>(R.id.btn_calculate)
        val btnTable = findViewById<Button>(R.id.btn_table)
        val btnGraph = findViewById<Button>(R.id.btn_graph)

        // Načítanie uložených hodnôt
        loadValues()

        btnCalculate.setOnClickListener {
            val velocity = etVelocity.text.toString().toDoubleOrNull() ?: 0.0
            val angle = etAngle.text.toString().toDoubleOrNull() ?: 0.0

            ProjectileData.velocity = velocity
            ProjectileData.angle = angle
            ProjectileData.calculateProjectile()

            // Uloženie hodnôt
            saveValues(velocity, angle)

            Toast.makeText(this, "Výpočet dokončený!", Toast.LENGTH_SHORT).show()
        }

        btnTable.setOnClickListener {
            startActivity(Intent(this, TableActivity::class.java))
        }

        btnGraph.setOnClickListener {
            startActivity(Intent(this, GraphActivity::class.java))
        }
    }

    // Uloženie hodnôt do SharedPreferences
    private fun saveValues(velocity: Double, angle: Double) {
        val sharedPref = getSharedPreferences("InputValues", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("VELOCITY", velocity.toString())
            putString("ANGLE", angle.toString())
            apply()
        }
    }

    // Načítanie hodnôt zo SharedPreferences
    private fun loadValues() {
        val sharedPref = getSharedPreferences("InputValues", Context.MODE_PRIVATE)
        etVelocity.setText(sharedPref.getString("VELOCITY", ""))
        etAngle.setText(sharedPref.getString("ANGLE", ""))
    }
}
