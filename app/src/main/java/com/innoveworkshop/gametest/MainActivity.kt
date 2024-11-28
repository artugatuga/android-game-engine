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
    protected var leftButton: Button? = null
    protected var rightButton: Button? = null

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
        leftButton = findViewById<View>(R.id.left_button) as Button
        rightButton = findViewById<View>(R.id.right_button) as Button

        jumpButton!!.setOnClickListener {
            game!!.player!!.ApplyForce(
                Vector(0f, 1200f)
            )
        }

        leftButton!!.setOnClickListener {
            game!!.player!!.ApplyForce(
                Vector(500f, 0f)
            )
        }

        rightButton!!.setOnClickListener {
            game!!.player!!.ApplyForce(
                Vector(-500f, 0f)
            )
        }
    }

    inner class Game : GameObject() {
        var player: DroppingRectangle? = null

        override fun onStart(surface: GameSurface?) {
            super.onStart(surface)
            player = DroppingRectangle(
                Vector((surface!!.width / 2).toFloat(), (surface.height / 2).toFloat()),
                100f, 100f, 1000f / 60f / 1000f, Color.rgb(128, 14, 80)
            )

            surface.addGameObject(player!!)
        }
    }
}