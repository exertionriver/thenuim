package river.exertion.kcop.system.ecs.component

import com.badlogic.gdx.ai.msg.Telegram
import river.exertion.kcop.narrative.structure.ImmersionStatus
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.inactivate
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.next
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.pause
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.unpause
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.system.messaging.messages.*
import river.exertion.kcop.view.messaging.AudioViewMessage
import river.exertion.kcop.view.messaging.DisplayViewTextureMessage
import river.exertion.kcop.view.messaging.StatusViewMessage

object NarrativeComponentMessageHandler {

    @Suppress("NewApi")
    fun NarrativeComponent.messageHandler(msg: Telegram?): Boolean {
        if (msg != null) {
            if (MessageChannelEnum.NARRATIVE_BRIDGE.isType(msg.message) && isInitialized ) {
                val narrativeMessage: NarrativeMessage = MessageChannelEnum.NARRATIVE_BRIDGE.receiveMessage(msg.extraInfo)

                when (narrativeMessage.narrativeMessageType) {
                    NarrativeMessage.NarrativeMessageType.UpdateNarrativeImmersion -> {
                        if (narrativeMessage.narrativeImmersion != null) {
                            narrativeImmersion = narrativeMessage.narrativeImmersion
                        }
                    }
                    NarrativeMessage.NarrativeMessageType.ReplaceCumlTimer -> {
                        MessageChannelEnum.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
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
            if (MessageChannelEnum.NARRATIVE_STATUS_BRIDGE.isType(msg.message) && isInitialized ) {
                val narrativeStatusMessage: NarrativeStatusMessage = MessageChannelEnum.NARRATIVE_STATUS_BRIDGE.receiveMessage(msg.extraInfo)

                when (narrativeStatusMessage.narrativeStatusMessageType) {
                    NarrativeStatusMessage.NarrativeFlagsMessageType.AddStatus -> {
                        //completion status
                        if (narrativeStatusMessage.key == null) {
                            MessageChannelEnum.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessage.StatusViewMessageType.AddStatus, sequentialStatusKey(), seqNarrativeProgress()))
                        }
                    }
                    NarrativeStatusMessage.NarrativeFlagsMessageType.RemoveStatus -> {
                        //completion status
                        if (narrativeStatusMessage.key == null) {
                            MessageChannelEnum.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessage.StatusViewMessageType.RemoveStatus, sequentialStatusKey()))
                        }
                    }
                }
                return true
            }
            if (MessageChannelEnum.NARRATIVE_FLAGS_BRIDGE.isType(msg.message) && isInitialized ) {
                val narrativeFlagsMessage: NarrativeFlagsMessage = MessageChannelEnum.NARRATIVE_FLAGS_BRIDGE.receiveMessage(msg.extraInfo)

                when (narrativeFlagsMessage.narrativeFlagsMessageType) {
                    NarrativeFlagsMessage.NarrativeFlagsMessageType.SetPersistFlag -> {
                        if (narrativeImmersion!!.flags.any { it.key == narrativeFlagsMessage.key }) {
                            narrativeImmersion!!.flags.first { it.key == narrativeFlagsMessage.key }.value = narrativeFlagsMessage.value
                        } else {
                            narrativeImmersion!!.flags.add(ImmersionStatus(narrativeFlagsMessage.key, narrativeFlagsMessage.value))
                        }
                    }
                    NarrativeFlagsMessage.NarrativeFlagsMessageType.SetBlockFlag -> {
                        if (blockFlags.any { it.key == narrativeFlagsMessage.key }) {
                            blockFlags.first { it.key == narrativeFlagsMessage.key }.value = narrativeFlagsMessage.value
                        } else {
                            blockFlags.add(ImmersionStatus(narrativeFlagsMessage.key, narrativeFlagsMessage.value))
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
                    NarrativeFlagsMessage.NarrativeFlagsMessageType.UnsetPersistFlag -> {
                        narrativeImmersion!!.flags.removeIf { it.key == narrativeFlagsMessage.key }
                    }
                }
                return true
            }
            if (MessageChannelEnum.NARRATIVE_MEDIA_BRIDGE.isType(msg.message) && isInitialized ) {
                val narrativeMediaMessage: NarrativeMediaMessage = MessageChannelEnum.NARRATIVE_MEDIA_BRIDGE.receiveMessage(msg.extraInfo)

                when (narrativeMediaMessage.narrativeMediaMessageType) {
                    NarrativeMediaMessage.NarrativeMediaMessageType.PlaySound -> {
                        if ( narrative!!.sounds.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannelEnum.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, AudioViewMessage(
                                    AudioViewMessage.AudioViewMessageType.PlaySound, narrative!!.sounds[narrativeMediaMessage.assetFilename]!!.asset)
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.PlayMusic -> {
                        if ( narrative!!.music.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannelEnum.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, AudioViewMessage(
                                    AudioViewMessage.AudioViewMessageType.PlayMusic, narrative!!.music[narrativeMediaMessage.assetFilename]!!.asset)
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.StopMusic -> {
                        if ( narrative!!.music.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannelEnum.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, AudioViewMessage(
                                    AudioViewMessage.AudioViewMessageType.StopMusic, null)
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.FadeInMusic -> {
                        if ( narrative!!.music.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannelEnum.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, AudioViewMessage(
                                    AudioViewMessage.AudioViewMessageType.FadeInMusic, narrative!!.music[narrativeMediaMessage.assetFilename]!!.asset)
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.FadeOutMusic -> {
                        if ( narrative!!.music.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannelEnum.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, AudioViewMessage(
                                    AudioViewMessage.AudioViewMessageType.FadeOutMusic, null)
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.CrossFadeMusic -> {
                        if ( narrative!!.music.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannelEnum.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, AudioViewMessage(
                                    AudioViewMessage.AudioViewMessageType.CrossFadeMusic, narrative!!.music[narrativeMediaMessage.assetFilename]!!.asset)
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.ShowImage -> {
                        if ( narrative!!.textures.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannelEnum.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                    DisplayViewTextureMessage.DisplayViewTextureMessageType.ShowImage, narrative!!.textures[narrativeMediaMessage.assetFilename]!!.asset, narrativeMediaMessage.imageLayoutPaneIdx?.toInt())
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.HideImage -> {
                        if ( narrative!!.textures.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannelEnum.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                    DisplayViewTextureMessage.DisplayViewTextureMessageType.HideImage, null, narrativeMediaMessage.imageLayoutPaneIdx?.toInt())
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.FadeInImage -> {
                        if ( narrative!!.textures.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannelEnum.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                    DisplayViewTextureMessage.DisplayViewTextureMessageType.FadeInImage, narrative!!.textures[narrativeMediaMessage.assetFilename]!!.asset, narrativeMediaMessage.imageLayoutPaneIdx?.toInt())
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.FadeOutImage -> {
                        if ( narrative!!.textures.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannelEnum.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                    DisplayViewTextureMessage.DisplayViewTextureMessageType.FadeOutImage, null, narrativeMediaMessage.imageLayoutPaneIdx?.toInt())
                            )
                        }
                    }
                    NarrativeMediaMessage.NarrativeMediaMessageType.CrossFadeImage -> {
                        if ( narrative!!.textures.keys.contains(narrativeMediaMessage.assetFilename) ) {
                            MessageChannelEnum.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
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