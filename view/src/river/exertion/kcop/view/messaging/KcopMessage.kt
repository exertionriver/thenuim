package river.exertion.kcop.view.messaging

data class KcopMessage(val kcopMessageType : KcopMessageType) {

    enum class KcopMessageType {
        FullScreen, KcopScreen, ColorPaletteOn, ColorPaletteOff
    }
}