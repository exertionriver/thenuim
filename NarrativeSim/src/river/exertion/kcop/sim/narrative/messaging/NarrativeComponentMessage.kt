package river.exertion.kcop.sim.narrative.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.structure.NarrativeState

data class NarrativeComponentMessage(val narrativeMessageType : NarrativeMessageType, val promptNext : String? = null, val narrativeState: NarrativeState? = null) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(NarrativeBridge, this::class))
    }

    enum class NarrativeMessageType {
        ReplaceCumlTimer, Pause, Unpause, Inactivate, Next
    }

    companion object {
        const val NarrativeBridge = "NarrativeBridge"
    }
}
