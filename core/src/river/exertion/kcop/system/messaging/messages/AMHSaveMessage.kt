package river.exertion.kcop.system.messaging.messages

import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent

data class AMHSaveMessage(val messageType : AMHSaveMessageType, val saveName : String? = null) {

    enum class AMHSaveMessageType {
        SaveOverwriteProfile, SaveMergeProfile, NewProfile,
        PrepSaveProgress, SaveProgress, RestartProgress
    }
}

