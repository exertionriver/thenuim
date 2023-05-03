package river.exertion.kcop.sim.narrative.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class NarrativeStatusMessage(val narrativeStatusMessageType : NarrativeStatusMessageType, val key : String? = null) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(NarrativeStatusBridge, this::class))
    }

    enum class NarrativeStatusMessageType {
        AddStatus, RemoveStatus
    }

    companion object {
        const val NarrativeStatusBridge = "NarrativeStatusBridge"
    }
}
