package com.innoveworkshop.gametest.assets

import android.annotation.SuppressLint
import com.innoveworkshop.gametest.MainActivity
import com.innoveworkshop.gametest.engine.GameSurface
import com.innoveworkshop.gametest.engine.MagnitudeVector
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
    var scored = false

    override fun onStart(surface: GameSurface?) {
        super.onStart(surface)
        physicsBody = PhysicsBody(
            id = id!!,
            collision = false,
            mass = 2f,
            gravity = 0f,
            airResistence = 0f,
            initialPosition = position,
            initialVelocity = Vector(0f, 0f),
            currentPosition = position,
            currentVelocity = Vector(0f, 0f),
            maxLifeTime = 8,
            surface = this.surface,
            objectTypeRec = this
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

    @SuppressLint("SetTextI18n")
    fun CheckPlayerPos(score: Int, playerPos: Vector) : Int{
        if(!scored && !isDestroyed){
            var currentScore = score

            if(position.x + width/2 < playerPos.x){
                currentScore++
                scored = true
                return currentScore
            }
        }
        return score
    }
}