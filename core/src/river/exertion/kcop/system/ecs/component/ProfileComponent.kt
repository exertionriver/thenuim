package river.exertion.kcop.system.ecs.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.narrative.character.NameTypes
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.AMHSaveMessage
import river.exertion.kcop.system.messaging.messages.EngineComponentMessage
import river.exertion.kcop.system.messaging.messages.EngineComponentMessageType
import river.exertion.kcop.system.messaging.messages.ProfileMessage
import river.exertion.kcop.system.profile.Profile

class ProfileComponent : IComponent, Telegraph {

    var profile : Profile? = null
    fun profileId() = if (profile != null) profile?.id!! else throw Exception("ProfileComponent::profileId() profile is null")

    override var isInitialized = false

    val timerPair = ImmersionTimerPair(ImmersionTimer(), ImmersionTimer())

    override fun initialize(initData: Any?) {

        if (initData != null) {
            super.initialize(initData)

            val profileComponentInit = IComponent.checkInitType<ProfileComponentInit>(initData)

            if (profileComponentInit!!.profileAsset != null) {
                profile = profileComponentInit.profileAsset!!.profile
                timerPair.cumlImmersionTimer.setPastStartTime(ImmersionTimer.inMilliseconds(profileComponentInit.profileAsset.profile!!.cumlTime()))
            } else {
                profile = Profile(name= NameTypes.COMMON.nextName())
            }

            MessageChannel.PROFILE_BRIDGE.enableReceive(this)

            activate()

        } else {
            //allow empty component
            super.initialize(null)
        }
    }

    fun activate() {
        if (isInitialized) {

            timerPair.instImmersionTimer!!.resetTimer()
            timerPair.instImmersionTimer.resumeTimer()
            timerPair.cumlImmersionTimer.resumeTimer()
        }
    }

    fun inactivate() {
        if (isInitialized) {

            timerPair.instImmersionTimer!!.pauseTimer()
            timerPair.cumlImmersionTimer.pauseTimer()

            MessageChannel.PROFILE_BRIDGE.disableReceive(this)

            isInitialized = false
        }
    }

    fun cumlTime() = if (isInitialized) timerPair.cumlImmersionTimer.immersionTime() else ImmersionTimer.zero()

    @Suppress("NewApi")
    override fun handleMessage(msg: Telegram?): Boolean {

        if (msg != null) {
            when {
                ( MessageChannel.PROFILE_BRIDGE.isType(msg.message) ) -> {
                    val profileMessage: ProfileMessage = MessageChannel.PROFILE_BRIDGE.receiveMessage(msg.extraInfo)

                    if ( (profile != null) && isInitialized) {
                        when (profileMessage.profileMessageType) {
                            ProfileMessage.ProfileMessageType.UPDATE_IMMERSION_ID -> {
                                if (profileMessage.immersionId != null) {
                                    profile!!.currentImmersionId = profileMessage.immersionId
                                }
                            }
                            ProfileMessage.ProfileMessageType.UPDATE_CUML_TIME -> {
                                if (profileMessage.cumlTime != null) {
                                    profile!!.cumlTime = profileMessage.cumlTime
                                }
                            }
                            ProfileMessage.ProfileMessageType.INACTIVATE -> {
                                inactivate()
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

    data class ProfileComponentInit(val profileAsset: ProfileAsset? = null)
}