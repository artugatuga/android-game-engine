package com.innoveworkshop.gametest

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.innoveworkshop.gametest.assets.Player
import com.innoveworkshop.gametest.assets.Projectile
import com.innoveworkshop.gametest.engine.Circle
import com.innoveworkshop.gametest.engine.GameObject
import com.innoveworkshop.gametest.engine.GameSurface
import com.innoveworkshop.gametest.engine.Vector

class MainActivity : AppCompatActivity() {
    protected var gameSurface: GameSurface? = null
    protected var shoot: Button? = null

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
        shoot = findViewById<View>(R.id.shoot_button) as Button

        shoot!!.setOnClickListener {
            game!!.projectiles!![game!!.projectiles!!.lastIndex] = CreateProjectile()
        }
    }

    fun CreateProjectile(): Projectile{
        val projectile = Projectile(
            game!!.player!!.position,
            10f,
            1000f / 60f / 1000f,
            Color.rgb(128, 14, 80)
        )
        gameSurface!!.addGameObject(projectile)
        projectile.ApplyForceToProjectile(
            Vector(-200f, 0f)
        )

        return projectile
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                game!!.player!!.ApplyForce(
                    Vector(0f, 1200f)
                )
            }
        }
        return true
    }

    inner class Game : GameObject() {
        var player: Player? = null
        public var projectiles: MutableList<GameObject>? = null

        override fun onStart(surface: GameSurface?) {
            super.onStart(surface)
            player = Player(
                Vector((surface!!.width / 4).toFloat(), (surface.height / 2).toFloat()),
                100f, 100f, 1000f / 60f / 1000f, Color.rgb(128, 14, 80)
            )

            surface.addGameObject(player!!)
        }
    }
}