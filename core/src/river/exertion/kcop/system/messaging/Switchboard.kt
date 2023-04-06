package river.exertion.kcop.system.messaging

import river.exertion.kcop.assets.AssetManagerHandler
import river.exertion.kcop.simulation.view.ctrl.LogViewCtrl
import river.exertion.kcop.simulation.view.ctrl.TextViewCtrl
import river.exertion.kcop.simulation.view.displayViewMenus.MainMenu
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuNavParams
import river.exertion.kcop.system.ecs.component.IRLTimeComponent
import river.exertion.kcop.system.ecs.component.ImmersionTimerComponent
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.messaging.messages.*

object Switchboard {

    fun closeMenu() {
        //reset UI controls
        MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(0, false))
        MessageChannel.PAUSE_VIEW_BRIDGE.send(null, PauseViewMessage(false))
        MessageChannel.MENU_VIEW_BRIDGE.send(null, DisplayViewMenuMessage(0, false))
    }

    fun clearMenu() {
        //clear menu state
        MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage())
        MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage())
        //put nav back on main menu
        MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(MainMenu.tag)))
    }

    fun openMenu() {
        clearMenu()
        MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(0, true))
        MessageChannel.PAUSE_VIEW_BRIDGE.send(null, PauseViewMessage(true))
        MessageChannel.MENU_VIEW_BRIDGE.send(null, DisplayViewMenuMessage(0, true))
    }

    fun loadSelectedProfile() {
        MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.LoadSelectedProfile))
    }

    fun loadSelectedNarrative() {
        MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.LoadSelectedNarrative))
    }

    fun saveSelectedProfile(saveName : String, saveType : AssetManagerHandler.SaveType) {

        when (saveType) {
            //overwrite-save profile
            AssetManagerHandler.SaveType.Overwrite -> {
                MessageChannel.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(
                    AMHSaveMessage.AMHSaveMessageType.SaveOverwriteProfile, null, null, saveName)
                )
            }
            else -> {}
        }
    }

    fun newProfile(saveName : String) {

        MessageChannel.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(
            AMHSaveMessage.AMHSaveMessageType.NewProfile, null, null, saveName)
        )
    }

    fun noloadProfile() {
        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessageType.REMOVE_COMPONENT,
                ProfileEntity.entityName, ProfileComponent::class.java))
        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, LogViewCtrl.NoProfileLoaded))
        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessageType.REMOVE_COMPONENT,
                ProfileEntity.entityName, IRLTimeComponent::class.java))
        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.ResetTime))
    }

    fun noloadNarrative() {
        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessageType.REMOVE_COMPONENT,
                ProfileEntity.entityName, NarrativeComponent::class.java))
        MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(TextViewCtrl.NoNarrativeLoaded))
        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessageType.REMOVE_COMPONENT,
                ProfileEntity.entityName, ImmersionTimerComponent::class.java))
        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.ResetTime))
    }
}