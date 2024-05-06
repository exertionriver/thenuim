package river.exertion.thenuim.messaging

import kotlin.reflect.KClass

data class MessageChannel(val messageChannelTag : String, val messageClass: KClass<*>)