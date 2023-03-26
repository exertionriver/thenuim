package river.exertion.kcop.system.ecs.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.NarrativeMessage
import river.exertion.kcop.system.messaging.messages.ProfileMessage
import river.exertion.kcop.system.profile.Profile

class ProfileComponent : IComponent, Telegraph {

    init {
        MessageChannel.PROFILE_BRIDGE.enableReceive(this)
    }

    var profile : Profile? = null

    override var entityName = ""
    override var isInitialized = false

    override fun initialize(entityName: String, initData: Any?) {

        if (initData != null) {
            profile = IComponent.checkInitType(initData)

            super.initialize(entityName, initData)
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean {

        if (msg != null) {
            if (MessageChannel.PROFILE_BRIDGE.isType(msg.message) ) {
                val profileMessage: ProfileMessage = MessageChannel.PROFILE_BRIDGE.receiveMessage(msg.extraInfo)

                if ( (profile != null) && isInitialized) {
                    if (profileMessage.updateString != null && profileMessage.narrativeId != null) {
                        when (profileMessage.profileMessageType) {
                            ProfileMessage.ProfileMessageType.UPDATE_BLOCK_ID -> {
                                profile!!.currentImmersionBlockId = profileMessage.updateString
                            }
                            ProfileMessage.ProfileMessageType.UPDATE_STATUS -> {
                                profile!!.statuses.firstOrNull { it.key == profileMessage.narrativeId }?.value = profileMessage.updateString.toFloat()
                            }
                            ProfileMessage.ProfileMessageType.UPDATE_CUML_TIME -> {
                                profile!!.statuses.firstOrNull { it.key == profileMessage.narrativeId }?.cumlImmersionTime = profileMessage.updateString
                            }
                        }
                        return true
                    }
                }
            }
        }
        return false
    }

    companion object {
        fun has(entity : Entity?) : Boolean = entity?.components?.firstOrNull{ it is ProfileComponent } != null
        fun getFor(entity : Entity?) : ProfileComponent? = if (has(entity)) entity?.components?.firstOrNull { it is ProfileComponent } as ProfileComponent else null
    }

}