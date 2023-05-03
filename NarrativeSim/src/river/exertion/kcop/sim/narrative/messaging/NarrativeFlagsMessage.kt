package river.exertion.kcop.sim.narrative.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class NarrativeFlagsMessage(val narrativeFlagsMessageType : NarrativeFlagsMessageType, val key : String, val value : String? = null) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(NarrativeFlagsBridge, this::class))
    }

    enum class NarrativeFlagsMessageType {
        SetPersistFlag, SetBlockFlag, UnsetPersistFlag, AddToCounter
    }

    companion object {
        const val NarrativeFlagsBridge = "NarrativeFlagsBridge"
    }

}
