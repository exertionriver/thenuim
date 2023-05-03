package river.exertion.kcop.assets

import com.badlogic.gdx.ai.msg.Telegram
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.simulation.view.displayViewMenus.params.NarrativeMenuDataParams
import river.exertion.kcop.simulation.view.displayViewMenus.params.ProfileMenuDataParams
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent
import river.exertion.kcop.system.messaging.messages.*
import river.exertion.kcop.system.profile.Profile

object AssetManagerMessageHandler {

    fun AssetManagerHandlerCl.messageHandler(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelEnum.AMH_LOAD_BRIDGE.isType(msg.message) ) -> {
                    val amhLoadMessage: AMHLoadMessage = MessageChannelEnum.AMH_LOAD_BRIDGE.receiveMessage(msg.extraInfo)

                    when (amhLoadMessage.messageType) {
                        AMHLoadMessage.AMHLoadMessageType.ReloadMenuProfiles -> {
                            reloadProfileAssets()

                            val loadedProfileAssetTitles = loadedProfileAssetTitles()
                            MessageChannelEnum.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(loadedProfileAssetTitles, loadedProfileAssetTitles[0])))
                        }
                        AMHLoadMessage.AMHLoadMessageType.ReloadMenuNarratives -> {
                            reloadNarrativeAssets()

                            val loadedNarrativeAssetTitles = loadedNarrativeAssetTitles()
                            MessageChannelEnum.INTER_MENU_BRIDGE.send(null, MenuDataMessage(null, NarrativeMenuDataParams(loadedNarrativeAssetTitles, loadedNarrativeAssetTitles[0])))
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
                        AMHLoadMessage.AMHLoadMessageType.SetSelectedProfileFromAsset -> {
                            if (amhLoadMessage.selectedTitle != null) {
                                reloadProfileAssets()
                                reloadNarrativeAssets()
                                reloadNarrativeImmersionAssets()

                                selectedProfileAsset = profileAssets.byTitle(amhLoadMessage.selectedTitle)

                                if (ProfileAsset.isValid(selectedProfileAsset) ) {
                                    selectedNarrativeAsset = narrativeAssets.byId(selectedProfileAsset!!.profile!!.currentImmersionId)
                                    selectedImmersionAsset = narrativeImmersionAssets.byIds(selectedProfileAsset?.assetId(), selectedNarrativeAsset?.assetId())

                                    selectedProfileAsset!!.updateFromImmersionAssets(selectedNarrativeAsset, selectedImmersionAsset)

                                    MessageChannelEnum.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, selectedProfileAsset!!.assetInfo(), selectedProfileAsset!!.assetName())))
                                }
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.SetSelectedNarrativeFromAsset -> {
                            if (amhLoadMessage.selectedTitle != null) {
                                reloadNarrativeAssets()

                                selectedNarrativeAsset = narrativeAssets.byTitle(amhLoadMessage.selectedTitle)

                                if (NarrativeAsset.isValid(selectedNarrativeAsset)) {
                                    MessageChannelEnum.INTER_MENU_BRIDGE.send(null, MenuDataMessage(null, NarrativeMenuDataParams(null, null, selectedNarrativeAsset!!.assetInfo(), selectedNarrativeAsset!!.assetName())))
                                }
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.UpdateSelectedProfileFromComponents -> {
                            if (ProfileAsset.isValid(selectedProfileAsset) && ProfileComponent.isValid(currentProfileComponent) && NarrativeComponent.isValid(currentImmersionComponent) ) {

                                selectedProfileAsset!!.update(currentProfileComponent!!, currentImmersionComponent!!)

                                MessageChannelEnum.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, selectedProfileAsset!!.assetInfo(), selectedProfileAsset!!.assetName())))

                                //workaround for settings key-value pairs
                                MessageChannelEnum.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(selectedProfileAsset?.profile?.settings?.map { it.key }, null, selectedProfileAsset?.profile?.settings?.map { it.value })))
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.UpdateSelectedNarrativeFromComponent -> {
                            if (NarrativeComponent.isValid(currentImmersionComponent) ) {
                                reloadNarrativeAssets()

                                selectedNarrativeAsset = narrativeAssets.byId(currentImmersionComponent!!.componentId())

                                MessageChannelEnum.INTER_MENU_BRIDGE.send(null, MenuDataMessage(null, NarrativeMenuDataParams(null, null, selectedNarrativeAsset!!.assetInfo(), selectedNarrativeAsset!!.assetName())))
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
                (MessageChannelEnum.AMH_SAVE_BRIDGE.isType(msg.message) ) -> {
                    val amhSaveMessage: AMHSaveMessage = MessageChannelEnum.AMH_SAVE_BRIDGE.receiveMessage(msg.extraInfo)

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

                                selectedProfileAsset = profileAssets.byTitle(IAsset.newAssetFilename(currentProfileComponent!!.profile!!.name, currentProfileComponent!!.componentId()))

                                //if an asset related to the profile is found, update, otherwise create an asset and update component with new profile
                                if (ProfileAsset.isValid(selectedProfileAsset)) {
                                    selectedProfileAsset!!.update(currentProfileComponent!!, currentImmersionComponent)
                                } else {
                                    selectedProfileAsset = ProfileAsset.new(currentProfileComponent!!, currentImmersionComponent)
                                }
                            } else throw Exception("AssetManagerHandler.messageHandler: ProfileComponent not valid")

                            //if a narrative is inited and active
                            if (NarrativeComponent.isValid(currentImmersionComponent)) {
                                reloadNarrativeImmersionAssets()

                                selectedImmersionAsset = narrativeImmersionAssets.byIds(selectedProfileAsset!!.assetId(), currentImmersionComponent!!.componentId())

                                //update immersion asset or create new if one does not exist
                                if (NarrativeImmersionAsset.isValid(selectedImmersionAsset)) {
                                    selectedImmersionAsset!!.update(currentImmersionComponent)
                                } else {
                                    selectedImmersionAsset = NarrativeImmersionAsset.new(currentImmersionComponent!!)
                                }
                            }

                            //profile asset info should be valid now, send info
                            val profileInfo = selectedProfileAsset!!.assetInfo()
                            MessageChannelEnum.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, profileInfo)))

                        }

                        AMHSaveMessage.AMHSaveMessageType.SaveProgress -> {
                            if (ProfileAsset.isValid(selectedProfileAsset)) {
                                selectedProfileAsset!!.save()
                            }

                            if (NarrativeImmersionAsset.isValid(selectedImmersionAsset)) {
                                MessageChannelEnum.NARRATIVE_BRIDGE.send(null, NarrativeMessage(NarrativeMessage.NarrativeMessageType.UpdateNarrativeImmersion, null, selectedImmersionAsset!!.narrativeImmersion))

                                selectedImmersionAsset!!.save()
                            }
                        }
                        AMHSaveMessage.AMHSaveMessageType.RestartProgress -> {
                            if ( NarrativeImmersionAsset.isValid(selectedImmersionAsset) ) {
                                selectedImmersionAsset?.delete()
                                selectedImmersionAsset = null
                            }

                            initSelectedNarrative()
                        }
                    }
                    return true
                }
            }
        }
        return false
    }
}