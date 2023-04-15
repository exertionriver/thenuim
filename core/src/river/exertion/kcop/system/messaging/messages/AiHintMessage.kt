package river.exertion.kcop.system.messaging.messages

data class AiHintMessage(val aiHintMessageType : AiHintMessageType, val aiHintEventId : String? = null, val aiHintEventReport : String? = null) {

    enum class AiHintMessageType {
        ClearHints, AddHint
    }
}