package river.exertion.kcop.system.messaging

import river.exertion.kcop.assets.AssetManagerHandlerCl
import river.exertion.kcop.simulation.view.displayViewMenus.MainMenu
import river.exertion.kcop.system.ecs.component.*
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.messaging.messages.*
import river.exertion.kcop.system.profile.settings.PSShowTimer
import river.exertion.kcop.system.profile.ProfileSetting
import river.exertion.kcop.system.profile.settings.PSCompStatus
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.LogViewMessage
import river.exertion.kcop.view.messaging.PauseViewMessage
import river.exertion.kcop.view.messaging.TextViewMessage

object Switchboard {

    fun closeMenu() {
        //reset UI controls
        MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, MenuViewMessage(null, 0, false))
        MessageChannelEnum.PAUSE_VIEW_BRIDGE.send(null, PauseViewMessage(false))
        MessageChannelEnum.MENU_VIEW_BRIDGE.send(null, MenuViewMessage(null, 0, false))
    }

    fun clearMenu() {
        //clear menu state
        MessageChannelEnum.INTER_MENU_BRIDGE.send(null, MenuDataMessage())
        MessageChannelEnum.INTRA_MENU_BRIDGE.send(null, MenuNavMessage())
        //put nav back on main menu
        MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, MenuViewMessage(MainMenu.tag))
    }

    fun openMenu() {
        clearMenu()
        MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, MenuViewMessage(null, 0, true))
        MessageChannelEnum.PAUSE_VIEW_BRIDGE.send(null, PauseViewMessage(true))
        MessageChannelEnum.MENU_VIEW_BRIDGE.send(null, MenuViewMessage(null, 0, true))
    }

    fun loadSelectedProfile() {
        MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.InitSelectedProfile))
    }

    fun loadSelectedNarrative() {
        MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.InitSelectedNarrative))
    }

    fun saveOverwriteSelectedProfile(saveName : String) {

        MessageChannelEnum.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(
            AMHSaveMessage.AMHSaveMessageType.SaveOverwriteProfile, saveName)
        )

    }

    fun newProfile(saveName : String) {

        MessageChannelEnum.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(
            AMHSaveMessage.AMHSaveMessageType.NewProfile, saveName)
        )
    }

    fun noloadProfile() {
        MessageChannelEnum.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.RemoveComponent,
                ProfileEntity.entityName, ProfileComponent::class.java))
        MessageChannelEnum.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, AssetManagerHandlerCl.NoProfileLoaded))
        MessageChannelEnum.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.RemoveComponent,
                ProfileEntity.entityName, IRLTimeComponent::class.java))
        MessageChannelEnum.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.ResetTime))
    }

    fun noloadNarrative() {
        MessageChannelEnum.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.RemoveComponent,
                ProfileEntity.entityName, NarrativeComponent::class.java))
        MessageChannelEnum.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(TextViewMessage.TextViewMessageType.ReportText, AssetManagerHandlerCl.NoNarrativeLoaded))
        MessageChannelEnum.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.RemoveComponent,
                ProfileEntity.entityName, ImmersionTimerComponent::class.java))
        MessageChannelEnum.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.ResetTime))
    }

    fun updateSettings(newSettings : MutableList<ProfileSetting>) {

        newSettings.forEach { profileSetting ->
            when (profileSetting.key) {
                PSShowTimer.selectionKey -> PSShowTimer.PSShowTimerOptions.byTag(profileSetting.value)?.exec()
                PSCompStatus.selectionKey -> PSCompStatus.PSCompStatusOptions.byTag(profileSetting.value)?.exec()
                else -> {}
            }
        }

        MessageChannelEnum.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UpdateSettings, null, null, newSettings))
    }

}