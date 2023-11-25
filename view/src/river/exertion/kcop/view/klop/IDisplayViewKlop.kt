package river.exertion.kcop.view.klop

import com.badlogic.gdx.InputProcessor
import river.exertion.kcop.base.IKlop
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.view.layout.DisplayView
import river.exertion.kcop.view.layout.displayViewLayout.DVLayoutHandler

interface IDisplayViewKlop : IKlop {

    fun showView() {
        DisplayView.currentDisplayViewLayoutHandler = displayViewLayoutHandler()
        KcopBase.inputMultiplexer.addProcessor(inputProcessor())
        DisplayView.build()
    }

    fun hideView() {
        displayViewLayoutHandler().clearContent()
        KcopBase.inputMultiplexer.removeProcessor(inputProcessor())
    }

    fun displayViewLayoutHandler() : IDisplayViewLayoutHandler
    fun inputProcessor() : InputProcessor
}