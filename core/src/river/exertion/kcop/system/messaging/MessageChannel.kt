package river.exertion.kcop.system.messaging

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.TelegramProvider
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.system.colorPalette.ColorPaletteMessage
import river.exertion.kcop.system.messaging.messages.*
import kotlin.reflect.KClass

enum class MessageChannel {

    COLOR_PALETTE_BRIDGE { override val messageClass = ColorPaletteMessage::class },
    DISPLAY_VIEW_TEXTURE_BRIDGE { override val messageClass = DisplayViewTextureMessage::class },
    DISPLAY_VIEW_AUDIO_BRIDGE { override val messageClass = DisplayViewAudioMessage::class },
    DISPLAY_VIEW_MENU_BRIDGE { override val messageClass = DisplayViewMenuMessage::class },
    DISPLAY_VIEW_TEXT_BRIDGE { override val messageClass = DisplayViewTextMessage::class },
    MENU_BRIDGE { override val messageClass = MenuMessage::class },
    TEXT_VIEW_BRIDGE { override val messageClass = TextViewMessage::class },
    LOG_VIEW_BRIDGE { override val messageClass = LogViewMessage::class },
    STATUS_VIEW_BRIDGE { override val messageClass = StatusViewMessage::class },
    MENU_VIEW_BRIDGE { override val messageClass = DisplayViewMenuMessage::class },
    INPUT_VIEW_BRIDGE { override val messageClass = InputViewMessage::class },
    AI_VIEW_BRIDGE { override val messageClass = AiViewMessage::class },
    PAUSE_VIEW_BRIDGE { override val messageClass = PauseViewMessage::class },
    NARRATIVE_BRIDGE { override val messageClass = NarrativeMessage::class },
    NARRATIVE_BRIDGE_PAUSE_GATE { override val messageClass = NarrativeMessage::class },
    PROFILE_BRIDGE { override val messageClass = ProfileMessage::class },
    ECS_ENGINE_ENTITY_BRIDGE { override val messageClass = EngineEntityMessage::class },
    ECS_ENGINE_COMPONENT_BRIDGE { override val messageClass = EngineComponentMessage::class },

    TWO_BATCH_BRIDGE { override val messageClass = PolygonSpriteBatch::class },
    FONT_BRIDGE { override val messageClass = FontPackage::class },
    AMH_BRIDGE { override val messageClass = AMHMessage::class },
    ;

    abstract val messageClass : KClass<*>

    fun id() = this.ordinal
    fun messageClass() = this.messageClass
    fun isType(messageId : Int) = id() == messageId
    fun send(sender: Telegraph?, message : Any) = if (messageClass().isInstance(message)) MessageManager.getInstance().dispatchMessage(sender, id(), message) else throw Exception("send:$this requires ${messageClass()}, found ${message::class}")
    fun enableReceive(receiver: Telegraph?) = MessageManager.getInstance().addListener(receiver, this.id())
    fun enableProvider(provider: TelegramProvider?) = MessageManager.getInstance().addProvider(provider, this.id())
    inline fun <reified T:Any> receiveMessage(message : Any) : T {
        return if (T::class == messageClass()) message as T else throw Exception("receive:$this requires ${this.messageClass()}, found ${T::class}")
    }
}