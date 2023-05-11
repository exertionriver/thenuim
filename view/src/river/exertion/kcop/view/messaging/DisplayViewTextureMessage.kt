package river.exertion.kcop.view.messaging

import com.badlogic.gdx.graphics.Texture
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class DisplayViewTextureMessage(val messageType : DisplayViewTextureMessageType, val texture : Texture? = null, val layoutPaneIdx : Int? = 0) {

    enum class DisplayViewTextureMessageType {
        ShowImage, HideImage, FadeInImage, FadeOutImage, CrossFadeImage, ClearAll
    }
}

