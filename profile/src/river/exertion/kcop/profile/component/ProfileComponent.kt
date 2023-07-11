package river.exertion.kcop.profile.component

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.asset.immersionTimer.ImmersionTimer
import river.exertion.kcop.asset.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.ecs.component.IComponent
import river.exertion.kcop.ecs.component.ImmersionTimerComponent
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.profile.Profile
import river.exertion.kcop.profile.ProfileKlop.ProfileBridge
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.profile.messaging.ProfileComponentMessage
import river.exertion.kcop.profile.settings.ProfileSettingEntry

class ProfileComponent : IComponent, Telegraph {

    var profile : Profile
        get() = ProfileAsset.currentProfileAsset.profile
        set(value) { ProfileAsset.currentProfileAsset.profile = value }

    override var componentId = profile.id

    override var isInitialized = false

    val instTimer = ImmersionTimer()

    var settings : MutableList<ProfileSettingEntry>
        get() = profile.settingEntries
        set(value) { profile.settingEntries = value }

    var cumlTimer : ImmersionTimer
        get() = profile.cumlTimer
        set(value) { profile.cumlTimer = value }

    override fun initialize(initData : Any?) {

        super.initialize(initData)

        activate()

        profile.execSettings()

    }

    fun activate() {
        if (isInitialized) {

            instTimer.resetTimer()
            instTimer.startOrResumeTimer()

            cumlTimer.startOrResumeTimer()

            MessageChannelHandler.enableReceive(ProfileBridge, this)
        }
    }

    fun inactivate() {
        if (isInitialized) {

            instTimer.pauseTimer()
            cumlTimer.pauseTimer()

            MessageChannelHandler.disableReceive(ProfileBridge, this)

            isInitialized = false
        }
    }

    @Suppress("NewApi")
    override fun handleMessage(msg: Telegram?): Boolean {

        if (msg != null) {
            when {
                (MessageChannelHandler.isType(ProfileBridge, msg.message)) -> {
                    val profileComponentMessage: ProfileComponentMessage = MessageChannelHandler.receiveMessage(ProfileBridge, msg.extraInfo)

                    if (isValid(this)) {
                        when (profileComponentMessage.profileMessageType) {
                            ProfileComponentMessage.ProfileMessageType.ReplaceCumlTimer -> {
                                IComponent.ecsInit<ImmersionTimerComponent>(initData = ImmersionTimerPair(instTimer, cumlTimer))
                            }
                            ProfileComponentMessage.ProfileMessageType.Inactivate -> {
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

        fun isValid(profileComponent: ProfileComponent?) : Boolean {
            return (profileComponent?.profile != null && profileComponent.isInitialized)
        }
    }
}