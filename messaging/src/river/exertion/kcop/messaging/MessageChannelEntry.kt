package river.exertion.kcop.messaging

import kotlin.reflect.KClass

class MessageChannelEntry(val messageChannelIdx : Int, val messageChannel: MessageChannel) {

    val messageChannelTag : String
        get() = messageChannel.messageChannelTag

    val messageClass : KClass<*>
        get() = messageChannel.messageClass
}