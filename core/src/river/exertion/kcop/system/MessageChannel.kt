package river.exertion.kcop.system

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.system.colorPalette.ColorPaletteMessage
import river.exertion.kcop.system.view.*
import kotlin.reflect.KClass

enum class MessageChannel {

    COLOR_PALETTE_BRIDGE { override val messageClass = ColorPaletteMessage::class },
    LAYOUT_BRIDGE { override val messageClass = ViewMessage::class },
    DISPLAY_VIEW_TEXTURE_BRIDGE { override val messageClass = DisplayViewTextureMessage::class },
    DISPLAY_VIEW_AUDIO_BRIDGE { override val messageClass = DisplayViewAudioMessage::class },
    DISPLAY_VIEW_MENU_BRIDGE { override val messageClass = DisplayViewMenuMessage::class },
    TEXT_VIEW_BRIDGE { override val messageClass = TextViewMessage::class },
    INPUT_VIEW_BRIDGE { override val messageClass = InputViewMessage::class },
    LOG_VIEW_BRIDGE { override val messageClass = LogViewMessage::class },
    STATUS_VIEW_BRIDGE { override val messageClass = StatusViewMessage::class },
    IMMERSION_TIME_BRIDGE { override val messageClass = ImmersionTimerMessage::class },
    NARRATIVE_PROMPT_BRIDGE { override val messageClass = ViewMessage::class },
    NARRATIVE_PROMPT_BRIDGE_PAUSE_GATE { override val messageClass = ViewMessage::class },
    ECS_ENGINE_BRIDGE { override val messageClass = EngineMessage::class }
    ;

    fun id() = this.ordinal
    fun isType(messageId : Int) = this.ordinal == messageId
    abstract val messageClass : KClass<*>
    fun send(sender: Telegraph?, message : Any) = if (this.messageClass.isInstance(message)) MessageManager.getInstance().dispatchMessage(sender, this.id(), message) else throw Exception("send:$this requires ${this.messageClass}, found ${message::class}")
    fun enableReceive(receiver: Telegraph?) = MessageManager.getInstance().addListener(receiver, this.id())
    inline fun <reified T:Any> receiveMessage(message : Any) : T {
        return if (T::class == this.messageClass) message as T else throw Exception("receive:$this requires ${this.messageClass}, found ${T::class}")
    }
}