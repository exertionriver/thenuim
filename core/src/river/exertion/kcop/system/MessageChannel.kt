package river.exertion.kcop.system

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.system.colorPalette.ColorPaletteMessage
import river.exertion.kcop.system.text1d.Text1dMessage
import river.exertion.kcop.system.view.InputViewMessage
import river.exertion.kcop.system.view.LogViewMessage
import river.exertion.kcop.system.view.ViewMessage
import kotlin.reflect.KClass

enum class MessageChannel {

    COLOR_PALETTE_BRIDGE { override val messageClass = ColorPaletteMessage::class },
    LAYOUT_BRIDGE { override val messageClass = ViewMessage::class },
    TEXT1D_BRIDGE { override val messageClass = Text1dMessage::class },
    INPUT_VIEW_BRIDGE { override val messageClass = InputViewMessage::class },
    LOG_VIEW_BRIDGE { override val messageClass = LogViewMessage::class },
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