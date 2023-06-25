import com.badlogic.gdx.ai.msg.TelegramProvider
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.messaging.MessageChannelHandler

object TestMessageProvider : TelegramProvider {

    init {
        MessageChannelHandler.enableProvider(GdxTestMessageChannelHandler.TestMessageBridge, this)
    }

    override fun provideMessageInfo(msg: Int, receiver: Telegraph?): Any {
        if ( (msg == MessageChannelHandler.byTag(GdxTestMessageChannelHandler.TestMessageBridge).messageChannelIdx) &&
            (receiver == TestMessageReceiver) )
                return TestMessage(TestMessage.TestMessageType.PROVIDED, TestMessage.TestMessageProvideString)

        return true
    }
}