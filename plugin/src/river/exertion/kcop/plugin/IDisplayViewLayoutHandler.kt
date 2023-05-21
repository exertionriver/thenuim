package river.exertion.kcop.plugin

import com.badlogic.gdx.scenes.scene2d.Actor

interface IDisplayViewLayoutHandler {

    fun build() : Actor
    fun clearContent()
}