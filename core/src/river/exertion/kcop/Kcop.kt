package river.exertion.kcop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.inject.register
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.messaging.TitleUpdaterMessage
import river.exertion.kcop.simulation.KcopSimulator

class Kcop : KtxGame<KtxScreen>(), Telegraph {

    override fun create() {
        KcopBase.create()

        MessageChannelHandler.addChannel(MessageChannel(TitleUpdaterMessage.TitleUpdaterBridge, TitleUpdaterMessage::class))
        MessageChannelHandler.enableReceive(TitleUpdaterMessage.TitleUpdaterBridge, this)

        addScreen(KcopSimulator())
        setScreen<KcopSimulator>()
    }

    override fun handleMessage(msg: Telegram?): Boolean {

        if (msg != null) {
            when {
                (MessageChannelHandler.isType(TitleUpdaterMessage.TitleUpdaterBridge, msg.message)) -> {
                    val titleUpdaterMessage: TitleUpdaterMessage = MessageChannelHandler.receiveMessage(
                        TitleUpdaterMessage.TitleUpdaterBridge, msg.extraInfo)

                    Gdx.graphics.setTitle(titleUpdaterMessage.newTitle)
                }
            }
        }
        return false
    }
}