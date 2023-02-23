package river.exertion.kcop.system.view

import com.badlogic.gdx.graphics.Texture

data class DisplayViewMessage(val messageType : DisplayViewMessageType, val texture : Texture?)

enum class DisplayViewMessageType {
    IMAGE_LARGE, IMAGE_MEDIUM, IMAGE_SMALL, IMAGE_CLEAR
}