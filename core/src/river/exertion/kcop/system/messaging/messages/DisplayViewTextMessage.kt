package river.exertion.kcop.system.messaging.messages

import river.exertion.kcop.assets.FontSize

data class DisplayViewTextMessage(val layoutTag: String, val displayText : String? = null, val displayFontSize : FontSize? = null)