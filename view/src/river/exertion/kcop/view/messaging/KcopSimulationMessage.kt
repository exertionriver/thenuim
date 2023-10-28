package river.exertion.kcop.view.messaging

data class KcopSimulationMessage(val kcopMessageType : KcopMessageType) {

    enum class KcopMessageType {
        FullScreen, KcopScreen, NextPlugin
    }
}