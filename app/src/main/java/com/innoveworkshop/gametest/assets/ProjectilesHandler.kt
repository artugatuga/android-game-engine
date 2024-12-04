package com.innoveworkshop.gametest.assets

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import com.innoveworkshop.gametest.engine.GameObject
import com.innoveworkshop.gametest.engine.GameSurface
import com.innoveworkshop.gametest.engine.Vector

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
class ProjectilesHandler {
    var InitialPos = mutableStateOf(Vector(0f,0f))

    inner class Handler : GameObject() {
        fun CreateProjectile(surface: GameSurface){
            val projectile = Projectile(
                InitialPos.value,
                10f,
                Color.rgb(128, 14, 80)
            )
            surface.addGameObject(projectile)
            projectile.ApplyForceToProjectile(
                Vector(-1000f, 1200f)
            )
        }
    }
}

