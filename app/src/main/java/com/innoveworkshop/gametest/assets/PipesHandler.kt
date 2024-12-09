package com.innoveworkshop.gametest.assets

import android.graphics.Color
import androidx.compose.runtime.traceEventEnd
import com.innoveworkshop.gametest.engine.GameObject
import com.innoveworkshop.gametest.engine.GameSurface
import com.innoveworkshop.gametest.engine.Vector
import kotlin.random.Random

class PipesHandler {
    inner class Handler {
        val pipesInGame = true
        var timeToSpawnAnother = 3f

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
        }
    }
}