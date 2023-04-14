package river.exertion.kcop.system.messaging.messages

data class NarrativeFlagsMessage(val narrativeFlagsMessageType : NarrativeFlagsMessageType, val key : String, val value : String? = null) {

    enum class NarrativeFlagsMessageType {
        SetFlag, UnsetFlag, AddToCounter
    }
}
