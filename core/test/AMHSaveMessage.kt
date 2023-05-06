data class AMHSaveMessage(val messageType : AMHSaveMessageType, val saveName : String? = null) {

    enum class AMHSaveMessageType {
        SaveOverwriteProfile, SaveMergeProfile, NewProfile,
        PrepSaveProgress, SaveProgress, RestartProgress
    }
}

