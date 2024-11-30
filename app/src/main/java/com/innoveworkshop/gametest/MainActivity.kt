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

class MainActivity : AppCompatActivity() {
    protected var gameSurface: GameSurface? = null
    protected var shoot: Button? = null

    protected var game: Game? = null

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameSurface = findViewById<View>(R.id.gameSurface) as GameSurface
        game = Game()
        gameSurface!!.setRootGameObject(game)

        setupControls()
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun setupControls() {
        shoot = findViewById<View>(R.id.shoot_button) as Button

        shoot!!.setOnClickListener {
            var i = 0

            while (i < game!!.projectiles.size){
                if(game!!.projectiles[i] == null){
                    game!!.projectiles[i] = CreateProjectile(i)
                    break
                }else{
                    i++
                }
            }
        }
    }

    fun CreateProjectile(id: Int): Projectile{
        val projectile = Projectile(
            game!!.player!!.position,
            10f,
            Color.rgb(128, 14, 80),
            id
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

    fun DeleteProjectile(int: Int){
        Log.d("ERROR", "NOT HERE")
        val projectile = game!!.projectiles[int]
        Log.d("ERROR", "NOT HERE ALSO")
        if(projectile != null){
            Log.d("ERROR", "NOPE")
            game!!.projectiles[int] = null
            Log.d("ERROR", "NAH AHN")
            gameSurface!!.removeGameObject(projectile)
            Log.d("ERROR", "NICLES BATATOIDES")
            projectile.isDestroyed = true
            Log.d("ERROR", "SO PODE SER AQUI")
        }
    }

    class Game : GameObject() {
        var player: Player? = null
        var projectiles = MutableList<GameObject?>(50) { null }

        override fun onStart(surface: GameSurface?) {
            super.onStart(surface)
            player = Player(
                Vector((surface!!.width / 4).toFloat(), (surface.height / 2).toFloat()),
                100f, 100f, Color.rgb(128, 14, 80)
            )
            surface.addGameObject(player!!)
        }

        fun DeleteProjectile(int: Int){
            MainActivity().DeleteProjectile(int)
        }
    }
}