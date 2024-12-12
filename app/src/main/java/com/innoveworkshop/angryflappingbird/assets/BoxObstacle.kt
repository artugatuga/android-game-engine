package com.innoveworkshop.angryflappingbird.assets

import com.innoveworkshop.angryflappingbird.engine.GameSurface
import com.innoveworkshop.angryflappingbird.engine.Vector
import com.innoveworkshop.angryflappingbird.engine.Physics
import com.innoveworkshop.angryflappingbird.engine.PhysicsBody
import com.innoveworkshop.angryflappingbird.engine.Rectangle

class BoxObstacle(
    position: Vector?,
    width: Float,
    height: Float,
    color: Int,
    val surface: GameSurface
) : Rectangle(position, width, height, color) {

    override fun onStart(surface: GameSurface?) {
        super.onStart(surface)
        name = "Obstacle"
        physicsBody = PhysicsBody(
            id = id!!,
            collision = true,
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

        if (!isDestroyed) {
            physicsBody = Physics().UpdatePhysicsBody(
                physicsBody = physicsBody!!
            )
            position = physicsBody!!.currentPosition

            if(physicsBody!!.colliding != null){
                destroy()
            }
        }
    }

    fun ApplyForceToObstacle(
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
