package river.exertion.kcop.system.view

import river.exertion.kcop.simulation.view.ViewType

data class ViewMessage(val targetView : ViewType?, val messageContent : String, val param : String? = null) {

    companion object {
        const val TogglePause = "TOGGLE_PAUSE"
        const val ToggleAi = "TOGGLE_AI"
        const val BlockInstTimer = "BLOCK_INST_TIMER"
        const val BlockCumlTimer = "BLOCK_CUML_TIMER"
    }
}
