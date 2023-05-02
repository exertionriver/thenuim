package river.exertion.kcop.view.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class AiHintMessage(val aiHintMessageType : AiHintMessageType, val aiHintEventId : String? = null, val aiHintEventReport : String? = null) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(AiHintBridge, this::class))
    }

    enum class AiHintMessageType {
        ClearHints, AddHint
    }

    companion object {
        const val AiHintBridge = "AiHintBridge"
    }
}