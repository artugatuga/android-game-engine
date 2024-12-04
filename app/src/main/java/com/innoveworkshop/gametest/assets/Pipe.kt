package com.innoveworkshop.gametest.assets

import com.innoveworkshop.gametest.engine.GameSurface
import com.innoveworkshop.gametest.engine.Physics
import com.innoveworkshop.gametest.engine.PhysicsBody
import com.innoveworkshop.gametest.engine.Rectangle
import com.innoveworkshop.gametest.engine.Vector

class Pipe(
    position: Vector?,
    width: Float,
    height: Float,
    color: Int
) : Rectangle (position, width, height, color) {
    var time = 0f
    var physicsBody: PhysicsBody? = null

    init {
        if (position != null) {
            physicsBody = PhysicsBody(
                mass = 2f,
                gravity = 0f,
                airResistence = 1f,
                initialPosition = position,
                initialVelocity = Vector(0f, 0f),
                currentPosition = position,
                currentVelocity = Vector(0f, 0f),
                maxLifeTime = 5
            )
        }
    }

    override fun onFixedUpdate() {
        super.onFixedUpdate()

        if(!isDestroyed){
            physicsBody!!.lifeTime += deltaTime
            time += deltaTime

            physicsBody = Physics().UpdatePhysicsBody(
                physicsBody = physicsBody!!,
                time = time
            )

            position = physicsBody!!.currentPosition
        }
    }

    fun ApplyForceToPipe(
        force: Vector
    ){
        physicsBody!!.initialPosition = position

        time = 0f
        val tempForce = Physics().ApplyForce(
            force = force,
            physicsBody = physicsBody!!
        )

        physicsBody!!.initialVelocity = tempForce
    }
}