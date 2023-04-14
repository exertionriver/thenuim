package river.exertion.kcop.system.messaging.messages

data class NarrativeStatusMessage(val narrativeStatusMessageType : NarrativeFlagsMessageType, val key : String? = null) {

    enum class NarrativeFlagsMessageType {
        AddStatus, RemoveStatus
    }
}
