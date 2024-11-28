package com.innoveworkshop.gametest

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.innoveworkshop.gametest.assets.DroppingRectangle
import com.innoveworkshop.gametest.engine.Circle
import com.innoveworkshop.gametest.engine.GameObject
import com.innoveworkshop.gametest.engine.GameSurface
import com.innoveworkshop.gametest.engine.Rectangle
import com.innoveworkshop.gametest.engine.Vector
import com.innoveworkshop.gametest.engine.Physics

class MainActivity : AppCompatActivity() {
    protected var gameSurface: GameSurface? = null
    protected var jumpButton: Button? = null

    protected var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameSurface = findViewById<View>(R.id.gameSurface) as GameSurface
        game = Game()
        gameSurface!!.setRootGameObject(game)

        setupControls()
    }

    private fun setupControls() {
        jumpButton = findViewById<View>(R.id.up_button) as Button
        jumpButton!!.setOnClickListener {
            game!!.player!!.ApplyForce()
        }
    }

    inner class Game : GameObject() {
        var circle: Circle? = null
        var player: DroppingRectangle? = null

        override fun onStart(surface: GameSurface?) {
            super.onStart(surface)

            circle = Circle(
                (surface!!.width / 2).toFloat(),
                (surface.height / 2).toFloat(),
                100f,
                Color.RED
            )

            player = DroppingRectangle(
                Vector((surface.width / 3).toFloat(), (surface.height / 3).toFloat()),
                100f, 100f, 1000f / 60f / 1000f, Color.rgb(128, 14, 80)
            )

            surface.addGameObject(circle!!)

            surface.addGameObject(player!!)
        }

        override fun onFixedUpdate() {
            super.onFixedUpdate()

            if (!circle!!.isFloored && !circle!!.hitRightWall() && !circle!!.isDestroyed) {
                //circle!!.setPosition(circle!!.position.x + 1, circle!!.position.y + 1)
            } else {
                circle!!.destroy()
            }
        }
    }
}