package river.exertion.kcop.view.klop

import com.badlogic.gdx.InputProcessor
import river.exertion.kcop.base.IKlop
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.view.layout.DisplayView
import river.exertion.kcop.view.layout.ViewLayout
import river.exertion.kcop.view.layout.displayViewLayout.DVLayoutHandler

interface IDisplayViewKlop : IKlop {

    fun showView() {
        ViewLayout.currentDisplayViewLayoutHandler = displayViewLayoutHandler()
        ViewLayout.rebuild()

        KcopBase.inputMultiplexer.addProcessor(inputProcessor())
    }

    fun hideView() {
        displayViewLayoutHandler().clearContent()
        KcopBase.inputMultiplexer.removeProcessor(inputProcessor())
    }

    fun displayViewLayoutHandler() : IDisplayViewLayoutHandler
    fun inputProcessor() : InputProcessor
}