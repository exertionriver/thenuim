package river.exertion.thenuim.view.messaging

data class KcopSimulationMessage(val kcopMessageType : KcopMessageType) {

    enum class KcopMessageType {
        KcopScreen, DisplayViewScreen, DisplayFullScreen, NextPlugin
    }
}