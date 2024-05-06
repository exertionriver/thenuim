package river.exertion.thenuim.messaging

import kotlin.reflect.KClass

data class MessageChannelEntry(val messageChannelIdx : Int, val messageChannel: MessageChannel) {

    val messageChannelTag : String
        get() = messageChannel.messageChannelTag

    val messageClass : KClass<*>
        get() = messageChannel.messageClass
}