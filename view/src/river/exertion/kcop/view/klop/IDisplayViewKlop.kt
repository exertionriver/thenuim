package river.exertion.kcop.view.klop

import com.badlogic.gdx.InputProcessor
import river.exertion.kcop.base.IKlop
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.view.layout.DisplayView

interface IDisplayViewKlop : IKlop {

    fun showView() {
        DisplayView.currentDisplayViewLayoutHandler = displayViewLayoutHandler()
        DisplayView.build()
        KcopBase.inputMultiplexer.addProcessor(inputProcessor())
        //will need to come back to this later
        KcopBase.inputMultiplexer.addProcessor(KcopBase.stage)
    }

    fun hideView() {
        displayViewLayoutHandler().clearContent()
        KcopBase.inputMultiplexer.removeProcessor(inputProcessor())
        //will need to come back to this later
        KcopBase.inputMultiplexer.removeProcessor(KcopBase.stage)
    }

    fun displayViewLayoutHandler() : IDisplayViewLayoutHandler
    fun inputProcessor() : InputProcessor
}