package river.exertion.kcop.system.messaging

import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.messaging.messages.*

object Switchboard {

    fun closeMenu() {
        MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(0, false))
        MessageChannel.PAUSE_VIEW_BRIDGE.send(null, PauseViewMessage(false))
        MessageChannel.MENU_VIEW_BRIDGE.send(null, DisplayViewMenuMessage(0, false))
    }

    fun openMenu() {
        MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(0, true))
        MessageChannel.PAUSE_VIEW_BRIDGE.send(null, PauseViewMessage(true))
        MessageChannel.MENU_VIEW_BRIDGE.send(null, DisplayViewMenuMessage(0, true))
    }

    fun updateProfile(narrativeComponent : NarrativeComponent) {
        MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UPDATE_BLOCK_ID, narrativeComponent.narrativeId(), narrativeComponent.narrativeCurrBlockId()))
        MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UPDATE_STATUS, narrativeComponent.sequentialStatusKey(), narrativeComponent.seqNarrativeProgress().toString()))
        MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UPDATE_CUML_TIME, narrativeComponent.narrativeId(), narrativeComponent.narrativeImmersionTimer.cumlImmersionTimer.immersionTime()))
    }
}