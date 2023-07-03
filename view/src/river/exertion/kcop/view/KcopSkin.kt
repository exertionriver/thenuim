package river.exertion.kcop.view

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.actors.onClick
import ktx.actors.onEnter
import ktx.assets.disposeSafely
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.asset.FontSize
import river.exertion.kcop.view.layout.AudioView

object KcopSkin {

    val uiSounds = mutableMapOf<UiSounds, Music>()

    var screenWidth = 50f
    var screenHeight = 50f

    lateinit var skin : Skin

    var displayMode = false

    fun labelStyle(fontSize : FontSize, colorPalette: ColorPalette? = ColorPalette.randomW3cBasic()) = LabelStyle (KcopFont.font(fontSize), colorPalette?.color())

    enum class UiSounds {
        Enter, Click, Swoosh
    }

    fun addOnEnter(actor : Actor) { actor.onEnter { AudioView.playSound(uiSounds[UiSounds.Enter]) }}

    fun addOnClick(actor : Actor) { actor.onClick { AudioView.playSound(uiSounds[UiSounds.Click]) }}

    //experiments for border
//        stage.addActor(Image(NinePatch(assets[TextureAssets.KoboldA])).apply { this.x = 0f; this.y = 0f; this.width = 10f; this.height = orthoCamera.viewportHeight })
//        stage.addActor(Image(NinePatch(assets[TextureAssets.KoboldA])).apply { this.x = river.exertion.kcop.view.layout.ViewType.firstWidth(orthoCamera.viewportWidth) - 10; this.y = 0f; this.width = 10f; this.height = orthoCamera.viewportHeight })

    //        layout.displayViewCtrl.recreate()

    fun dispose() {
        skin.disposeSafely()
    }

    val BackgroundColor = ColorPalette.BackgroundColor
}