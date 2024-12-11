package com.innoveworkshop.gametest

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.innoveworkshop.gametest.assets.PipesHandler
import com.innoveworkshop.gametest.assets.Player
import com.innoveworkshop.gametest.engine.GameObject
import com.innoveworkshop.gametest.engine.GameSurface
import com.innoveworkshop.gametest.engine.Vector
import com.innoveworkshop.gametest.assets.ProjectilesHandler
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    protected var gameSurface: GameSurface? = null
    protected var shoot: Button? = null
    var score = 0
    var highScore = 0
    var scoreText: TextView? = null
    var highestScoreText: TextView? = null

    protected var game: Game? = null
    protected var projectilesHandler: ProjectilesHandler? = null

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameSurface = findViewById<View>(R.id.gameSurface) as GameSurface
        game = Game()
        projectilesHandler = ProjectilesHandler()
        gameSurface!!.setRootGameObject(game)
        scoreText = findViewById<TextView>(R.id.score_text)
        highestScoreText = findViewById<TextView>(R.id.high_score)
        scoreText!!.text = "Score: $score"
        highestScoreText!!.text = "Highest Score: $score"
        setupControls()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun setupControls() {
        shoot = findViewById<Button>(R.id.shoot_button)

        shoot!!.setOnClickListener {
            projectilesHandler!!.InitialPos.value = game!!.player!!.position
            projectilesHandler!!.Handler().CreateProjectile(gameSurface!!)
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_UP -> {
                game!!.player!!.ApplyForce(
                    Vector(0f, 1200f)
                )
            }
        }
        return true
    }

    inner class Game : GameObject() {
        var player: Player? = null
        var time: Float = 3f

        override fun onStart(surface: GameSurface?) {
            super.onStart(surface)
            player = Player(
                Vector((surface!!.width / 4).toFloat(), (surface.height / 2).toFloat()),
                100f,
                100f,
                Color.rgb(128, 14, 80),
                surface
            )
            surface.addGameObject(player!!)
        }

        @SuppressLint("SetTextI18n")
        override fun onFixedUpdate() {
            super.onFixedUpdate()
            time = PipesHandler().Handler().onFixedUpdate(gameSurface!!, time, deltaTime)

            if(!player!!.isDestroyed){
                var i = 0
                while (i < gameSurface!!.pipesInGame.size){
                    score = gameSurface!!.pipesInGame[i]!!.CheckPlayerPos(score, player!!.position)
                    scoreText!!.text = "Score: $score"
                    i++
                }
            }else{
                RestartGame()
            }
        }

        fun RestartGame(){
            player!!.Reset()
            time = 3f
            SaveGame()
            var i = 1
            while (i < gameSurface!!.gameObjects.size){
                if(gameSurface!!.gameObjects[i] != null){
                    gameSurface!!.gameObjects[i]!!.destroy()
                    gameSurface!!.gameObjects[i]!!.isDestroyed = true
                }
                i++
            }
        }

        @SuppressLint("SetTextI18n")
        fun SaveGame(){
            if(highScore < score){
                highestScoreText!!.text = "Highest Score: $score"
                highScore = score
            }
            score = 0
            scoreText!!.text = "Score: $score"
        }
    }
}
