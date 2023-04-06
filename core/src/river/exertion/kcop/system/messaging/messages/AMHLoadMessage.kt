package river.exertion.kcop.system.messaging.messages

data class AMHLoadMessage(val messageType : AMHLoadMessageType, val selectedTitle : String? = null) {

    enum class AMHLoadMessageType {
        ReloadMenuProfiles, ReloadMenuNarratives,
        SetSelectedProfileAsset, SetSelectedNarrativeAsset,
        LoadSelectedProfile, LoadSelectedNarrative,
        RestartProgress
    }
}

