package river.exertion.thenuim.messaging

data class TitleUpdaterMessage(val newTitle : String) {

    companion object {
        const val TitleUpdaterBridge = "TitleUpdaterBridge"
    }
}
