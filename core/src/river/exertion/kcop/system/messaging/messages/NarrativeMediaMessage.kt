package river.exertion.kcop.system.messaging.messages

data class NarrativeMediaMessage(val narrativeMediaMessageType : NarrativeMediaMessageType, val assetFilename : String, val imageLayoutPaneIdx: String? = null) {

    enum class NarrativeMediaMessageType {
        PlaySound, PlayMusic, StopMusic, FadeInMusic, FadeOutMusic, CrossFadeMusic, ShowImage, HideImage, FadeInImage, FadeOutImage, CrossFadeImage,
    }
}
