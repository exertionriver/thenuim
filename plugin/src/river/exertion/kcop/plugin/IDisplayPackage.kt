package river.exertion.kcop.plugin

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage

interface IDisplayPackage : IPackage {
    fun build() : Actor
    fun inputProcessor() : InputProcessor
}