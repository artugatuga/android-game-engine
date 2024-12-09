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
        if(physicsBody.timeFromForceAplied > deltaTime * 3){
            if (physicsBody.collision && physicsBody.surface!!.gameObjects[physicsBody.id] != null){
                return Touching(physicsBody)
            }
        }
        return physicsBody
    }

    fun Touching(physicsBody: PhysicsBody) : PhysicsBody{
        var i = 0
        while (i < physicsBody.surface!!.pipesInGame.size){
            var touching: Boolean

            if(physicsBody.objectTypeCir != null){
                touching = circleRectangleColision(physicsBody.objectTypeCir!!, physicsBody.surface!!.pipesInGame[i]!!.physicsBody!!.objectTypeRec!!)
            }else{
                touching = rectangleRectangleColision(physicsBody.objectTypeRec!!, physicsBody.surface!!.pipesInGame[i]!!.physicsBody!!.objectTypeRec!!)
            }

            if (touching){
                var side: Int
                if(physicsBody.objectTypeCir != null){
                    side = colisionSideCirRec(physicsBody.objectTypeCir!!, physicsBody.surface!!.pipesInGame[i]!!.physicsBody!!.objectTypeRec!!)
                }else{
                    side = colisionSideRec(physicsBody.objectTypeRec!!, physicsBody.surface!!.pipesInGame[i]!!.physicsBody!!.objectTypeRec!!)
                }

                if (side == 1){ //tops
                    physicsBody.initialVelocity.y = -physicsBody.currentVelocity.y
                    physicsBody.initialPosition = physicsBody.currentPosition
                    physicsBody.timeFromForceAplied = 0f
                }else{
                    physicsBody.initialVelocity.x = -physicsBody.currentVelocity.x
                    physicsBody.initialPosition = physicsBody.currentPosition
                    physicsBody.timeFromForceAplied = 0f
                }
                return physicsBody
            }
            i++
        }

        return physicsBody
    }

    fun onCollision(physicsBody: PhysicsBody) : Boolean{

        val body = physicsBody
        val newBody = Touching(physicsBody)

        if(newBody.timeFromForceAplied != body.timeFromForceAplied){
            return true
        }

        return false
    }

    fun colisionSideCirRec(circle: Circle, rectangle: Rectangle) : Int{
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

    fun colisionSideRec(mainRectangle: Rectangle, secRectangle: Rectangle) : Int{
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

    fun circleRectangleColision(circle: Circle, rectangle: Rectangle) : Boolean{
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