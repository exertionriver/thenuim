package river.exertion.kcop.system.ecs.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.AMHMessage
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
            super.initialize(entityName, initData)
            val profileComponentInit = IComponent.checkInitType<ProfileComponentInit>(initData)

            profile = profileComponentInit!!.profileAsset.profile

        } else {
            //allow empty component
            super.initialize(entityName, null)
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean {

        if (msg != null) {
            when {
                ( MessageChannel.PROFILE_BRIDGE.isType(msg.message) ) -> {
                    val profileMessage: ProfileMessage = MessageChannel.PROFILE_BRIDGE.receiveMessage(msg.extraInfo)

                    if ( (profile != null) && isInitialized) {
                        if ( (profileMessage.updateString != null && profileMessage.immersionName != null) ||
                                (profileMessage.profileMessageType == ProfileMessage.ProfileMessageType.LOAD_AMH_WITH_CURRENT ) ) {
                            when (profileMessage.profileMessageType) {
                                ProfileMessage.ProfileMessageType.UPDATE_IMMERSION -> {
                                    profile!!.currentImmersionName = profileMessage.immersionName
                                    profile!!.locations?.firstOrNull { it.immersionName == profile!!.currentImmersionName }?.immersionBlockId = profileMessage.updateString!!
                                }
                                ProfileMessage.ProfileMessageType.UPDATE_BLOCK_ID -> {
                                    profile!!.locations?.firstOrNull { it.immersionName == profile!!.currentImmersionName }?.immersionBlockId = profileMessage.updateString!!
                                }
                                ProfileMessage.ProfileMessageType.UPDATE_STATUS -> {
                                    profile!!.statuses?.firstOrNull { it.key == profileMessage.immersionName }?.value = profileMessage.updateString!!.toFloat()
                                }
                                ProfileMessage.ProfileMessageType.UPDATE_CUML_TIME -> {
                                    profile!!.locations?.firstOrNull { it.immersionName == profile!!.currentImmersionName }?.cumlImmersionTime = profileMessage.updateString
                                }
                                ProfileMessage.ProfileMessageType.LOAD_AMH_WITH_CURRENT -> {
                                    MessageChannel.AMH_BRIDGE.send(null, AMHMessage(AMHMessage.AMHMessageType.ReloadCurrentProfile, null, this.profile))
                                }
                            }
                            return true
                        }
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

    data class ProfileComponentInit(val profileAsset: ProfileAsset)
}