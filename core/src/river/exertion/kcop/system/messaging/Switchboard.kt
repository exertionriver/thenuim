package river.exertion.kcop.system.messaging

import river.exertion.kcop.assets.AssetManagerHandler
import river.exertion.kcop.simulation.view.displayViewMenus.MainMenu
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuNavParams
import river.exertion.kcop.system.ecs.component.*
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.messaging.messages.*
import river.exertion.kcop.system.profile.settings.PSShowTimer
import river.exertion.kcop.system.profile.ProfileSetting
import river.exertion.kcop.system.profile.settings.PSCompStatus

object Switchboard {

    fun closeMenu() {
        //reset UI controls
        MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(null, 0, false))
        MessageChannel.PAUSE_VIEW_BRIDGE.send(null, PauseViewMessage(false))
        MessageChannel.MENU_VIEW_BRIDGE.send(null, DisplayViewMenuMessage(null, 0, false))
    }

    fun clearMenu() {
        //clear menu state
        MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage())
        MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage())
        //put nav back on main menu
        MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(MainMenu.tag))
    }

    fun openMenu() {
        clearMenu()
        MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(null, 0, true))
        MessageChannel.PAUSE_VIEW_BRIDGE.send(null, PauseViewMessage(true))
        MessageChannel.MENU_VIEW_BRIDGE.send(null, DisplayViewMenuMessage(null, 0, true))
    }

    fun loadSelectedProfile() {
        MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.InitSelectedProfile))
    }

    fun loadSelectedNarrative() {
        MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.InitSelectedNarrative))
    }

    fun saveOverwriteSelectedProfile(saveName : String) {

        MessageChannel.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(
            AMHSaveMessage.AMHSaveMessageType.SaveOverwriteProfile, saveName)
        )

    }

    fun newProfile(saveName : String) {

        MessageChannel.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(
            AMHSaveMessage.AMHSaveMessageType.NewProfile, saveName)
        )
    }

    fun noloadProfile() {
        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.RemoveComponent,
                ProfileEntity.entityName, ProfileComponent::class.java))
        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, AssetManagerHandler.NoProfileLoaded))
        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.RemoveComponent,
                ProfileEntity.entityName, IRLTimeComponent::class.java))
        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.ResetTime))
    }

    fun noloadNarrative() {
        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.RemoveComponent,
                ProfileEntity.entityName, NarrativeComponent::class.java))
        MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(TextViewMessage.TextViewMessageType.ReportText, AssetManagerHandler.NoNarrativeLoaded))
        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.RemoveComponent,
                ProfileEntity.entityName, ImmersionTimerComponent::class.java))
        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.ResetTime))
    }

    fun updateSettings(newSettings : MutableList<ProfileSetting>) {

        newSettings.forEach { profileSetting ->
            when (profileSetting.key) {
                PSShowTimer.selectionKey -> PSShowTimer.PSShowTimerOptions.byTag(profileSetting.value)?.exec()
                PSCompStatus.selectionKey -> PSCompStatus.PSCompStatusOptions.byTag(profileSetting.value)?.exec()
                else -> {}
            }
        }

        MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UpdateSettings, null, null, newSettings))
    }

}