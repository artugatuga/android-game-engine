package com.innoveworkshop.angryflappingbird.engine

import android.graphics.Canvas
import android.graphics.Paint

open class Circle(
    position: Vector?,
    var radius: Float,
    color: Int
) : GameObject(position!!), Caged {

    // Set up the paint.
    var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.color = color
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawCircle(position.x, position.y, radius, paint)
    }

    override fun hitLeftWall(): Boolean {
        return (position.x - radius) <= gameSurface!!.width
    }

    override fun hitRightWall(): Boolean {
        return (position.x + radius) >= gameSurface!!.width
    }

    override val isFloored: Boolean
        get() = (position.y + radius) >= gameSurface!!.height
}
