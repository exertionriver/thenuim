package river.exertion.kcop.system.messaging.messages

data class DisplayViewMenuMessage(val targetMenuTag : String? = null, val menuButtonIdx : Int? = null, var isChecked : Boolean = false)
