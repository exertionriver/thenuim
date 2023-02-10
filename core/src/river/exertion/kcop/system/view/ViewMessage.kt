package river.exertion.kcop.system.view

data class ViewMessage(val targetView : ViewType?, val messageContent : String) {

    companion object {
        const val TogglePause = "TOGGLE_PAUSE"
    }
}
