package com.innoveworkshop.gametest.assets

import com.innoveworkshop.gametest.engine.GameSurface
import com.innoveworkshop.gametest.engine.Rectangle
import com.innoveworkshop.gametest.engine.Vector
import com.innoveworkshop.gametest.engine.Physics
import com.innoveworkshop.gametest.engine.PhysicsBody

class Player(
    position: Vector?,
    width: Float,
    height: Float,
    color: Int,
    val surface: GameSurface
) : Rectangle (position, width, height, color) {

    override fun onStart(surface: GameSurface?) {
        super.onStart(surface)
        physicsBody = PhysicsBody(
            id = id!!,
            collision = false,
            mass = 2f,
            gravity = 3f,
            airResistence = 0f,
            initialPosition = position,
            initialVelocity = Vector(0f, 0f),
            currentPosition = position,
            currentVelocity = Vector(0f, 0f),
            surface = this.surface,
            objectTypeRec = this
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

    fun ApplyForce(
        force: Vector
    ){
        physicsBody!!.initialPosition = position

        val tempForce = Physics().ApplyForce(
            force = force,
            physicsBody = physicsBody!!
        )

        physicsBody!!.initialVelocity = tempForce
    }
}
