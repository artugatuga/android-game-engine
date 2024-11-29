package com.innoveworkshop.gametest.assets

import com.innoveworkshop.gametest.engine.Rectangle
import com.innoveworkshop.gametest.engine.Vector
import com.innoveworkshop.gametest.engine.Physics
import com.innoveworkshop.gametest.engine.PhysicsBody

class Player(
    position: Vector?,
    width: Float,
    height: Float,
    deltaT: Float,
    color: Int
) : Rectangle (position, width, height, color) {

    var deltaTime: Float = deltaT
    var time = 0f

    var physicsBody: PhysicsBody? = null

    init {
        if (position != null) {
            physicsBody = PhysicsBody(
                mass = 2f,
                gravity = 2f,
                initialPosition = position,
                initialVelocity = Vector(0f, 0f),
                currentPosition = position,
                currentVelocity = Vector(0f, 0f)
            )
        }
    }

    override fun onFixedUpdate() {
        super.onFixedUpdate()

        if (!isFloored) {
            physicsBody!!.lifeTime += deltaTime
            time = physicsBody!!.lifeTime

            physicsBody = Physics().UpdatePhysicsBody(
                physicsBody = physicsBody!!,
                time = time
            )
            position = physicsBody!!.currentPosition
        }else{
            time = 0f
        }
    }

    fun ApplyForce(
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
