package river.exertion.kcop.system.messaging.messages

data class ProfileMessage(val profileMessageType : ProfileMessageType, val narrativeId : String? = null, val updateString : String? = null) {

    enum class ProfileMessageType {
        UPDATE_BLOCK_ID, UPDATE_STATUS, UPDATE_CUML_TIME
    }
}
