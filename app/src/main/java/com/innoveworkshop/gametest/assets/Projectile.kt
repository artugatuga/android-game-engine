package com.innoveworkshop.gametest.assets

import com.innoveworkshop.gametest.engine.Circle
import com.innoveworkshop.gametest.engine.GameSurface
import com.innoveworkshop.gametest.engine.Vector
import com.innoveworkshop.gametest.engine.Physics
import com.innoveworkshop.gametest.engine.PhysicsBody

class Projectile(
    position: Vector?,
    radius: Float,
    color: Int,
    val surface: GameSurface
) : Circle (position, radius, color) {

    override fun onStart(surface: GameSurface?) {
        super.onStart(surface)
        physicsBody = PhysicsBody(
            id = id!!,
            collision = true,
            mass = 2f,
            gravity = 2f,
            airResistence = 150f,
            initialPosition = Vector(0f, 0f),
            initialVelocity = Vector(0f, 0f),
            currentPosition = Vector(0f, 0f),
            currentVelocity = Vector(0f, 0f),
            maxLifeTime = 10,
            surface = this.surface,
            objectTypeCir = this
        )
    }

    override fun onFixedUpdate() {
        super.onFixedUpdate()

        if(!isDestroyed && physicsBody != null){
            physicsBody = Physics().UpdatePhysicsBody(
                physicsBody = physicsBody!!
            )

            position = physicsBody!!.currentPosition

            if(physicsBody!!.maxLifeTime <= physicsBody!!.lifeTime){
                destroy()
            }
        }
    }

    fun ApplyForceToProjectile(
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
