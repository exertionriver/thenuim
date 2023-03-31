package river.exertion.kcop.system.messaging.messages

data class ProfileMessage(val profileMessageType : ProfileMessageType, val immersionName : String? = null, val updateString : String? = null) {

    enum class ProfileMessageType {
        UPDATE_IMMERSION, UPDATE_BLOCK_ID, UPDATE_STATUS, UPDATE_CUML_TIME, LOAD_AMH_WITH_CURRENT
    }
}
