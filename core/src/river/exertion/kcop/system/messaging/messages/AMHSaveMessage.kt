package river.exertion.kcop.system.messaging.messages

import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent

data class AMHSaveMessage(val messageType : AMHSaveMessageType, val currentProfileComponent : ProfileComponent? = null, val currentNarrativeComponent : NarrativeComponent? = null, val saveName : String? = null) {

    enum class AMHSaveMessageType {
        ReloadCurrentProfile, ReloadCurrentImmersion,
        PollSelectedProfileAsset, PollSelectedNarrativeAsset,
        SaveOverwriteProfile, SaveMergeProfile, NewProfile,
        PrepSaveProgress, SaveProgress
    }
}

