package com.innoveworkshop.gametest.engine

import android.util.Log

class PhysicsBody(
    @JvmField var mass: Float,
    @JvmField var gravity: Float,
    @JvmField var initialPosition: Vector,
    @JvmField var initialVelocity: Vector,
    @JvmField var currentPosition: Vector,
    @JvmField var currentVelocity: Vector,
)

class Physics {
    var defaultGravity = 98.1f

    fun ApplyForce(
        force: Vector = Vector(1f,1f),
        physicsBody: PhysicsBody,
    ):Vector {
        val objectGravity = Vector(
            0f,
            defaultGravity * physicsBody.gravity
        )

        val gravityForce = Vector(
            0f,
            physicsBody.mass * objectGravity.y
        )

        val finalForce = Vector(
            0 - force.x,
            gravityForce.y - force.y
        )

        val acceleration = Vector(
            finalForce.x / physicsBody.mass,
            finalForce.y / physicsBody.mass
        )

        if(force.x == 0f)
        {
            acceleration.x += physicsBody.initialVelocity.x
        }

        if(force.y == 0f)
        {
            acceleration.y += physicsBody.initialVelocity.y
        }

        Log.d("CURRENT VELOCITY X",  acceleration.x.toString())
        Log.d("CURRENT VELOCITY Y",  acceleration.y.toString())

        return acceleration
    }

    fun UpdatePhysicsBody(
        physicsBody: PhysicsBody,
        time: Float
    ):PhysicsBody {
        val objectGravity = defaultGravity * physicsBody.gravity

        physicsBody.currentVelocity = Vector(
            physicsBody.initialVelocity.x / time,
            physicsBody.initialVelocity.y * time
        )

        physicsBody.currentPosition = Vector(
            physicsBody.initialPosition.x + physicsBody.currentVelocity.x * (time * time),
            physicsBody.initialPosition.y + physicsBody.currentVelocity.y + (objectGravity) * (time * time)
        )

        physicsBody.initialVelocity = Vector(
            physicsBody.initialVelocity.x ,
            physicsBody.initialVelocity.y
        )

        return physicsBody
    }
}