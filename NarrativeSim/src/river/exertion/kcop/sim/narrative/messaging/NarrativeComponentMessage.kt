package river.exertion.kcop.sim.narrative.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.component.NarrativeComponent

data class NarrativeComponentMessage(val narrativeComponent : NarrativeComponent) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(NarrativeComponentBridge, this::class))
    }

    companion object {
        const val NarrativeComponentBridge = "NarrativeComponentBridge"
    }
}

