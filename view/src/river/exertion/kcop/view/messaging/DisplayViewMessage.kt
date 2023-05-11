package river.exertion.kcop.view.messaging

data class DisplayViewMessage(val messageType : DisplayViewMessageType) {

    enum class DisplayViewMessageType {
        Rebuild
    }
}

