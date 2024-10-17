package com.example.zadaniejava

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var etVelocity: EditText
    private lateinit var etAngle: EditText
    private val client = OkHttpClient()
    private val dotenv = dotenv({
        directory = "/assets"
        filename = "env"
    })

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

            // Uloženie hodnôt
            saveValues(velocity, angle)

            calculateProjectileOnServer(velocity, angle)
        }

        btnTable.setOnClickListener {
            startActivity(Intent(this, TableActivity::class.java))
        }

        btnGraph.setOnClickListener {
            startActivity(Intent(this, GraphActivity::class.java))
        }
    }

    private fun calculateProjectileOnServer(velocity: Double, angle: Double) {
        Log.i("env", dotenv["API_URL"])
        val baseUrl = dotenv["API_URL"]
        val url = "$baseUrl/calculate"

        val json = """
        {
            "velocity": $velocity,
            "angle": $angle
        }
    """.trimIndent()

        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .header("Content-Type", "application/json; charset=utf-8")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        Log.e("HTTP Error", "something went wrong")
                        return@use
                    }

                    val responseData = response.body?.string()
                    responseData?.let {
                        val jsonResponse = JSONObject(it)
                        val resultsArray = jsonResponse.getJSONArray("results")
                        val results = mutableListOf<Triple<Double, Double, Double>>()

                        for (i in 0 until resultsArray.length()) {
                            val item = resultsArray.getJSONArray(i)
                            val t = item.getDouble(0)
                            val x = item.getDouble(1)
                            val y = item.getDouble(2)
                            results.add(Triple(t, x, y))
                        }

                        withContext(Dispatchers.Main) {
                            ProjectileData.results = results
                            Toast.makeText(this@MainActivity, "Výpočet dokončený!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
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
