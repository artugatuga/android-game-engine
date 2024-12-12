package com.innoveworkshop.angryflappingbird.assets

import com.innoveworkshop.angryflappingbird.engine.GameSurface
import com.innoveworkshop.angryflappingbird.engine.Rectangle
import com.innoveworkshop.angryflappingbird.engine.Vector
import com.innoveworkshop.angryflappingbird.engine.Physics
import com.innoveworkshop.angryflappingbird.engine.PhysicsBody

class Player(
    position: Vector?,
    width: Float,
    height: Float,
    color: Int,
    val surface: GameSurface
) : Rectangle (position, width, height, color) {

    var died = false

    override fun onStart(surface: GameSurface?) {
        super.onStart(surface)
        Reset()
    }

    override fun onFixedUpdate() {
        super.onFixedUpdate()

        if (!isDestroyed) {
            physicsBody = Physics().UpdatePhysicsBody(
                physicsBody = physicsBody!!
            )
            position = physicsBody!!.currentPosition

            if(physicsBody!!.colliding != null){
                isDestroyed = true
            }
            else if (surface.height < position.y + height/2){
                isDestroyed = true
            }
        }
    }

    fun Reset(){
        died = false
        isDestroyed = false
        val startPos = Vector((surface.width / 4).toFloat(), (surface.height / 2).toFloat())
        physicsBody = PhysicsBody(
            id = id!!,
            collision = true,
            mass = 2f,
            gravity = 3f,
            airResistence = 0f,
            initialPosition = startPos,
            initialVelocity = Vector(0f, 0f),
            currentPosition = startPos,
            currentVelocity = Vector(0f, 0f),
            surface = this.surface,
            objectTypeRec = this
        )
    }

    fun ApplyForce(
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
