package river.exertion.kcop.system.ecs.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.assets.AssetManagerHandlerCl
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.*
import river.exertion.kcop.system.profile.Profile
import river.exertion.kcop.system.profile.ProfileSetting

class ProfileComponent : IComponent, Telegraph {

    var profile : Profile? = null

    override fun componentId() = if (profile != null) profile?.id!! else throw Exception("ProfileComponent::componentId() profile is null")

    override var isInitialized = false

    val timerPair = ImmersionTimerPair(ImmersionTimer(), ImmersionTimer())

    var currentImmersionId : String
        get() = profile?.currentImmersionId ?: AssetManagerHandlerCl.NoNarrativeLoaded
        set(value) { profile?.currentImmersionId = value }

    var settings : MutableList<ProfileSetting>
        get() = profile?.settings ?: mutableListOf()
        set(value) { profile?.settings = value }

    var cumlTime : String
        get() = profile?.cumlTime ?: ImmersionTimer.CumlTimeZero
        set(value) { profile?.cumlTime = value }

    fun cumlComponentTime() = if (isInitialized) timerPair.cumlImmersionTimer.immersionTime() else ImmersionTimer.CumlTimeZero

    override fun initialize(initData: Any?) {

        if (initData != null) {
            val profileComponentInit = IComponent.checkInitType<ProfileComponentInit>(initData)

            if (profileComponentInit != null) {

                profile = profileComponentInit.profile

                timerPair.cumlImmersionTimer.setPastStartTime(ImmersionTimer.inMilliseconds(profileComponentInit.cumlTime()))

                super.initialize(initData)

                activate()

                //update kcop with current settings, including setting log timers
                Switchboard.updateSettings(profileComponentInit.profile.settings)
            }
        }
    }

    fun activate() {
        if (isInitialized) {

            timerPair.instImmersionTimer!!.resetTimer()
            timerPair.instImmersionTimer.resumeTimer()
            timerPair.cumlImmersionTimer.resumeTimer()

            MessageChannelEnum.PROFILE_BRIDGE.enableReceive(this)
            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.RefreshCurrentProfile, null, this))
        }
    }

    fun inactivate() {
        if (isInitialized) {

            timerPair.instImmersionTimer!!.pauseTimer()
            timerPair.cumlImmersionTimer.pauseTimer()

            MessageChannelEnum.PROFILE_BRIDGE.disableReceive(this)
            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.RemoveCurrentProfile))

            isInitialized = false
        }
    }

    @Suppress("NewApi")
    override fun handleMessage(msg: Telegram?): Boolean {

        if (msg != null) {
            when {
                (MessageChannelEnum.PROFILE_BRIDGE.isType(msg.message)) -> {
                    val profileMessage: ProfileMessage = MessageChannelEnum.PROFILE_BRIDGE.receiveMessage(msg.extraInfo)

                    if (isValid(this)) {
                        when (profileMessage.profileMessageType) {
                            ProfileMessage.ProfileMessageType.UpdateImmersionId -> {
                                if (profileMessage.immersionId != null) {
                                    currentImmersionId = profileMessage.immersionId
                                }
                            }

                            ProfileMessage.ProfileMessageType.ReplaceCumlTimer -> {
                                MessageChannelEnum.ECS_ENGINE_COMPONENT_BRIDGE.send(
                                    null, EngineComponentMessage(
                                        EngineComponentMessage.EngineComponentMessageType.ReplaceComponent,
                                        ProfileEntity.entityName, ImmersionTimerComponent::class.java, this.timerPair
                                    )
                                )
                            }

                            ProfileMessage.ProfileMessageType.UpdateSettings -> {
                                if (profileMessage.settings != null) {
                                    settings = profileMessage.settings
                                }
                            }

                            ProfileMessage.ProfileMessageType.UpdateProfile -> {
                                if (profileMessage.profile != null) {
                                    profile = profileMessage.profile
                                }
                            }

                            ProfileMessage.ProfileMessageType.Inactivate -> {
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

        fun isValid(profileComponent: ProfileComponent?) : Boolean {
            return (profileComponent?.profile != null && profileComponent.isInitialized)
        }

        fun ecsInit(profile : Profile = Profile()) {
            //inactivate current narrative
            MessageChannelEnum.NARRATIVE_BRIDGE.send(null, NarrativeMessage(NarrativeMessage.NarrativeMessageType.Inactivate))
            //inactivate current profile
            MessageChannelEnum.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.Inactivate))

            MessageChannelEnum.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.ReplaceComponent,
                ProfileEntity.entityName, ProfileComponent::class.java,
                ProfileComponentInit(profile)
            ) )
            MessageChannelEnum.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.ReplaceComponent,
                ProfileEntity.entityName, IRLTimeComponent::class.java
            ) )
        }
    }

    data class ProfileComponentInit(val profile: Profile = Profile()) {
        fun cumlTime() = profile.cumlTime
    }
}