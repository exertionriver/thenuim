package river.exertion.kcop.plugin

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage

interface IDisplayPlugin : IPlugin {
    fun build(screenWidth : Float, screenHeight : Float, stage : Stage)
    fun displayKcopScreen(offset : Vector2)
    fun displayFullScreen(offset : Vector2)

}