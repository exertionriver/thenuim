package river.exertion.kcop.system.messaging

import river.exertion.kcop.assets.AssetManagerHandler
import river.exertion.kcop.simulation.view.displayViewMenus.MainMenu
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuNavParams
import river.exertion.kcop.system.ecs.component.NarrativeComponent
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
        MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(0, true))
        MessageChannel.PAUSE_VIEW_BRIDGE.send(null, PauseViewMessage(true))
        MessageChannel.MENU_VIEW_BRIDGE.send(null, DisplayViewMenuMessage(0, true))
    }

    fun updateProfile(narrativeComponent : NarrativeComponent) {
        MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UPDATE_BLOCK_ID, narrativeComponent.narrativeName(), narrativeComponent.narrativeCurrBlockId()))
        MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UPDATE_STATUS, narrativeComponent.narrativeName(), narrativeComponent.seqNarrativeProgress().toString()))
    }

    fun newProfile(entityName : String) {
        //instantiate profile
        MessageChannel.ECS_ENGINE_ENTITY_BRIDGE.send(null, EngineEntityMessage(
            EngineEntityMessageType.INSTANTIATE_ENTITY,
            ProfileEntity::class.java, entityName)
        )
    }

    fun loadSelectedProfile() {
        MessageChannel.AMH_BRIDGE.send(null, AMHMessage(AMHMessage.AMHMessageType.LoadSelectedProfile))
    }

    fun loadSelectedNarrative() {
        MessageChannel.AMH_BRIDGE.send(null, AMHMessage(AMHMessage.AMHMessageType.LoadSelectedNarrative))
    }

    fun saveSelectedProfile(saveName : String, saveType : AssetManagerHandler.SaveType) {

        when (saveType) {
            //overwrite-save profile
            AssetManagerHandler.SaveType.Overwrite -> {
                MessageChannel.AMH_BRIDGE.send(null, AMHMessage(
                    AMHMessage.AMHMessageType.SaveOverwriteProfile, null, null, saveName)
                )
            }
            else -> {}
        }
    }
}