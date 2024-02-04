package river.exertion.kcop.view.klop

import com.badlogic.gdx.InputProcessor
import river.exertion.kcop.base.IKlop
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.messaging.TitleUpdaterMessage
import river.exertion.kcop.view.KcopInputProcessor
import river.exertion.kcop.view.layout.StatusView
import river.exertion.kcop.view.layout.TextView
import river.exertion.kcop.view.layout.ViewLayout

interface IDisplayViewKlop : IKlop {

    fun showView() {
        MessageChannelHandler.send(TitleUpdaterMessage.TitleUpdaterBridge, TitleUpdaterMessage(title()))

        ViewLayout.currentDisplayViewLayoutHandler = displayViewLayoutHandler()
        ViewLayout.rebuild()

        KcopBase.inputMultiplexer.addProcessor(inputProcessor())
    }

    fun hideView() {
        displayViewLayoutHandler().clearContent()
        StatusView.clearStatuses()
        TextView.clearContent()

        KcopBase.inputMultiplexer.removeProcessor(inputProcessor())
    }

    fun displayViewLayoutHandler() : IDisplayViewLayoutHandler
    fun inputProcessor() : InputProcessor
}