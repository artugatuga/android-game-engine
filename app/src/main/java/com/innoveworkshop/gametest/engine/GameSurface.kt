package com.innoveworkshop.gametest.engine

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi
import java.util.Timer
import java.util.TimerTask

class GameSurface @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {
    private val holder: SurfaceHolder
    private var timer: Timer? = null
    private var root: GameObject? = null

    // Create the GameObject list.
    var gameObjects = ArrayList<GameObject?>()
    var pipesInGame = ArrayList<GameObject?>()

    init {
        // Ensure we are on top of everything.
        setZOrderOnTop(true)

        // Set up the SurfaceHolder event handler.
        holder = getHolder()
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                // Ensure we get the onDraw events.
                setWillNotDraw(false)

                // Start up the root object.
                root!!.onStart(this@GameSurface)

                // Set up the fixed update timer.
                timer = Timer()
                timer!!.scheduleAtFixedRate(FixedUpdateTimer(), 0, (1000 / 60).toLong())
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                // TODO: Stop everything.
            }
        })
    }

    fun setRootGameObject(root: GameObject?) {
        this.root = root
    }

    fun addPipe(gameObject: GameObject){
        pipesInGame.add(gameObject)
    }

    fun addGameObject(gameObject: GameObject) {
        if(gameObjects.isEmpty()){
            gameObjects.add(gameObject)
            gameObject.id = gameObjects.size - 1
        }else{
            var i = 0
            val size = gameObjects.size
            while (i < size){
                if(gameObjects[i] == null){
                    gameObjects[i] = gameObject
                    gameObject.id = i
                    break
                }else if (i == gameObjects.size - 1){
                    gameObjects.add(gameObject)
                    gameObject.id = gameObjects.size - 1
                }
                i++
            }
        }
        gameObject.onStart(this)
    }

    fun removeGameObject(gameObject: GameObject) {
        var i = 0
        while (i < gameObjects.size - gameObject.id!! - 1){
            gameObjects[gameObject.id!! + i] = gameObjects[gameObject.id!! + i + 1]
            gameObjects[gameObject.id!! + i]?.id = gameObjects[gameObject.id!! + i]?.id!! - 1
            i++
        }
        gameObjects[gameObjects.size - 1] = null
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        root!!.onDraw(canvas)
        for (gameObject in gameObjects) {
            gameObject?.onDraw(canvas)
        }
    }

    internal inner class FixedUpdateTimer : TimerTask() {
        override fun run() {
            for (gameObject in gameObjects) {
                gameObject?.onFixedUpdate()
            }
            root!!.onFixedUpdate()
            invalidate()
        }
    }
}
