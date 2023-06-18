package river.exertion.kcop.view.klop

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import river.exertion.kcop.base.IKlop
import river.exertion.kcop.view.layout.DisplayView

interface IDisplayViewKlop : IKlop {

    fun showView() {
        DisplayView.currentDisplayViewLayoutHandler = displayViewLayoutHandler()
        DisplayView.build()
        inputMultiplexer?.addProcessor(inputProcessor())
    }

    fun hideView() {
        displayViewLayoutHandler().clearContent()
        inputMultiplexer?.removeProcessor(inputProcessor())
    }

    var inputMultiplexer : InputMultiplexer?

    fun displayViewLayoutHandler() : IDisplayViewLayoutHandler
    fun inputProcessor() : InputProcessor
}