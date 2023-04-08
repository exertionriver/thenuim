package river.exertion.kcop.assets

import com.badlogic.gdx.ai.msg.Telegram
import river.exertion.kcop.simulation.view.displayViewMenus.params.NarrativeMenuDataParams
import river.exertion.kcop.simulation.view.displayViewMenus.params.ProfileMenuDataParams
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.*

object AssetManagerMessageHandler {

    fun AssetManagerHandler.messageHandler(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.AMH_LOAD_BRIDGE.isType(msg.message) ) -> {
                    val amhLoadMessage: AMHLoadMessage = MessageChannel.AMH_LOAD_BRIDGE.receiveMessage(msg.extraInfo)

                    when (amhLoadMessage.messageType) {
                        AMHLoadMessage.AMHLoadMessageType.ReloadMenuProfiles -> {
                            reloadProfileAssets()

                            val loadedProfileAssetTitles = if (profileAssets.values.isNotEmpty()) profileAssets.values.map { it.assetTitle() } else listOf("<no profiles found>")
                            MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(loadedProfileAssetTitles, loadedProfileAssetTitles[0])))
                        }
                        AMHLoadMessage.AMHLoadMessageType.ReloadMenuNarratives -> {
                            reloadNarrativeAssets()

                            val loadedNarrativeAssetTitles = if (narrativeAssets.values.isNotEmpty()) narrativeAssets.values.map { it.assetTitle() } else listOf("<no narratives found>")
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
                            reloadProfileAssets()
                            reloadNarrativeImmersionAssets()

                            if (amhLoadMessage.selectedTitle != null) {
                                selectedProfileAsset = profileAssets.byTitle(amhLoadMessage.selectedTitle)

                                if (selectedProfileAsset != null && selectedProfileAsset!!.profile != null) {
                                    selectedNarrativeAsset = narrativeAssets.byId(selectedProfileAsset!!.profile!!.currentImmersionId)
                                    selectedImmersionAsset = narrativeImmersionAssets.byIds(selectedProfileAsset?.assetId(), selectedNarrativeAsset?.assetId())

                                    selectedProfileAsset!!.update(selectedNarrativeAsset, selectedImmersionAsset)

                                    MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, selectedProfileAsset!!.assetInfo(), selectedProfileAsset!!.assetName())))
                                }
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.SetSelectedNarrativeAsset -> {
                            reloadNarrativeAssets()
                            reloadNarrativeImmersionAssets()

                            if (amhLoadMessage.selectedTitle != null) {
                                selectedNarrativeAsset = narrativeAssets.byTitle(amhLoadMessage.selectedTitle)
                                selectedImmersionAsset = narrativeImmersionAssets.byIds(selectedProfileAsset?.assetId(), selectedNarrativeAsset?.assetId())

                                if (selectedNarrativeAsset != null) {
                                    MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(null, NarrativeMenuDataParams(null, null, selectedNarrativeAsset!!.assetInfo(), selectedNarrativeAsset!!.assetName())))
                                }
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.RefreshSelectedProfile -> {
                            if (selectedProfileAsset != null && currentProfileComponent != null && currentImmersionComponent != null) {

                                selectedProfileAsset?.update(currentProfileComponent, currentImmersionComponent)

                                MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(selectedProfileAsset?.profile?.settings?.map { it.key }, null, selectedProfileAsset?.profile?.settings?.map { it.value })))
                                MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, selectedProfileAsset!!.assetInfo(), selectedProfileAsset!!.assetName())))
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.RefreshSelectedNarrative -> {
                            if (selectedNarrativeAsset != null && currentImmersionComponent != null) {

                                selectedNarrativeAsset!!.update(currentImmersionComponent)

                                MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(null, NarrativeMenuDataParams(null, null, selectedNarrativeAsset!!.assetInfo(), selectedNarrativeAsset!!.assetName())))
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.LoadSelectedProfile -> {
                            initSelectedProfile()
                        }
                        AMHLoadMessage.AMHLoadMessageType.LoadSelectedNarrative -> {
                            initSelectedNarrative()
                        }
                    }

                    return true
                }
                (MessageChannel.AMH_SAVE_BRIDGE.isType(msg.message) ) -> {
                    val amhSaveMessage: AMHSaveMessage = MessageChannel.AMH_SAVE_BRIDGE.receiveMessage(msg.extraInfo)

                    when (amhSaveMessage.messageType) {
                        AMHSaveMessage.AMHSaveMessageType.SaveOverwriteProfile -> {
                            if (selectedProfileAsset != null && amhSaveMessage.saveName != null) {

                                selectedProfileAsset!!.profile!!.name = amhSaveMessage.saveName

                                selectedProfileAsset!!.save(selectedProfileAsset!!.newAssetFilename())
                            }
                        }
                        AMHSaveMessage.AMHSaveMessageType.SaveMergeProfile -> {}
                        AMHSaveMessage.AMHSaveMessageType.NewProfile -> {
                            if (amhSaveMessage.saveName != null) {
                                selectedProfileAsset = ProfileAsset.new(amhSaveMessage.saveName)

                                selectedProfileAsset!!.save()

                                MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UpdateProfile, selectedProfileAsset!!.profile))

                                Switchboard.noloadNarrative()
                            }
                        }

                        AMHSaveMessage.AMHSaveMessageType.PrepSaveProgress -> {
                            reloadProfileAssets()
                            reloadNarrativeAssets()
                            reloadNarrativeImmersionAssets()

                            if (currentProfileComponent != null) {
                                selectedProfileAsset = profileAssets.byId(currentProfileComponent!!.componentId())

                                if (selectedProfileAsset != null) {
                                    selectedProfileAsset!!.update(currentProfileComponent, currentImmersionComponent)
                                } else {
                                    selectedProfileAsset = ProfileAsset.new()

                                    MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UpdateProfile, selectedProfileAsset!!.profile))
                                }
                            } else {
                                selectedProfileAsset = ProfileAsset.new()

                                selectedProfileAsset!!.initProfile()
                            }

                            val profileInfo = selectedProfileAsset!!.assetInfo()

                            //update from current immersion if one is active
                            if (currentImmersionComponent != null) {
                                selectedNarrativeAsset = narrativeAssets.byId(currentImmersionComponent!!.componentId())

                                if (selectedNarrativeAsset != null) {
                                    selectedNarrativeAsset!!.update(currentImmersionComponent)

                                    selectedImmersionAsset = narrativeImmersionAssets.byIds(selectedProfileAsset!!.assetId(), selectedNarrativeAsset!!.assetId())

                                    //create immersion asset if it does not exist
                                    if (selectedImmersionAsset != null) {
                                        selectedImmersionAsset!!.update(currentImmersionComponent)
                                    } else {
                                        selectedImmersionAsset = NarrativeImmersionAsset.new(currentProfileComponent!!, currentImmersionComponent!!)

                                        MessageChannel.NARRATIVE_BRIDGE.send(null, NarrativeMessage(NarrativeMessage.NarrativeMessageType.UpdateNarrativeImmersion, null, selectedImmersionAsset!!.narrativeImmersion))
                                    }
                                }
                            }

                            MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, profileInfo), NarrativeMenuDataParams(null, null, null)))
                        }

                        AMHSaveMessage.AMHSaveMessageType.SaveProgress -> {
                            selectedProfileAsset!!.save()
                            selectedImmersionAsset?.save()
                        }
                        AMHSaveMessage.AMHSaveMessageType.RestartProgress -> {
                            selectedImmersionAsset?.delete()
                            selectedImmersionAsset = null

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