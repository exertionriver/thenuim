package river.exertion.kcop.system.messaging.messages

data class ViewMessage(val messageContent : String, val param : String? = null) {

    companion object {
        const val TogglePause = "TOGGLE_PAUSE"
        const val ToggleAi = "TOGGLE_AI"
    }
}
