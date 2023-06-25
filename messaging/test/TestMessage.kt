data class TestMessage(val testMessageType: TestMessageType, val messageString : String) {

    enum class TestMessageType {
        SENT, PROVIDED
    }

    companion object {
        const val TestMessageSendString = "testingMessaging123"
        const val TestMessageProvideString = "testingMessaging456"
    }
}