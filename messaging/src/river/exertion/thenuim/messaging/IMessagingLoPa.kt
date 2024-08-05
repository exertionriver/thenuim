package river.exertion.thenuim.messaging

import river.exertion.thenuim.base.ILoPa

interface IMessagingLoPa : ILoPa {

    fun loadChannels()

    override fun dispose() {
        MessageChannelHandler.dispose()

        super.dispose()
    }
}