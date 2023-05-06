package river.exertion.kcop.view

import com.badlogic.gdx.assets.AssetManager
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IPackage
import river.exertion.kcop.view.messaging.*

object ViewPackage : IPackage {

    override var id = Id.randomId()

    override var name = this::class.simpleName.toString()

    override fun loadChannels() {
        MessageChannelHandler.addChannel(MessageChannel(SDCBridge, SdcHandler::class))
        MessageChannelHandler.addChannel(MessageChannel(KcopSkinBridge, KcopSkin::class))

        MessageChannelHandler.addChannel(MessageChannel(AiHintBridge, AiHintMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(AudioViewBridge, AudioViewMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(DisplayModeBridge, DisplayModeMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(DisplayViewTextBridge, DisplayViewTextMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(DisplayViewTextureBridge, DisplayViewTextureMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(InputViewBridge, InputViewMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(KcopBridge, KcopMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(LogViewBridge, LogViewMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(MenuViewBridge, MenuViewMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(PauseViewBridge, PauseViewMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(StatusViewBridge, StatusViewMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(TextViewBridge, TextViewMessage::class))

        MessageChannelHandler.addChannel(MessageChannel(MenuNavBridge, MenuNavMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(ImmersionPauseBridge, String::class))
        MessageChannelHandler.addChannel(MessageChannel(ImmersionKeypressBridge, String::class))
    }

    override fun loadAssets(assetManager: AssetManager) { }

    const val SDCBridge = "SDCBridge"
    const val KcopSkinBridge = "KcopSkinBridge"

    const val AiHintBridge = "AiHintBridge"
    const val AudioViewBridge = "AudioViewBridge"
    const val DisplayModeBridge = "DisplayModeBridge"
    const val DisplayViewTextBridge = "DisplayViewTextBridge"
    const val DisplayViewTextureBridge = "DisplayViewTextureBridge"
    const val InputViewBridge = "InputViewBridge"
    const val KcopBridge = "KcopBridge"
    const val LogViewBridge = "LogViewBridge"
    const val MenuViewBridge = "MenuViewBridge"
    const val PauseViewBridge = "PauseViewBridge"
    const val StatusViewBridge = "StatusViewBridge"
    const val TextViewBridge = "TextViewBridge"

    const val MenuNavBridge = "MenuNavBridge"
    const val ImmersionPauseBridge = "ImmersionPauseBridge"
    const val ImmersionKeypressBridge = "ImmersionKeypressBridge"
}