import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import river.exertion.thenuim.base.GdxTestBase
import river.exertion.thenuim.base.Id
import river.exertion.thenuim.base.KcopBase
import river.exertion.thenuim.base.Log
import river.exertion.thenuim.messaging.MessageChannel
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.messaging.klop.IMessagingKlop

class GdxTestMessageChannelHandler : IMessagingKlop, GdxTestBase() {

    override var id = Id.randomId()
    override var tag = this::class.simpleName.toString()
    override val name = KcopBase.appName
    override val version = KcopBase.appVersion

    var channelIdx = -1

    //used for IMessagingKlop overload
    @BeforeAll
    override fun load() {
    }

    //used for IMessagingKlop overload
    @BeforeEach
    override fun loadChannels() {
        channelIdx = MessageChannelHandler.addChannel(MessageChannel(TestMessageBridge, TestMessage::class))
    }

    //used for IMessagingKlop overload
    @AfterEach
    override fun unload() {
        if (MessageChannelHandler.checkByTag(TestMessageBridge) != null)
            MessageChannelHandler.removeChannel(MessageChannelHandler.byTag(TestMessageBridge).messageChannel)
    }

    @AfterAll
    override fun dispose() {
        super<IMessagingKlop>.dispose()
    }

    @Test
    fun testChannel() {

        assertNotNull(MessageChannelHandler.checkByTag(TestMessageBridge))
        assertNull(MessageChannelHandler.checkByTag(NoMessageBridge))

        assertEquals(TestMessage::class, MessageChannelHandler.byTag(TestMessageBridge).messageClass)
        assertEquals(TestMessageBridge, MessageChannelHandler.byTag(TestMessageBridge).messageChannelTag)
        assertEquals(channelIdx, MessageChannelHandler.byTag(TestMessageBridge).messageChannelIdx)

        val byTagException = assertThrows(Exception::class.java) { MessageChannelHandler.byTag(NoMessageBridge) }
        Log.test("byTag() exception correctly thrown", byTagException.message)

        assertEquals(TestMessage::class, MessageChannelHandler.byId(channelIdx).messageClass)
        assertEquals(TestMessageBridge, MessageChannelHandler.byId(channelIdx).messageChannelTag)
        assertEquals(channelIdx, MessageChannelHandler.byId(channelIdx).messageChannelIdx)

        val byIdException = assertThrows(Exception::class.java) { MessageChannelHandler.byId(channelIdx + 1) }
        Log.test("byId() exception correctly thrown", byIdException.message)

        assertTrue(MessageChannelHandler.isType(TestMessageBridge, channelIdx))
        assertFalse(MessageChannelHandler.isType(NoMessageBridge, channelIdx))

        MessageChannelHandler.removeChannel(MessageChannelHandler.byTag(TestMessageBridge).messageChannel)

        val removeException = assertThrows(Exception::class.java) { MessageChannelHandler.byTag(TestMessageBridge) }
        Log.test("remove() exception correctly thrown", removeException.message)
    }

    @Test
    fun testSendReceive() {
        TestMessageReceiver

        Log.test("sending message", TestMessage.TestMessageSendString)

        MessageChannelHandler.send(TestMessageBridge, TestMessage(TestMessage.TestMessageType.SENT, TestMessage.TestMessageSendString))

        MessageChannelHandler.disableReceive(TestMessageBridge, TestMessageReceiver)

        Log.test("disabling receiver, sending second message", "'received sent message' should not appear")

        MessageChannelHandler.send(TestMessageBridge, TestMessage(TestMessage.TestMessageType.SENT, TestMessage.TestMessageSendString))
    }

    @Test
    fun testProvideReceive() {
        Log.test("init provider", TestMessage.TestMessageProvideString)
        TestMessageProvider

        Log.test("init receiver", TestMessage.TestMessageProvideString)
        TestMessageReceiver
    }

    companion object {
        const val TestMessageBridge = "TestMessageBridge"
        const val NoMessageBridge = "NoMessageBridge"
    }

}