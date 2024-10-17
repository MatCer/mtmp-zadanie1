package com.example.zadaniejava

object ProjectileData {
    var velocity: Double = 0.0
    var angle: Double = 0.0
    var results: List<Triple<Double, Double, Double>> = emptyList()

    fun calculateProjectile() {
        val g = 9.81
        val radAngle = Math.toRadians(angle)
        val vx = velocity * kotlin.math.cos(radAngle)
        val vy = velocity * kotlin.math.sin(radAngle)
        val tTotal = 2 * vy / g
        val timeStep = 0.1
        val result = mutableListOf<Triple<Double, Double, Double>>()

        var t = 0.0
        while (t <= tTotal) {
            val x = vx * t
            val y = vy * t - 0.5 * g * t * t
            if (y >= 0) {
                result.add(Triple(t, x, y))
            }
            t += timeStep
        }

        val finalX = vx * tTotal
        result.add(Triple(tTotal, finalX, 0.0))

        results = result
    }
}
