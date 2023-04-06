package river.exertion.kcop.system.messaging.messages

data class ProfileMessage(val profileMessageType : ProfileMessageType, val immersionId : String? = null, val cumlTime : String? = null) {

    enum class ProfileMessageType {
        UPDATE_IMMERSION_ID, UPDATE_CUML_TIME, INACTIVATE
    }
}
