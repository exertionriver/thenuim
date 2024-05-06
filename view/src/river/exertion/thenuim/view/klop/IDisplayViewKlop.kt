package river.exertion.thenuim.view.klop

import com.badlogic.gdx.InputProcessor
import river.exertion.thenuim.base.IKlop
import river.exertion.thenuim.base.KcopBase
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.messaging.TitleUpdaterMessage
import river.exertion.thenuim.view.layout.StatusView
import river.exertion.thenuim.view.layout.TextView
import river.exertion.thenuim.view.layout.ViewLayout

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