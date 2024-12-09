package com.innoveworkshop.gametest.assets

import com.innoveworkshop.gametest.engine.Circle
import com.innoveworkshop.gametest.engine.GameSurface
import com.innoveworkshop.gametest.engine.Vector
import com.innoveworkshop.gametest.engine.Physics
import com.innoveworkshop.gametest.engine.PhysicsBody

class TestCircle(
    position: Vector?,
    radius: Float,
    color: Int,
    val surface: GameSurface
) : Circle(position, radius, color) {

    override fun onStart(surface: GameSurface?) {
        super.onStart(surface)
        physicsBody = PhysicsBody(
            id = id!!,
            collision = false,
            mass = 2f,
            gravity = 0f,
            airResistence = 0f,
            initialPosition = position,
            initialVelocity = Vector(0f, 0f),
            currentPosition = position,
            currentVelocity = Vector(0f, 0f),
            surface = this.surface,
            objectTypeCir = this
        )
    }

    override fun onFixedUpdate() {
        super.onFixedUpdate()

        if (!isFloored) {
            physicsBody = Physics().UpdatePhysicsBody(
                physicsBody = physicsBody!!
            )
            position = physicsBody!!.currentPosition
        }else{
            physicsBody!!.timeFromForceAplied = 0f
        }
    }
}
