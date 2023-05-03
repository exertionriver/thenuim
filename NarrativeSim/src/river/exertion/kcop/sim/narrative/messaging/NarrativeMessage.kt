package river.exertion.kcop.sim.narrative.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.structure.NarrativeImmersion

data class NarrativeMessage(val narrativeMessageType : NarrativeMessageType, val promptNext : String? = null, val narrativeImmersion: NarrativeImmersion? = null) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(NarrativeBridge, this::class))
    }

    enum class NarrativeMessageType {
        UpdateNarrativeImmersion, ReplaceCumlTimer, Pause, Unpause, Inactivate, Next
    }

    companion object {
        const val NarrativeBridge = "NarrativeBridge"
    }
}
