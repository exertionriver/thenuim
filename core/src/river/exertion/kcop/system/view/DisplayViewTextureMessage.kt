package river.exertion.kcop.system.view

import com.badlogic.gdx.graphics.Texture

data class DisplayViewTextureMessage(val messageType : DisplayViewTextureMessageType, val texture : Texture?)

enum class DisplayViewTextureMessageType {
    IMAGE_LARGE, IMAGE_MEDIUM, IMAGE_SMALL, IMAGE_CLEAR,
    FADE_IMAGE_LARGE_IN, FADE_IMAGE_LARGE_OUT, CROSSFADE_IMAGE_LARGE
}