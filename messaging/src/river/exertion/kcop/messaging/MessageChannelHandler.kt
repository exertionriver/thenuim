package river.exertion.kcop.messaging

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.TelegramProvider
import com.badlogic.gdx.ai.msg.Telegraph

object MessageChannelHandler {

    private var messageChannelsIdx = 0
    val messageChannels = mutableListOf<MessageChannelEntry>()

    fun checkByTag(messageChannelTag : String) : MessageChannelEntry? {
        return messageChannels.firstOrNull { it.messageChannelTag == messageChannelTag }
    }

    fun byTag(messageChannelTag : String) : MessageChannelEntry {
        return checkByTag(messageChannelTag) ?: throw Exception("${this::class.simpleName}:${MessageChannelHandler::byTag.name} : messageChannel $messageChannelTag not found")
    }

    fun byId(messageChannelIdx : Int) : MessageChannelEntry {
        return messageChannels.firstOrNull { it.messageChannelIdx == messageChannelIdx } ?: throw Exception("${this::class.simpleName}:${MessageChannelHandler::byId.name} : messageChannel at $messageChannelIdx not found")
    }

    fun addChannel(messageChannel : MessageChannel) {
        val channelEntryCheck = checkByTag(messageChannel.messageChannelTag)

        if (channelEntryCheck == null) messageChannels.add(MessageChannelEntry(messageChannelsIdx++, messageChannel)) else throw Exception("${this::class.simpleName}:${MessageChannelHandler::addChannel.name} : channel ${messageChannel.messageChannelTag} already added")
    }

    fun addChannels(messageChannels : List<MessageChannel>) {
        messageChannels.forEach { addChannel(it) }
    }

    fun removeChannel(messageChannel: MessageChannel) {
        val channelEntryToRemove = byTag(messageChannel.messageChannelTag)

        messageChannels.remove(channelEntryToRemove)
    }

    fun removeChannels(messageChannels : List<MessageChannel>) {
        messageChannels.forEach { removeChannel(it) }
    }

    fun isType(messageChannelTag : String, messageChannelIdx : Int) : Boolean {
        return messageChannels.isNotEmpty() && byId(messageChannelIdx).messageChannelTag == messageChannelTag
    }

    fun send(messageChannelTag : String, message : Any, sender: Telegraph? = null) {
        val messageChannelEntry = byTag(messageChannelTag)

        if (messageChannelEntry.messageClass.isInstance(message)) {
            MessageManager.getInstance().dispatchMessage(sender, messageChannelEntry.messageChannelIdx, message)
        } else throw Exception("${this::class.simpleName}:${MessageChannelHandler::send.name} messageClass requires ${messageChannelEntry.messageClass.simpleName}, found ${message::class}")
    }

    fun enableReceive(messageChannelTag: String, receiver: Telegraph?) = MessageManager.getInstance().addListener(receiver, byTag(messageChannelTag).messageChannelIdx)
    fun disableReceive(messageChannelTag: String, receiver: Telegraph?) = MessageManager.getInstance().removeListener(receiver, byTag(messageChannelTag).messageChannelIdx)
    fun enableProvider(messageChannelTag: String, provider: TelegramProvider?) = MessageManager.getInstance().addProvider(provider, byTag(messageChannelTag).messageChannelIdx)

    inline fun <reified T:Any> receiveMessage(messageChannelTag: String, message : Any) : T {
        return if (T::class == byTag(messageChannelTag).messageClass) message as T
            else throw Exception("${this::class.simpleName}:${MessageChannelHandler::send.name}:$messageChannelTag requires ${byTag(messageChannelTag).messageClass}, found ${T::class}")
    }
}

