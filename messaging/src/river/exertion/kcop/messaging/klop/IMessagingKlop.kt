package river.exertion.kcop.messaging.klop

import river.exertion.kcop.base.IKlop
import river.exertion.kcop.messaging.MessageChannelHandler

interface IMessagingKlop : IKlop {

    fun loadChannels()

    override fun dispose() {
        MessageChannelHandler.dispose()

        super.dispose()
    }
}