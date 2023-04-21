package river.exertion.kcop.system.messaging.messages

data class KcopMessage(val kcopMessageType : KcopMessageType) {

    enum class KcopMessageType {
        FullScreen, KcopScreen
    }
}