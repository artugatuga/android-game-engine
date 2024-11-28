package com.innoveworkshop.gametest.engine

class Physics {
    private val gravity = 9.81f

    fun Gravity(
        time: Float = 0f
    ):Float {
        val acceleration = time/gravity
        return gravity + gravity * acceleration
    }

    fun ApplyForce(
        time: Float = 0f,
        force: Float = 0f
    ):Float {
        val acceleration = force/time
        return acceleration
    }
}