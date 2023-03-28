package river.exertion.kcop.system.messaging.messages

import river.exertion.kcop.assets.ProfileAsset

data class AMHMessage(val messageType : AMHMessageType, val saveProfileAsset : ProfileAsset? = null) {

    enum class AMHMessageType {
        ReloadMenuProfiles, SaveProfile
    }
}

