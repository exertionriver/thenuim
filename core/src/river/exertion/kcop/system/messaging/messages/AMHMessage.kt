package river.exertion.kcop.system.messaging.messages

import river.exertion.kcop.system.profile.Profile

data class AMHMessage(val messageType : AMHMessageType, val selectedTitle : String? = null, val currentProfile : Profile? = null, val saveName : String? = null) {

    enum class AMHMessageType {
        ReloadMenuProfiles, ReloadMenuNarratives,
        SetSelectedProfileAsset, SetSelectedNarrativeAsset,
        LoadSelectedProfile, LoadSelectedNarrative, ReloadCurrentProfile,
        SaveOverwriteProfile, SaveMergeProfile
    }
}

