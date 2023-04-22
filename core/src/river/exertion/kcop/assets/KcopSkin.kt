package river.exertion.kcop.assets

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.actors.onClick
import ktx.actors.onEnter
import ktx.assets.disposeSafely
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.DisplayViewAudioMessage

class KcopSkin(var skin : Skin, var fontPackage: FontPackage) {

    val uiSounds = mutableMapOf<UiSounds, Music>()

    fun labelStyle(fontSize : FontSize, color : Color) = LabelStyle (fontPackage.font(fontSize), color)

    fun addOnEnter(actor : Actor) { actor.onEnter { MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
            DisplayViewAudioMessage.DisplayViewAudioMessageType.PlaySound, uiSounds[UiSounds.Enter])) }}

    fun addOnClick(actor : Actor) { actor.onClick { MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
            DisplayViewAudioMessage.DisplayViewAudioMessageType.PlaySound, uiSounds[UiSounds.Click])) }}

    enum class UiSounds {
        Enter, Click, Swoosh
    }

    //experiments for border
//        stage.addActor(Image(NinePatch(assets[TextureAssets.KoboldA])).apply { this.x = 0f; this.y = 0f; this.width = 10f; this.height = orthoCamera.viewportHeight })
//        stage.addActor(Image(NinePatch(assets[TextureAssets.KoboldA])).apply { this.x = ViewType.firstWidth(orthoCamera.viewportWidth) - 10; this.y = 0f; this.width = 10f; this.height = orthoCamera.viewportHeight })

    //        layout.displayViewCtrl.recreate()
    companion object {
        val BackgroundColor = ColorPalette.of("black")
    }

    fun dispose() {
        skin.disposeSafely()
        fontPackage.dispose()
    }

}