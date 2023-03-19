package river.exertion.kcop.system.messaging.messages

data class NarrativeMessage(val narrativeMessageType : NarrativeMessageType, val promptNext : String? = null) {

    enum class NarrativeMessageType {
        PAUSE, UNPAUSE, NEXT
    }
}
