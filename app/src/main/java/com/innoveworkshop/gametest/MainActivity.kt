package com.innoveworkshop.gametest

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.innoveworkshop.gametest.assets.Player
import com.innoveworkshop.gametest.assets.Projectile
import com.innoveworkshop.gametest.engine.GameObject
import com.innoveworkshop.gametest.engine.GameSurface
import com.innoveworkshop.gametest.engine.Vector
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.innoveworkshop.gametest.assets.BasicInfo
import com.innoveworkshop.gametest.assets.ProjectilesHandler
import com.innoveworkshop.gametest.assets.ProjectilesHandler.Handler

class MainActivity : AppCompatActivity() {
    protected var gameSurface: GameSurface? = null
    protected var shoot: Button? = null

    protected var game: Game? = null
    protected var projectilesHandler: Handler? = null

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameSurface = findViewById<View>(R.id.gameSurface) as GameSurface
        game = Game()
        projectilesHandler = Handler()
        gameSurface!!.setRootGameObject(game)

        setupControls()
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun setupControls() {
        shoot = findViewById<View>(R.id.shoot_button) as Button

        shoot!!.setOnClickListener {
            projectilesHandler!!.projectile.value = BasicInfo(game!!.player!!.position)
            projectilesHandler!!.create.value = true
        }
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

    class Game : GameObject() {
        var player: Player? = null

        override fun onStart(surface: GameSurface?) {
            super.onStart(surface)
            player = Player(
                Vector((surface!!.width / 4).toFloat(), (surface.height / 2).toFloat()),
                100f, 100f, Color.rgb(128, 14, 80)
            )
            surface.addGameObject(player!!)
        }

        fun DeleteProjectile(int: Int){

        }
    }
}
