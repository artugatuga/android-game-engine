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
    var time: Float = 0f
    var isFlying = false

    override fun onFixedUpdate() {
        super.onFixedUpdate()

        if (!isFlying) {
            time += deltaTime
            position.y += Physics().Gravity(time)
        }else{
            ApplyForce()
        }
    }

    fun ApplyForce(
        force: Float = 10f
    ){
        var tempForce = Physics().ApplyForce(time, force)

        if(tempForce < 0){
            isFlying = false
            return
        }

        position.y -= Physics().ApplyForce(time, force)
    }
}
