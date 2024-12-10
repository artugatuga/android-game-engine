package com.innoveworkshop.gametest.engine

import androidx.compose.runtime.currentComposer
import androidx.core.view.ContentInfoCompat.Flags
import kotlin.math.atan
import kotlin.math.min
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
    @JvmField var colliding: PhysicsBody? = null,
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

        physicsBody.currentVelocity = Vector(
            physicsBody.initialVelocity.x + (objectAirResistence * time),
            physicsBody.initialVelocity.y + (objectGravity * time)
        )

        if (physicsBody.currentVelocity.x > 0f){
            objectAirResistence *= -1
        }

        physicsBody.currentPosition = Vector(
            physicsBody.initialPosition.x + (physicsBody.initialVelocity.x * time) + (objectAirResistence/2) * (time * time),
            physicsBody.initialPosition.y + (physicsBody.initialVelocity.y * time) + (objectGravity) * (time * time)
        )

        return physicsBody
    }

    fun CollisionDetection(
        physicsBody: PhysicsBody,
    ) : PhysicsBody{
        if(physicsBody.timeFromForceAplied > deltaTime * 2){
            if (physicsBody.collision && physicsBody.surface!!.gameObjects[physicsBody.id] != null){
                return Touching(physicsBody)
            }
        }
        return physicsBody
    }

    fun Touching(physicsBody: PhysicsBody) : PhysicsBody{
        var i = 0
//        while (i < physicsBody.surface!!.pipesInGame.size){
//            if(physicsBody != physicsBody.surface!!.pipesInGame[i]!!.physicsBody!!){
//                var touching: Boolean
//
//                if(physicsBody.objectTypeCir != null){
//                    touching = circleRectangleColision(physicsBody.objectTypeCir!!, physicsBody.surface!!.pipesInGame[i]!!.physicsBody!!.objectTypeRec!!)
//                }else{
//                    touching = rectangleRectangleColision(physicsBody.objectTypeRec!!, physicsBody.surface!!.pipesInGame[i]!!.physicsBody!!.objectTypeRec!!)
//                }
//
//                if (touching){
//                    val side: Int
//
//                    if(physicsBody.objectTypeCir != null){
//                        side = colisionSideCirRec(physicsBody.objectTypeCir!!, physicsBody.surface!!.pipesInGame[i]!!.physicsBody!!.objectTypeRec!!)
//                    }else{
//                        side = colisionSideRec(physicsBody.objectTypeRec!!, physicsBody.surface!!.pipesInGame[i]!!.physicsBody!!.objectTypeRec!!)
//                    }
//
//                    if (side == 1){ //tops
//                        physicsBody.initialVelocity.y = -physicsBody.currentVelocity.y
//                        physicsBody.initialPosition = physicsBody.currentPosition
//                        physicsBody.timeFromForceAplied = 0f
//                    }else{
//                        physicsBody.initialVelocity.x = -physicsBody.currentVelocity.x
//                        physicsBody.initialPosition = physicsBody.currentPosition
//                        physicsBody.timeFromForceAplied = 0f
//                    }
//                    physicsBody.colliding = physicsBody.surface!!.pipesInGame[i]!!.physicsBody!!
//
//                    return physicsBody
//                }else{
//                    physicsBody.colliding = null
//                }
//            }
//
//            i++
//        }

        while (i < physicsBody.surface!!.gameObjects.size){
            if(physicsBody.surface!!.gameObjects[i] != null){
                if(physicsBody.id != physicsBody.surface!!.gameObjects[i]!!.id){
                    var touching: Boolean
                    val secPhysicsBody = physicsBody.surface!!.gameObjects[i]!!.physicsBody

                    if(physicsBody.objectTypeCir != null){
                        if(secPhysicsBody!!.objectTypeCir != null){
                            touching = circleCircleCollision(physicsBody.objectTypeCir!!, secPhysicsBody.objectTypeCir!!)
                        }else{
                            touching = circleRectangleCollision(physicsBody.objectTypeCir!!, secPhysicsBody.objectTypeRec!!)
                        }
                    }else{
                        if(secPhysicsBody!!.objectTypeCir != null){
                            touching = circleRectangleCollision(secPhysicsBody.objectTypeCir!!, physicsBody.objectTypeRec!!)
                        }else{
                            touching = rectangleRectangleColision(physicsBody.objectTypeRec!!, secPhysicsBody.objectTypeRec!!)
                        }
                    }

                    if (touching){
                        val side: Int

                        if(physicsBody.objectTypeCir != null){
                            if(secPhysicsBody.objectTypeCir != null){
                                side = 3
                            }else{
                                side = collisionSideCirRec(physicsBody.objectTypeCir!!, secPhysicsBody.objectTypeRec!!)
                            }
                        }else{
                            if(secPhysicsBody.objectTypeCir != null){
                                side = collisionSideCirRec(secPhysicsBody.objectTypeCir!!, physicsBody.objectTypeRec!!)
                            }else{
                                side = collisionSideRec(physicsBody.objectTypeRec!!, secPhysicsBody.objectTypeRec!!)
                            }
                        }

                        if (side == 1){ //tops
                            physicsBody.initialVelocity.y = -physicsBody.currentVelocity.y
                            physicsBody.initialPosition = physicsBody.currentPosition
                            physicsBody.timeFromForceAplied = 0f
                        }else if (side == 2){ // Sides
                            physicsBody.initialVelocity.x = -physicsBody.currentVelocity.x
                            physicsBody.initialPosition = physicsBody.currentPosition
                            physicsBody.timeFromForceAplied = 0f
                        }else{
                            val distanceVec = SubtractingVectors(physicsBody.currentPosition, secPhysicsBody.currentPosition)
                            val distance = MagnitudeVector(distanceVec)
                            val mainObjectSpeed = sqrt((physicsBody.currentVelocity.x * physicsBody.currentVelocity.x) + (physicsBody.currentVelocity.y * physicsBody.currentVelocity.y))
                            val velocityX = (distanceVec.x/distance) * mainObjectSpeed
                            val velocityY = (distanceVec.y/distance) * mainObjectSpeed
                            val velocity = Vector(velocityX * 0.75f, velocityY * 0.75f)

                            physicsBody.initialVelocity = velocity
                            physicsBody.initialPosition = physicsBody.currentPosition
                            physicsBody.timeFromForceAplied = 0f
                        }

                        physicsBody.colliding = physicsBody.surface!!.gameObjects[i]!!.physicsBody!!

                        return physicsBody
                    }else{
                        physicsBody.colliding = null
                    }
                }
            }

            i++
        }

        return physicsBody
    }

    fun collisionSideCirRec(circle: Circle, rectangle: Rectangle) : Int{
        var distXLeft = (circle.position.x + circle.radius) - (rectangle.position.x - rectangle.width/2)
        var distXRight = (circle.position.x - circle.radius) - (rectangle.position.x + rectangle.width/2)
        var distYTop = (circle.position.y + circle.radius) - (rectangle.position.y - rectangle.height/2)
        var distYBottom = (circle.position.y - circle.radius) - (rectangle.position.y + rectangle.height/2)

        if(distXLeft < 0){
            distXLeft *= -1
        }
        if(distXRight < 0){
            distXRight *= -1
        }
        if(distYTop < 0){
            distYTop *= -1
        }
        if(distYBottom < 0){
            distYBottom *= -1
        }

        val minDistX = min(distXLeft, distXRight)
        val minDistY = min(distYTop, distYBottom)

        if(minDistX < minDistY){
            return 2
        }else{
            return 1
        }
    }

    fun collisionSideRec(mainRectangle: Rectangle, secRectangle: Rectangle) : Int{
        var distXLeft = (mainRectangle.position.x + mainRectangle.width/2) - (secRectangle.position.x - secRectangle.width/2)
        var distXRight = (mainRectangle.position.x - mainRectangle.width/2) - (secRectangle.position.x + secRectangle.width/2)
        var distYTop = (mainRectangle.position.y + mainRectangle.height/2) - (secRectangle.position.y - secRectangle.height/2)
        var distYBottom = (mainRectangle.position.y - mainRectangle.height/2) - (secRectangle.position.y + secRectangle.height/2)

        if(distXLeft < 0){
            distXLeft *= -1
        }
        if(distXRight < 0){
            distXRight *= -1
        }
        if(distYTop < 0){
            distYTop *= -1
        }
        if(distYBottom < 0){
            distYBottom *= -1
        }

        val minDistX = min(distXLeft, distXRight)
        val minDistY = min(distYTop, distYBottom)

        if(minDistX < minDistY){
            return 2
        }else{
            return 1
        }
    }

    fun circleCircleCollision(mainCircle: Circle, secCircle: Circle) : Boolean{
        val distanceVec = SubtractingVectors(mainCircle.position, secCircle.position)
        val distance = MagnitudeVector(distanceVec)
        val distanceLeft = distance - mainCircle.radius - secCircle.radius

        if (distanceLeft < 20f){
            return true
        }

        return false
    }

    fun circleRectangleCollision(circle: Circle, rectangle: Rectangle) : Boolean{
        var passingX = false
        var passingY = false

        if(circle.position.x > rectangle.position.x - rectangle.width/2 && circle.position.x < rectangle.position.x + rectangle.width/2){
            passingX = true
        }

        if(circle.position.y > rectangle.position.y - rectangle.height/2 && circle.position.y < rectangle.position.y + rectangle.height/2){
            passingY = true
        }
        if (passingX && passingY){
            return true
        }

        return false
    }

    fun rectangleRectangleColision(mainRectangle: Rectangle, secRectangle: Rectangle) : Boolean{
        var passingX = false
        var passingY = false

        if(mainRectangle.position.x + mainRectangle.width/2 > secRectangle.position.x - secRectangle.width/2 && mainRectangle.position.x - mainRectangle.width/2 < secRectangle.position.x + secRectangle.width/2){
            passingX = true
        }

        if(mainRectangle.position.y + mainRectangle.height/2 > secRectangle.position.y - secRectangle.height/2 && mainRectangle.position.y - mainRectangle.height/2 < secRectangle.position.y + secRectangle.height/2){
            passingY = true
        }
        if (passingX && passingY){
            return true
        }

        return false
    }
}