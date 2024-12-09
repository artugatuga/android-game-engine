package com.innoveworkshop.gametest.engine

import android.graphics.Canvas

abstract class GameObject {
    var position: Vector
    protected var deltaTime = 1/60f
    protected var lifeTime = 0f
    var id: Int? = null
    var physicsBody: PhysicsBody? = null

    protected var gameSurface: GameSurface? = null
    var isDestroyed: Boolean = false

    constructor(position: Vector) {
        this.position = position
    }

    @JvmOverloads
    constructor(x: Float = 0f, y: Float = 0f) {
        position = Vector(x, y)
    }

    fun setPosition(x: Float, y: Float) {
        position.x = x
        position.y = y
    }

    fun destroy() {
        //gameSurface!!.removeGameObject(this)
        isDestroyed = true
    }

    open fun onStart(surface: GameSurface?) {
        this.gameSurface = surface
    }

    open fun onFixedUpdate() {
        lifeTime += deltaTime

        if (isDestroyed) {
            setPosition(-10000f, -10000f)
        }
    }

    open fun onDraw(canvas: Canvas?) {}
}
