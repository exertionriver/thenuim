package river.exertion.thenuim.view

import com.badlogic.gdx.InputProcessor
import river.exertion.thenuim.base.ILoPa
import river.exertion.thenuim.base.TnmBase
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.messaging.TitleUpdaterMessage
import river.exertion.thenuim.view.layout.StatusView
import river.exertion.thenuim.view.layout.TextView
import river.exertion.thenuim.view.layout.ViewLayout

interface IDisplayViewLoPa : ILoPa {

    fun showView() {
        MessageChannelHandler.send(TitleUpdaterMessage.TitleUpdaterBridge, TitleUpdaterMessage(title()))

        ViewLayout.currentDisplayViewLayoutHandler = displayViewLayoutHandler()
        ViewLayout.rebuild()

        TnmBase.inputMultiplexer.addProcessor(inputProcessor())
    }

    fun hideView() {
        displayViewLayoutHandler().clearContent()
        StatusView.clearStatuses()
        TextView.clearContent()

        TnmBase.inputMultiplexer.removeProcessor(inputProcessor())
    }

    fun displayViewLayoutHandler() : IDisplayViewLayoutHandler
    fun inputProcessor() : InputProcessor
}