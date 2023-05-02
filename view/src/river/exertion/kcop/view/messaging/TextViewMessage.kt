package river.exertion.kcop.view.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class TextViewMessage(val textViewMessageType : TextViewMessageType, val narrativeText : String? = null, val prompts : List<String>? = null) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(TextViewBridge, this::class))
    }

    enum class TextViewMessageType {
        ReportText, HintText
    }

    companion object {
        const val TextViewBridge = "TextViewBridge"
    }
}