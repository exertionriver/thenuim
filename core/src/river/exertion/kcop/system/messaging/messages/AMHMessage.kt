package river.exertion.kcop.system.messaging.messages

data class AMHMessage(val messageType : AMHMessageType) {

    enum class AMHMessageType {
        ReloadMenuProfiles, SaveProfile
    }
}

