import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.thenuim.messaging.MessageChannelHandler
import org.junit.jupiter.api.Assertions.assertEquals
import river.exertion.thenuim.base.Log

object TestMessageReceiver : Telegraph {

    init {
        MessageChannelHandler.enableReceive(GdxTestMessageChannelHandler.TestMessageBridge, this)
    }

    override fun handleMessage(msg : Telegram?) : Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(GdxTestMessageChannelHandler.TestMessageBridge, msg.message)) -> {
                    val testMessage : TestMessage = MessageChannelHandler.receiveMessage(GdxTestMessageChannelHandler.TestMessageBridge, msg.extraInfo)

                    when (testMessage.testMessageType) {
                        TestMessage.TestMessageType.SENT -> {
                            Log.test("received sent message", testMessage.messageString)
                            assertEquals(TestMessage.TestMessageSendString, testMessage.messageString)
                        }
                        TestMessage.TestMessageType.PROVIDED -> {
                            Log.test("received provided message", testMessage.messageString)
                            assertEquals(TestMessage.TestMessageProvideString, testMessage.messageString)
                        }
                    }
                }
            }
        }

        return true
    }
}