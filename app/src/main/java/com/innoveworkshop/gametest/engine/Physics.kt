package com.innoveworkshop.gametest.engine

import com.innoveworkshop.gametest.engine.GameSurface

class PhysicsBody(
    @JvmField var id: Int,
    @JvmField var collision: Boolean,
    @JvmField var mass: Float,
    @JvmField var gravity: Float,
    @JvmField var airResistence: Float,
    @JvmField var initialPosition: Vector,
    @JvmField var initialVelocity: Vector,
    @JvmField var currentPosition: Vector,
    @JvmField var currentVelocity: Vector,
    @JvmField var timeFromForceAplied: Float = 0f,
    @JvmField var lifeTime: Float = 0f,
    @JvmField var maxLifeTime: Int = 0,
    @JvmField var surface: GameSurface? = null,
)

class Physics {
    var defaultGravity = 98.1f
    var deltaTime = 1/60f

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

        physicsBody.timeFromForceAplied = 0f

        return acceleration
    }

    fun UpdatePhysicsBody(
        physicsBody: PhysicsBody
    ) : PhysicsBody {
        physicsBody.lifeTime += deltaTime
        physicsBody.timeFromForceAplied += deltaTime

        return PositionCalculus(CollisionDetection(physicsBody), physicsBody.timeFromForceAplied)
    }

    fun PositionCalculus(
        physicsBody: PhysicsBody,
        time: Float
    ) : PhysicsBody{
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

        return physicsBody
    }

    fun CollisionDetection(
        physicsBody: PhysicsBody,
    ) : PhysicsBody{

        if (physicsBody.collision && physicsBody.surface!!.gameObjects[physicsBody.id] != null){
            var i = 0
            val mainPosition = physicsBody.surface!!.gameObjects[physicsBody.id]!!.position
            while (i < physicsBody.surface!!.gameObjects.size){
                if(physicsBody.surface!!.gameObjects[i] != null){
                    if(physicsBody.surface!!.gameObjects[i]!!.id != physicsBody.id ){
                        val secPosition = physicsBody.surface!!.gameObjects[i]!!.position
                        val vecBetween = SubtractingVectors(mainPosition, secPosition)
                        val dist = MagnitudeVector(vecBetween)

                        if(mainPosition.x >= secPosition.x && i != 0){
                            physicsBody.timeFromForceAplied = 0f
                            physicsBody.initialVelocity.x = -physicsBody.currentVelocity.x
                            physicsBody.initialPosition.x = physicsBody.currentPosition.x - 100
                            physicsBody.initialPosition.y = physicsBody.currentPosition.y
                            physicsBody.airResistence = 0f
                            break
                        }
                    }
                }
                i++
            }
        }

        return physicsBody
    }
}