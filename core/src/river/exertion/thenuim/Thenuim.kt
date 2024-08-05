package river.exertion.thenuim

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import ktx.app.KtxGame
import ktx.app.KtxScreen
import river.exertion.thenuim.base.TnmBase
import river.exertion.thenuim.messaging.MessageChannel
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.messaging.TitleUpdaterMessage
import river.exertion.thenuim.simulation.TnmSim

//Entry-point for Thenuim
class Thenuim : KtxGame<KtxScreen>(), Telegraph {

    override fun create() {
        TnmBase.create()

        MessageChannelHandler.addChannel(MessageChannel(TitleUpdaterMessage.TitleUpdaterBridge, TitleUpdaterMessage::class))
        MessageChannelHandler.enableReceive(TitleUpdaterMessage.TitleUpdaterBridge, this)

        addScreen(TnmSim())
        setScreen<TnmSim>()
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