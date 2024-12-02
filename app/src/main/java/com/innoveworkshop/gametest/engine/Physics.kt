package com.innoveworkshop.gametest.engine

import android.util.Log
import androidx.compose.runtime.currentComposer

class PhysicsBody(
    @JvmField var mass: Float,
    @JvmField var gravity: Float,
    @JvmField var airResistence: Float,
    @JvmField var initialPosition: Vector,
    @JvmField var initialVelocity: Vector,
    @JvmField var currentPosition: Vector,
    @JvmField var currentVelocity: Vector,
    @JvmField var lifeTime: Float = 0f,
    @JvmField var maxLifeTime: Int = 0,
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
            0f - force.x,
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

        return acceleration
    }

    fun UpdatePhysicsBody(
        physicsBody: PhysicsBody,
        time: Float
    ):PhysicsBody {
        val objectGravity = defaultGravity * physicsBody.gravity
        var objectAirResistence = physicsBody.airResistence

        if (physicsBody.initialVelocity.x > 0f){
            objectAirResistence *= -1
        }

        physicsBody.currentVelocity = Vector(
            physicsBody.initialVelocity.x + (objectAirResistence * time),
            physicsBody.initialVelocity.y + (objectGravity * time)
        )

        physicsBody.currentPosition = Vector(
            physicsBody.initialPosition.x + (physicsBody.initialVelocity.x * time) + (objectAirResistence/2) * (time * time),
            physicsBody.initialPosition.y + (physicsBody.initialVelocity.y * time) + (objectGravity) * (time * time)
        )

        Log.d("INSTANTANEOUS TIME", time.toString())
        Log.d("INSTANTANEOUS GRAVITY", objectGravity.toString())
        Log.d("INSTANTANEOUS INITIAL VELOCITY", physicsBody.initialVelocity.y.toString())
        Log.d("INSTANTANEOUS VELOCITY", physicsBody.currentVelocity.y.toString())

        return physicsBody
    }
}