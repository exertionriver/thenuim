package river.exertion.kcop.view

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.actors.onClick
import ktx.actors.onEnter
import ktx.assets.disposeSafely
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.ViewPackage.AudioViewBridge
import river.exertion.kcop.view.messaging.AudioViewMessage

object KcopSkin {

    val uiSounds = mutableMapOf<UiSounds, Music>()

    var screenWidth = 50f
    var screenHeight = 50f

    lateinit var skin : Skin
    lateinit var fontPackage: FontPackage

    var displayMode = false
    /*
    val layouts = mutableListOf(DVLayout.dvLayout())

    fun layoutByName(name : String) = layouts.firstOrNull { it.name == name } ?: layouts[0]

    fun nextLayout(name : String) : DVLayout {

        val currentLayoutIdx = layouts.indexOf(layoutByName(name))

        return if (currentLayoutIdx == layouts.size) layouts[0] else layouts[currentLayoutIdx + 1]
    }
*/
    fun labelStyle(fontSize : FontSize, colorPalette: ColorPalette? = ColorPalette.randomW3cBasic()) = LabelStyle (fontPackage.font(fontSize), colorPalette?.color())

    fun addOnEnter(actor : Actor) { actor.onEnter { MessageChannelHandler.send(AudioViewBridge, AudioViewMessage(
        AudioViewMessage.AudioViewMessageType.PlaySound, uiSounds[UiSounds.Enter])) }}

    fun addOnClick(actor : Actor) { actor.onClick { MessageChannelHandler.send(AudioViewBridge, AudioViewMessage(
        AudioViewMessage.AudioViewMessageType.PlaySound, uiSounds[UiSounds.Click])) }}

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

    val BackgroundColor = ColorPalette.of("black")
}