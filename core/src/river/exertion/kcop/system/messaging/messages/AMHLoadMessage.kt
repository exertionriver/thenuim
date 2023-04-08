package river.exertion.kcop.system.messaging.messages

import river.exertion.kcop.system.ecs.component.IComponent

data class AMHLoadMessage(val messageType : AMHLoadMessageType, val selectedTitle : String? = null, val loadComponent: IComponent? = null) {

    enum class AMHLoadMessageType {
        ReloadMenuProfiles, ReloadMenuNarratives,
        RefreshCurrentProfile, RefreshCurrentImmersion,
        RemoveCurrentProfile, RemoveCurrentImmersion,
        SetSelectedProfileAsset, SetSelectedNarrativeAsset,
        LoadSelectedProfile, LoadSelectedNarrative,
        RefreshSelectedProfile, RefreshSelectedNarrative
    }
}

