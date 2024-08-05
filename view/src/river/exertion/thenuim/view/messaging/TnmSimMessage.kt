package river.exertion.thenuim.view.messaging

data class TnmSimMessage(val tnmSimMessageType : TnmSimMessageType) {

    enum class TnmSimMessageType {
        DebugScreen, DisplayViewScreen, DisplayFullScreen, NextPlugin
    }
}