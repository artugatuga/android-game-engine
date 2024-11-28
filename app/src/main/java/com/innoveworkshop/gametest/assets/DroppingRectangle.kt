package com.innoveworkshop.gametest.assets

import android.os.Debug
import android.util.Log
import android.view.Gravity
import com.innoveworkshop.gametest.engine.Rectangle
import com.innoveworkshop.gametest.engine.Vector
import com.innoveworkshop.gametest.engine.Physics

class DroppingRectangle(
    position: Vector?,
    width: Float,
    height: Float,
    deltaT: Float,
    color: Int
) : Rectangle(position, width, height, color) {
    var deltaTime: Float = deltaT
    var time = 0f
    var mass = 2f
    var initialYPosition = 0f
    var initialVelocity = 0f

    init {
        if (position != null) {
            initialYPosition = position.y
        }
    }

    override fun onFixedUpdate() {
        super.onFixedUpdate()

        if (!isFloored) {
            position.y = Physics().ChnageY(
                initialYPosition,
                initialVelocity,
                time
            )
            time += deltaTime
            Log.d("TIME", time.toString())

        }else{
            time = 0f
        }
    }

    fun ApplyForce(
        force: Float = 1200f
    ){
        initialYPosition = position.y
        time = 0f
        val tempForce = Physics().ApplyForce(force, mass)

        initialVelocity = tempForce
    }
}
