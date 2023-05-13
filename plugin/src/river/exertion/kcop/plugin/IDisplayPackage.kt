package river.exertion.kcop.plugin

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.scenes.scene2d.Actor

interface IDisplayPackage {

    fun build() : Actor
    fun inputProcessor() : InputProcessor
}