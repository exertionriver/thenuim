package river.exertion.kcop.system.messaging

import river.exertion.kcop.assets.AssetManagerHandler
import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.messaging.messages.*
import river.exertion.kcop.system.profile.Profile

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
        MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UPDATE_STATUS, narrativeComponent.narrativeId(), narrativeComponent.seqNarrativeProgress().toString()))
    }

    fun newProfile(entityName : String) {
        //instantiate profile
        MessageChannel.ECS_ENGINE_ENTITY_BRIDGE.send(null, EngineEntityMessage(
            EngineEntityMessageType.INSTANTIATE_ENTITY,
            ProfileEntity::class.java, entityName)
        )
    }

    fun loadProfile(profileAsset : ProfileAsset, narrativeAsset : NarrativeAsset?) {
        //inactivate narrative
        MessageChannel.NARRATIVE_BRIDGE.send(null, NarrativeMessage(NarrativeMessage.NarrativeMessageType.INACTIVATE))

        //clear profile entities
        MessageChannel.ECS_ENGINE_ENTITY_BRIDGE.send(null, EngineEntityMessage(
            EngineEntityMessageType.REMOVE_ALL_ENTITIES,
            ProfileEntity::class.java)
        )

        //instantiate profile
        MessageChannel.ECS_ENGINE_ENTITY_BRIDGE.send(null, EngineEntityMessage(
            EngineEntityMessageType.INSTANTIATE_ENTITY,
            ProfileEntity::class.java, profileAsset)
        )

        //add current narrative
        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
            EngineComponentMessageType.ADD_COMPONENT,
            profileAsset.profile!!.id, NarrativeComponent::class.java, null,

            if (narrativeAsset != null) {
            NarrativeComponent.NarrativeComponentInit(narrativeAsset,
                profileAsset.profile!!.currentImmersionBlockId!!,
                profileAsset.profile!!.statuses.firstOrNull { it.key == narrativeAsset.narrative?.id }?.cumlImmersionTime)
            } else null
        ) )
    }

    fun saveProfile(newName : String, profileAsset : ProfileAsset, currentProfile : Profile, saveType : AssetManagerHandler.SaveType) {

        when (saveType) {
            //overwrite-save profile
            AssetManagerHandler.SaveType.Overwrite -> {
                MessageChannel.AMH_BRIDGE.send(null, AMHMessage(
                    AMHMessage.AMHMessageType.SaveProfile, profileAsset.apply { this.profile = currentProfile; this.profile!!.name = newName })
                )
            }
            else -> {}
        }
    }
}