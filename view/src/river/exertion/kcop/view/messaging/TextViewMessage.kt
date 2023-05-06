package river.exertion.kcop.view.messaging

data class TextViewMessage(val textViewMessageType : TextViewMessageType, val narrativeText : String? = null, val prompts : List<String>? = null) {

    enum class TextViewMessageType {
        ReportText, HintText
    }
}