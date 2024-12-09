package com.innoveworkshop.gametest.engine

import androidx.compose.runtime.currentComposer
import androidx.core.view.ContentInfoCompat.Flags
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.tan

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
    @JvmField var objectTypeCir: Circle? = null,
    @JvmField var objectTypeRec: Rectangle? = null,
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
        if(physicsBody.timeFromForceAplied > deltaTime * 4){
            if (physicsBody.collision && physicsBody.surface!!.gameObjects[physicsBody.id] != null){
//            var i = 0
//            while (i < physicsBody.surface!!.pipesInGame.size){
//                val touching = colisaoCirculoRetangulo(physicsBody.objectTypeCir!!, physicsBody.surface!!.pipesInGame[i]!!.physicsBody!!.objectTypeRec!!)
//
//                if(touching){
//                    physicsBody.objectTypeCir!!.destroy()
////                    physicsBody.timeFromForceAplied = 0f
////                    physicsBody.initialVelocity.x = -physicsBody.currentVelocity.x
////                    physicsBody.initialPosition.x = physicsBody.currentPosition.x -100
////                    physicsBody.initialPosition.y = physicsBody.currentPosition.y
////                    physicsBody.airResistence = 0f
//                }
//
//                i++
//            }

                for(Circle in physicsBody.surface!!.pipesInGame){
                    var distanceX = physicsBody.objectTypeCir!!.position.x - Circle!!.position.x
                    var distanceY = physicsBody.objectTypeCir!!.position.y - Circle.position.y
                    var distance = sqrt((distanceX * distanceX) + (distanceY * distanceY))
                    if(distance <= (Circle.physicsBody!!.objectTypeCir!!.radius + physicsBody.objectTypeCir!!.radius)){
                        var mainObjectSpeed = sqrt((physicsBody.currentVelocity.x*physicsBody.currentVelocity.x)+(physicsBody.currentVelocity.y*physicsBody.currentVelocity.y))
                        var velocityX = (distanceX/distance)*mainObjectSpeed
                        var velocityY = (distanceY/distance)*mainObjectSpeed
                        var velocity = Vector(velocityX * 0.75f, velocityY * 0.75f)
                        physicsBody.initialVelocity = velocity
                        physicsBody.initialPosition = physicsBody.currentPosition
                        physicsBody.timeFromForceAplied = 0f
                        physicsBody.airResistence = 0f
                    }
                }

            }
        }
        return physicsBody
    }

    fun colisaoCirculoRetangulo(circle: Circle, rectangle: Rectangle): Boolean {
        val distanceVec = SubtractingVectors(rectangle.position, circle.position)
        val distance = MagnitudeVector(distanceVec)
        var realDistance: Float

        val alpha = atan(distanceVec.y/distanceVec.x)

        val y = distanceVec.x/ tan(alpha)

        val h = sqrt(distanceVec.x.pow(2).toDouble() + y.pow(2).toDouble())

        realDistance = (distance - circle.radius - h).toFloat()

        if(realDistance < 0){
            realDistance *= -1
        }

        if(realDistance < 30){
            return true
        }

//        if (rectangle.position.x < circle.position.x){
//            mult = -1
//        }
//        else{
//            mult = 1
//        }
//        var disX = ((rectangle.position.x + rectangle.width/2 * mult)-(circle.position.x + circle.radius * mult))
//
//        if (rectangle.position.y < circle.position.y){
//            mult = -1
//        }
//        else{
//            mult = 1
//        }
//
//        var disY = ((rectangle.position.y + rectangle.height/2 * mult)-(circle.position.y + circle.radius * mult))
//        if (disX < 0){
//            disX *= -1
//        }
//        if (disY < 0){
//            disY *= -1
//        }
//
//        if(disY <= rectangle.height){
//            return true
//        }
//        if(disX <= rectangle.width){
//            return true
//        }
        return false
    }
}