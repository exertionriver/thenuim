package river.exertion.thenuim.messaging.klop

import river.exertion.thenuim.base.IKlop
import river.exertion.thenuim.messaging.MessageChannelHandler

interface IMessagingKlop : IKlop {

    fun loadChannels()

    override fun dispose() {
        MessageChannelHandler.dispose()

        super.dispose()
    }
}