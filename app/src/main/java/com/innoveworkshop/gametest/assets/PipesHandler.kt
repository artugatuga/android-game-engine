package com.innoveworkshop.gametest.assets

import android.graphics.Color
import androidx.compose.runtime.traceEventEnd
import com.innoveworkshop.gametest.engine.GameObject
import com.innoveworkshop.gametest.engine.GameSurface
import com.innoveworkshop.gametest.engine.Vector
import org.w3c.dom.Text
import kotlin.random.Random

class PipesHandler {
    inner class Handler {
        val pipesInGame = true
        var timeToSpawnAnother = 4f

        fun onFixedUpdate(surface: GameSurface, time: Float, dt: Float) : Float{
            var currentTime = time
            if (pipesInGame && currentTime <= 0){
                PipesHandler().Handler().CreatePipes(surface)
                currentTime = timeToSpawnAnother
            }
            currentTime -= dt
            return currentTime
        }

        fun CreatePipes(surface: GameSurface){
            val spaceBetween = 500f
            val upPipeHeight = (700..(surface.height.toFloat() - 700).toInt()).random().toFloat()
            val downPipeHeight = surface.height.toFloat() - spaceBetween - upPipeHeight

            val upPipe = Pipe(
                Vector(surface.width.toFloat(), upPipeHeight/2),
                100f,
                upPipeHeight,
                Color.rgb(255, 255, 255),
                surface
            )

            val downPipe = Pipe(
                Vector(surface.width.toFloat(), surface.height.toFloat() - downPipeHeight/2),
                100f,
                downPipeHeight,
                Color.rgb(255, 255, 255),
                surface
            )

            surface.addPipe(upPipe)
            surface.addPipe(downPipe)
            surface.addGameObject(upPipe)
            surface.addGameObject(downPipe)

            upPipe.ApplyForceToPipe(
                Vector(300f, 0f)
            )
            downPipe.ApplyForceToPipe(
                Vector(300f, 0f)
            )

            CreateRandomizedObstacles(surface, upPipe, spaceBetween)
        }

        fun CreateRandomizedObstacles(surface: GameSurface, upPipe: Pipe, spaceBetween: Float) {
            val num = (0..4).random().toFloat()
            var i = 0
            while (i < num){
                val obstacle = BoxObstacle(
                    Vector(surface.width.toFloat(), upPipe.position.y + upPipe.height/2 + (spaceBetween * (i+1))/(num + 1)),
                    80f, 80f,
                    Color.rgb(255, 0, 255),
                    surface
                )

                surface.addPipe(obstacle)

                surface.addGameObject(obstacle)

                obstacle.ApplyForceToObstacle(
                    Vector(300f, 0f)
                )

                i++
            }
        }
    }
}