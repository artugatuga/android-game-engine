package com.innoveworkshop.gametest.assets

import com.innoveworkshop.gametest.engine.Circle
import com.innoveworkshop.gametest.engine.Vector
import com.innoveworkshop.gametest.engine.Physics
import com.innoveworkshop.gametest.engine.PhysicsBody

class Projectile(
    position: Vector?,
    radius: Float,
    dt: Float,
    color: Int
) : Circle (position, radius, color) {

    val deltaTime: Float = dt
    var time = 0f

    var physicsBody: PhysicsBody? = null

    init {
        if (position != null) {
            physicsBody = PhysicsBody(
                mass = 2f,
                gravity = 0f,
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
            time = physicsBody!!.lifeTime

            physicsBody = Physics().UpdatePhysicsBody(
                physicsBody = physicsBody!!,
                time = time
            )

            position = physicsBody!!.currentPosition

            if(physicsBody!!.maxLifeTime <= physicsBody!!.lifeTime){
                gameSurface!!.removeGameObject(this)
            }
        }
    }

    fun ApplyForceToProjectile(
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
