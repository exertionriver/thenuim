package river.exertion.kcop.system.messaging.messages

data class TextViewMessage(val narrativeText : String, val prompts : List<String>? = null, val param: String? = null)