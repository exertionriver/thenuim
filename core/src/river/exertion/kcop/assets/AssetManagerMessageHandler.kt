package river.exertion.kcop.assets

import com.badlogic.gdx.ai.msg.Telegram
import river.exertion.kcop.simulation.view.displayViewMenus.params.NarrativeMenuDataParams
import river.exertion.kcop.simulation.view.displayViewMenus.params.ProfileMenuDataParams
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.*
import river.exertion.kcop.system.profile.Profile

object AssetManagerMessageHandler {

    fun AssetManagerHandler.messageHandler(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.AMH_LOAD_BRIDGE.isType(msg.message) ) -> {
                    val amhLoadMessage: AMHLoadMessage = MessageChannel.AMH_LOAD_BRIDGE.receiveMessage(msg.extraInfo)

                    when (amhLoadMessage.messageType) {
                        AMHLoadMessage.AMHLoadMessageType.ReloadMenuProfiles -> {
                            reloadProfileAssets()

                            val loadedProfileAssetTitles = loadedProfileAssetTitles()
                            MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(loadedProfileAssetTitles, loadedProfileAssetTitles[0])))
                        }
                        AMHLoadMessage.AMHLoadMessageType.ReloadMenuNarratives -> {
                            reloadNarrativeAssets()

                            val loadedNarrativeAssetTitles = loadedNarrativeAssetTitles()
                            MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(null, NarrativeMenuDataParams(loadedNarrativeAssetTitles, loadedNarrativeAssetTitles[0])))
                        }
                        AMHLoadMessage.AMHLoadMessageType.RefreshCurrentProfile -> {
                            if (amhLoadMessage.loadComponent != null) {
                                currentProfileComponent = amhLoadMessage.loadComponent as ProfileComponent
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.RefreshCurrentImmersion -> {
                            if (amhLoadMessage.loadComponent != null) {
                                currentImmersionComponent = amhLoadMessage.loadComponent as NarrativeComponent
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.RemoveCurrentProfile -> {
                            currentProfileComponent = null
                        }
                        AMHLoadMessage.AMHLoadMessageType.RemoveCurrentImmersion -> {
                            currentImmersionComponent = null
                        }
                        AMHLoadMessage.AMHLoadMessageType.SetSelectedProfileAsset -> {
                            if (amhLoadMessage.selectedTitle != null) {
                                reloadProfileAssets()
                                reloadNarrativeImmersionAssets()

                                selectedProfileAsset = profileAssets.byTitle(amhLoadMessage.selectedTitle)

                                if (ProfileAsset.isValid(selectedProfileAsset) ) {
                                    selectedNarrativeAsset = narrativeAssets.byId(selectedProfileAsset!!.profile!!.currentImmersionId)
                                    selectedImmersionAsset = narrativeImmersionAssets.byIds(selectedProfileAsset?.assetId(), selectedNarrativeAsset?.assetId())

                                    selectedProfileAsset!!.update(selectedNarrativeAsset, selectedImmersionAsset)

                                    MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, selectedProfileAsset!!.assetInfo(), selectedProfileAsset!!.assetName())))
                                }
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.SetSelectedNarrativeAsset -> {
                            if (amhLoadMessage.selectedTitle != null) {
                                reloadNarrativeAssets()
                                reloadNarrativeImmersionAssets()

                                selectedNarrativeAsset = narrativeAssets.byTitle(amhLoadMessage.selectedTitle)

                                if (NarrativeAsset.isValid(selectedNarrativeAsset)) {
                                    MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(null, NarrativeMenuDataParams(null, null, selectedNarrativeAsset!!.assetInfo(), selectedNarrativeAsset!!.assetName())))
                                }
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.RefreshSelectedProfile -> {
                            if (ProfileAsset.isValid(selectedProfileAsset) && ProfileComponent.isValid(currentProfileComponent) && NarrativeComponent.isValid(currentImmersionComponent) ) {

                                selectedProfileAsset!!.fullUpdate(currentProfileComponent!!)
                                selectedProfileAsset!!.update(currentImmersionComponent!!)

                                MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, selectedProfileAsset!!.assetInfo(), selectedProfileAsset!!.assetName())))

                                //workaround for settings key-value pairs
                                MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(selectedProfileAsset?.profile?.settings?.map { it.key }, null, selectedProfileAsset?.profile?.settings?.map { it.value })))
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.RefreshSelectedNarrative -> {
                            if (NarrativeAsset.isValid(selectedNarrativeAsset) && NarrativeComponent.isValid(currentImmersionComponent) ) {

                                selectedNarrativeAsset!!.update(currentImmersionComponent)

                                MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(null, NarrativeMenuDataParams(null, null, selectedNarrativeAsset!!.assetInfo(), selectedNarrativeAsset!!.assetName())))
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.InitSelectedProfile -> {
                            initSelectedProfile()
                        }
                        AMHLoadMessage.AMHLoadMessageType.InitSelectedNarrative -> {
                            initSelectedNarrative()
                        }
                    }

                    return true
                }
                (MessageChannel.AMH_SAVE_BRIDGE.isType(msg.message) ) -> {
                    val amhSaveMessage: AMHSaveMessage = MessageChannel.AMH_SAVE_BRIDGE.receiveMessage(msg.extraInfo)

                    when (amhSaveMessage.messageType) {
                        AMHSaveMessage.AMHSaveMessageType.SaveOverwriteProfile -> {
                            if (ProfileAsset.isValid(selectedProfileAsset)) {

                                selectedProfileAsset!!.profile!!.name = amhSaveMessage.saveName ?: Profile.genName()

                                selectedProfileAsset!!.save(selectedProfileAsset!!.newAssetFilename())
                            }
                        }
                        AMHSaveMessage.AMHSaveMessageType.SaveMergeProfile -> {}
                        AMHSaveMessage.AMHSaveMessageType.NewProfile -> {
                            selectedProfileAsset = ProfileAsset.new(amhSaveMessage.saveName ?: Profile.genName())

                            selectedProfileAsset!!.save()

                            initSelectedProfile()
                        }

                        AMHSaveMessage.AMHSaveMessageType.PrepSaveProgress -> {

                            //if a profile is inited and active
                            if (ProfileComponent.isValid(currentProfileComponent)) {
                                reloadProfileAssets()

                                selectedProfileAsset = profileAssets.byId(currentProfileComponent!!.componentId())

                                //if an asset related to the profile is found, update, otherwise create an asset and update component with new profile
                                if (ProfileAsset.isValid(selectedProfileAsset)) {
                                    selectedProfileAsset!!.fullUpdate(currentProfileComponent!!)
                                    selectedProfileAsset!!.update(currentImmersionComponent)
                                } else {
                                    selectedProfileAsset = ProfileAsset.new()

                                    //pull over any settings or timer info from active profile
                                    selectedProfileAsset!!.infoUpdate(currentProfileComponent!!)
                                }
                            //otherwise, create an asset and init a profile component
                            } else {
                                selectedProfileAsset = ProfileAsset.new()

                            }

                            //if a narrative is inited and active
                            if (NarrativeComponent.isValid(currentImmersionComponent)) {
                                reloadNarrativeAssets()
                                reloadNarrativeImmersionAssets()

                                selectedNarrativeAsset = narrativeAssets.byId(currentImmersionComponent!!.componentId())

                                //if an asset related to the narrative is found, update
                                if (NarrativeAsset.isValid(selectedNarrativeAsset)) {

                                    selectedNarrativeAsset!!.update(currentImmersionComponent)

                                    selectedImmersionAsset = narrativeImmersionAssets.byIds(selectedProfileAsset!!.assetId(), selectedNarrativeAsset!!.assetId())

                                    //update immersion asset or create new if one does not exist
                                    if (NarrativeImmersionAsset.isValid(selectedImmersionAsset)) {
                                        selectedImmersionAsset!!.update(currentImmersionComponent)
                                    } else {
                                        selectedImmersionAsset = NarrativeImmersionAsset.new(currentImmersionComponent!!)
                                    }
                                }

                                selectedProfileAsset!!.update(currentImmersionComponent)
                            }

                            //profile asset info should be valid now, send info
                            val profileInfo = selectedProfileAsset!!.assetInfo()
                            MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, profileInfo)))

                        }

                        AMHSaveMessage.AMHSaveMessageType.SaveProgress -> {
                            if (ProfileAsset.isValid(selectedProfileAsset) && NarrativeImmersionAsset.isValid(selectedImmersionAsset)) {
                                MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UpdateProfile, selectedProfileAsset!!.profile))
                                MessageChannel.NARRATIVE_BRIDGE.send(null, NarrativeMessage(NarrativeMessage.NarrativeMessageType.UpdateNarrativeImmersion, null, selectedImmersionAsset!!.narrativeImmersion))

                                selectedProfileAsset!!.save()
                                selectedImmersionAsset!!.save()
                            }
                        }
                        AMHSaveMessage.AMHSaveMessageType.RestartProgress -> {
                            if (NarrativeAsset.isValid(selectedNarrativeAsset) && NarrativeImmersionAsset.isValid(selectedImmersionAsset) ) {
                                selectedImmersionAsset?.delete()
                                selectedImmersionAsset = null

                                initSelectedNarrative()
                            }
                        }
                    }
                    return true
                }
            }
        }
        return false
    }
}