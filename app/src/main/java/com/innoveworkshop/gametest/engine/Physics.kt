package com.innoveworkshop.gametest.engine

import android.util.Log
import androidx.collection.floatObjectMapOf
import kotlin.concurrent.fixedRateTimer

class Physics {
    private val gravity = 9.81f * 20

    fun ApplyForce(
        force: Float = 1f,
        mass: Float = 1f,
    ):Float {
        val gravityForce = mass * gravity
        Log.d("ACCELARATION", gravityForce.toString())
        val finalForce = gravityForce - force
        val accelaration = finalForce/mass
        return accelaration
    }

    fun ChnageY(
        initialYPosition: Float,
        initialVelocity: Float,
        time: Float
    ): Float{
        val tempY = initialYPosition + (initialVelocity * time) + (gravity)*(time*time)
        return tempY
    }
}