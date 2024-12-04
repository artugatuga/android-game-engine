package com.innoveworkshop.gametest.assets

import android.graphics.Color
import androidx.compose.runtime.traceEventEnd
import com.innoveworkshop.gametest.engine.GameObject
import com.innoveworkshop.gametest.engine.GameSurface
import com.innoveworkshop.gametest.engine.Vector

class PipesHandler {
    inner class Handler : GameObject() {
        fun CreatePipe(surface: GameSurface){
            val pipe = Pipe(
                Vector(surface.width.toFloat(), 100f ),
                100f,
                1000f,
                Color.rgb(255, 255, 255)
            )
            surface.addGameObject(pipe)
            pipe.ApplyForceToPipe(
                Vector(500f, 0f)
            )
        }
    }
}