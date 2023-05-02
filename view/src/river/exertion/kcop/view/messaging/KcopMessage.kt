package river.exertion.kcop.view.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class KcopMessage(val kcopMessageType : KcopMessageType) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(KcopBridge, this::class))
    }

    enum class KcopMessageType {
        FullScreen, KcopScreen, ShowColorPalette, HideColorPalette
    }

    companion object {
        const val KcopBridge = "KcopBridge"
    }
}