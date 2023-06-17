package river.exertion.kcop.bundle

import com.badlogic.gdx.InputProcessor

interface IDisplayPackage {

    fun displayViewLayoutHandler() : IDisplayViewLayoutHandler
    fun inputProcessor() : InputProcessor
}