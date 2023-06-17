package river.exertion.kcop.bundle

import com.badlogic.gdx.scenes.scene2d.Actor

interface IDisplayViewLayoutHandler {

    fun build() : Actor
    fun clearContent()
}