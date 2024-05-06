package river.exertion.thenuim.sim.narrative.messaging

import river.exertion.thenuim.sim.narrative.structure.NarrativeState

data class NarrativeComponentMessage(val narrativeMessageType : NarrativeMessageType, val promptNext : String? = null, val narrativeState: NarrativeState? = null) {

    enum class NarrativeMessageType {
        ReplaceCumlTimer, ReplaceBlockCumlTimer, RemoveBlockCumlTimer, Pause, Unpause, Inactivate, Next, Refresh
    }

}
