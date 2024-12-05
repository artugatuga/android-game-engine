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
    color: Int,
    val surface: GameSurface
) : Rectangle (position, width, height, color) {
    var physicsBody: PhysicsBody? = null

    override fun onStart(surface: GameSurface?) {
        super.onStart(surface)
        physicsBody = PhysicsBody(
            id = id!!,
            collision = false,
            mass = 2f,
            gravity = 0f,
            airResistence = 1f,
            initialPosition = position,
            initialVelocity = Vector(0f, 0f),
            currentPosition = position,
            currentVelocity = Vector(0f, 0f),
            maxLifeTime = 5,
            surface = this.surface
        )
    }

    override fun onFixedUpdate() {
        super.onFixedUpdate()

        if(!isDestroyed){

            physicsBody = Physics().UpdatePhysicsBody(
                physicsBody = physicsBody!!
            )

            position = physicsBody!!.currentPosition

            if(physicsBody!!.maxLifeTime <= physicsBody!!.lifeTime){
                destroy()
            }
        }
    }

    fun ApplyForceToPipe(
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