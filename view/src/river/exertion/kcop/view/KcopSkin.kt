package river.exertion.kcop.view

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.assets.disposeSafely
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

class KcopSkin(var skin : Skin, var fontPackage: FontPackage) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(KcopSkinBridge, this::class))
    }

    val uiSounds = mutableMapOf<UiSounds, Music>()
/*
    val layouts = mutableListOf(DVLayout.dvLayout())

    fun layoutByName(name : String) = layouts.firstOrNull { it.name == name } ?: layouts[0]

    fun nextLayout(name : String) : DVLayout {

        val currentLayoutIdx = layouts.indexOf(layoutByName(name))

        return if (currentLayoutIdx == layouts.size) layouts[0] else layouts[currentLayoutIdx + 1]
    }
*/
    fun labelStyle(fontSize : FontSize, colorPalette: ColorPalette? = ColorPalette.randomW3cBasic()) = LabelStyle (fontPackage.font(fontSize), colorPalette?.label()?.color())

/*    fun addOnEnter(actor : Actor) { actor.onEnter { MessageChannelEnum.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
            DisplayViewAudioMessage.DisplayViewAudioMessageType.PlaySound, uiSounds[UiSounds.Enter])) }}

    fun addOnClick(actor : Actor) { actor.onClick { MessageChannelEnum.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
            DisplayViewAudioMessage.DisplayViewAudioMessageType.PlaySound, uiSounds[UiSounds.Click])) }}
*/
    enum class UiSounds {
        Enter, Click, Swoosh
    }

    //experiments for border
//        stage.addActor(Image(NinePatch(assets[TextureAssets.KoboldA])).apply { this.x = 0f; this.y = 0f; this.width = 10f; this.height = orthoCamera.viewportHeight })
//        stage.addActor(Image(NinePatch(assets[TextureAssets.KoboldA])).apply { this.x = river.exertion.kcop.view.layout.ViewType.firstWidth(orthoCamera.viewportWidth) - 10; this.y = 0f; this.width = 10f; this.height = orthoCamera.viewportHeight })

    //        layout.displayViewCtrl.recreate()

    fun dispose() {
        skin.disposeSafely()
        fontPackage.dispose()
    }

    companion object {
        const val KcopSkinBridge = "KcopSkinBridge"

        val BackgroundColor = ColorPalette.of("black")
    }
}