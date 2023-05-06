package river.exertion.kcop.view.messaging

data class AiHintMessage(val aiHintMessageType : AiHintMessageType, val aiHintEventId : String? = null, val aiHintEventReport : String? = null) {

    enum class AiHintMessageType {
        ClearHints, AddHint
    }
}