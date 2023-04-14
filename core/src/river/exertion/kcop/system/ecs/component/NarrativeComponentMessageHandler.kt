package river.exertion.kcop.system.ecs.component

import com.badlogic.gdx.ai.msg.Telegram
import river.exertion.kcop.narrative.structure.ImmersionStatus
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.inactivate
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.next
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.pause
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.unpause
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.*

object NarrativeComponentMessageHandler {

    @Suppress("NewApi")
    fun NarrativeComponent.messageHandler(msg: Telegram?): Boolean {
        if (msg != null) {
            if (MessageChannel.NARRATIVE_BRIDGE.isType(msg.message) && isInitialized ) {
                val narrativeMessage: NarrativeMessage = MessageChannel.NARRATIVE_BRIDGE.receiveMessage(msg.extraInfo)

                when (narrativeMessage.narrativeMessageType) {
                    NarrativeMessage.NarrativeMessageType.UpdateNarrativeImmersion -> {
                        if (narrativeMessage.narrativeImmersion != null) {
                            narrativeImmersion = narrativeMessage.narrativeImmersion
                        }
                    }
                    NarrativeMessage.NarrativeMessageType.ReplaceCumlTimer -> {
                        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                                EngineComponentMessage.EngineComponentMessageType.ReplaceComponent,
                                ProfileEntity.entityName, ImmersionTimerComponent::class.java, this.timerPair))
                    }
                    NarrativeMessage.NarrativeMessageType.Pause -> pause()
                    NarrativeMessage.NarrativeMessageType.Unpause -> unpause()
                    NarrativeMessage.NarrativeMessageType.Inactivate -> inactivate()
                    NarrativeMessage.NarrativeMessageType.Next -> if (narrativeMessage.promptNext != null) next(narrativeMessage.promptNext)
                }
                return true
            }
            if (MessageChannel.NARRATIVE_STATUS_BRIDGE.isType(msg.message) && isInitialized ) {
                val narrativeStatusMessage: NarrativeStatusMessage = MessageChannel.NARRATIVE_STATUS_BRIDGE.receiveMessage(msg.extraInfo)

                when (narrativeStatusMessage.narrativeStatusMessageType) {
                    NarrativeStatusMessage.NarrativeFlagsMessageType.AddStatus -> {
                        //completion status
                        if (narrativeStatusMessage.key == null) {
                            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessage.StatusViewMessageType.AddStatus, sequentialStatusKey(), seqNarrativeProgress()))
                        }
                    }
                    NarrativeStatusMessage.NarrativeFlagsMessageType.RemoveStatus -> {
                        //completion status
                        if (narrativeStatusMessage.key == null) {
                            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessage.StatusViewMessageType.RemoveStatus, sequentialStatusKey()))
                        }
                    }
                }
                return true
            }
            if (MessageChannel.NARRATIVE_FLAGS_BRIDGE.isType(msg.message) && isInitialized ) {
                val narrativeFlagsMessage: NarrativeFlagsMessage = MessageChannel.NARRATIVE_FLAGS_BRIDGE.receiveMessage(msg.extraInfo)

                when (narrativeFlagsMessage.narrativeFlagsMessageType) {
                    NarrativeFlagsMessage.NarrativeFlagsMessageType.SetFlag -> {
                        if (narrativeImmersion!!.flags.any { it.key == narrativeFlagsMessage.key }) {
                            narrativeImmersion!!.flags.first { it.key == narrativeFlagsMessage.key }.value = narrativeFlagsMessage.value
                        } else {
                            narrativeImmersion!!.flags.add(ImmersionStatus(narrativeFlagsMessage.key, narrativeFlagsMessage.value))
                        }
                    }
                    NarrativeFlagsMessage.NarrativeFlagsMessageType.AddToCounter -> {
                        if (narrativeImmersion!!.flags.any { it.key == narrativeFlagsMessage.key && (it.value?.toIntOrNull() is Int) }) {
                            narrativeImmersion!!.flags.first { it.key == narrativeFlagsMessage.key }.value =
                                    (narrativeImmersion!!.flags.first { it.key == narrativeFlagsMessage.key }.value!!.toInt() + (narrativeFlagsMessage.value?.toIntOrNull() ?: 0)).toString()
                        } else {
                            narrativeImmersion!!.flags.add(ImmersionStatus(narrativeFlagsMessage.key, narrativeFlagsMessage.value))
                        }
                    }
                    NarrativeFlagsMessage.NarrativeFlagsMessageType.UnsetFlag -> {
                        narrativeImmersion!!.flags.removeIf { it.key == narrativeFlagsMessage.key }
                    }
                }
                return true
            }
            if (MessageChannel.NARRATIVE_MEDIA_BRIDGE.isType(msg.message) && isInitialized ) {
                val narrativeMediaMessage: NarrativeMediaMessage = MessageChannel.NARRATIVE_MEDIA_BRIDGE.receiveMessage(msg.extraInfo)

                when (narrativeMediaMessage.narrativeMediaMessageType) {
                    NarrativeMediaMessage.NarrativeMediaMessageType.PlaySound -> {
                        if ( narrative!!.sounds.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                    DisplayViewAudioMessage.DisplayViewAudioMessageType.PlaySound, narrative!!.sounds[narrativeMediaMessage.assetFilename]!!.asset)
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.PlayMusic -> {
                        if ( narrative!!.music.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                    DisplayViewAudioMessage.DisplayViewAudioMessageType.PlayMusic, narrative!!.music[narrativeMediaMessage.assetFilename]!!.asset)
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.StopMusic -> {
                        if ( narrative!!.music.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                    DisplayViewAudioMessage.DisplayViewAudioMessageType.StopMusic, null)
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.FadeInMusic -> {
                        if ( narrative!!.music.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                    DisplayViewAudioMessage.DisplayViewAudioMessageType.FadeInMusic, narrative!!.music[narrativeMediaMessage.assetFilename]!!.asset)
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.FadeOutMusic -> {
                        if ( narrative!!.music.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                    DisplayViewAudioMessage.DisplayViewAudioMessageType.FadeOutMusic, null)
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.CrossFadeMusic -> {
                        if ( narrative!!.music.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                    DisplayViewAudioMessage.DisplayViewAudioMessageType.CrossFadeMusic, narrative!!.music[narrativeMediaMessage.assetFilename]!!.asset)
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.ShowImage -> {
                        if ( narrative!!.textures.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                    DisplayViewTextureMessage.DisplayViewTextureMessageType.ShowImage, narrative!!.textures[narrativeMediaMessage.assetFilename]!!.asset, narrativeMediaMessage.imageLayoutPaneIdx?.toInt())
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.HideImage -> {
                        if ( narrative!!.textures.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                    DisplayViewTextureMessage.DisplayViewTextureMessageType.HideImage, null, narrativeMediaMessage.imageLayoutPaneIdx?.toInt())
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.FadeInImage -> {
                        if ( narrative!!.textures.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                    DisplayViewTextureMessage.DisplayViewTextureMessageType.FadeInImage, narrative!!.textures[narrativeMediaMessage.assetFilename]!!.asset, narrativeMediaMessage.imageLayoutPaneIdx?.toInt())
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.FadeOutImage -> {
                        if ( narrative!!.textures.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                    DisplayViewTextureMessage.DisplayViewTextureMessageType.FadeOutImage, null, narrativeMediaMessage.imageLayoutPaneIdx?.toInt())
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.CrossFadeImage -> {
                        if ( narrative!!.textures.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                    DisplayViewTextureMessage.DisplayViewTextureMessageType.CrossFadeImage, narrative!!.textures[narrativeMediaMessage.assetFilename]!!.asset, narrativeMediaMessage.imageLayoutPaneIdx?.toInt())
                            )
                        }
                    }
                }
                return true
            }
        }

        return false
    }
}