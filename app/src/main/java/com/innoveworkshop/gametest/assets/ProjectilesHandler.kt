package com.innoveworkshop.gametest.assets

import android.graphics.Color
import android.icu.text.Transliterator.Position
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.innoveworkshop.gametest.MainActivity
import com.innoveworkshop.gametest.engine.GameObject
import com.innoveworkshop.gametest.engine.GameSurface
import com.innoveworkshop.gametest.engine.Vector

data class BasicInfo(
    val position: Vector,
)

class ProjectilesHandler {
    class Handler : GameObject(){
        var projectiles = MutableList<GameObject?>(50){null}
        var create = mutableStateOf(false)
        var projectile = mutableStateOf(BasicInfo(Vector(0f,0f)))

        override fun onFixedUpdate() {
            super.onFixedUpdate()

            if (create.value){
                var i = 0

                while (i < projectiles.size){
                    if(projectiles[i] == null){
                        projectiles[i] = CreateProjectile(i, projectile.value)
                        break
                    }else{
                        i++
                    }
                }

                create.value = false
            }
        }

        fun CreateProjectile(id: Int, info: BasicInfo): Projectile{
            val projectile = Projectile(
                info.position,
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

        fun DeleteProjectile(id: Int, surface: GameSurface){
            Log.d("ERROR", "NOT HERE")
            val projectile = projectiles[id]
            Log.d("ERROR", "NOT HERE ALSO")
            if(projectile != null){
                Log.d("ERROR", "NOPE")
                projectiles[id] = null
                Log.d("ERROR", "NAH AHN")
                surface.removeGameObject(projectile)
                Log.d("ERROR", "NICLES BATATOIDES")
                projectile.isDestroyed = true
                Log.d("ERROR", "SO PODE SER AQUI")
            }
        }
    }
}

