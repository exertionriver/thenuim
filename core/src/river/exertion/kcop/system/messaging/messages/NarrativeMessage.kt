package river.exertion.kcop.system.messaging.messages

import river.exertion.kcop.narrative.structure.NarrativeImmersion

data class NarrativeMessage(val narrativeMessageType : NarrativeMessageType, val promptNext : String? = null, val narrativeImmersion: NarrativeImmersion? = null) {

    enum class NarrativeMessageType {
        UpdateNarrativeImmersion, ReplaceCumlTimer, Pause, Unpause, Inactivate, Next
    }
}
