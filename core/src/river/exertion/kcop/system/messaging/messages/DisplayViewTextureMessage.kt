package river.exertion.kcop.system.messaging.messages

import com.badlogic.gdx.graphics.Texture

data class DisplayViewTextureMessage(val messageType : DisplayViewTextureMessageType, val layoutPaneIdx : Int, val texture : Texture?)

enum class DisplayViewTextureMessageType {
    SHOW_IMAGE, CLEAR_ALL, FADE_IMAGE_IN, FADE_IMAGE_OUT, CROSSFADE_IMAGE
}