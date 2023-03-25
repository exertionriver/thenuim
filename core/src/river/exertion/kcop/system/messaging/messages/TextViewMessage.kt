package river.exertion.kcop.system.messaging.messages

data class TextViewMessage(val narrativeText : String, val prompts : List<String>, val param: String)