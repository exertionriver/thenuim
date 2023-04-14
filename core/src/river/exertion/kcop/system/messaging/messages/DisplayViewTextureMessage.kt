package river.exertion.kcop.system.messaging.messages

import com.badlogic.gdx.graphics.Texture

data class DisplayViewTextureMessage(val messageType : DisplayViewTextureMessageType, val texture : Texture? = null, val layoutPaneIdx : Int? = 0) {

    enum class DisplayViewTextureMessageType {
        ShowImage, HideImage, FadeInImage, FadeOutImage, CrossFadeImage, ClearAll
    }
}

