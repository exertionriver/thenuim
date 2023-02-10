package river.exertion.kcop.simulation.view

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.actors.onClick
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.view.ViewMessage
import river.exertion.kcop.system.view.ViewType

class PauseViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : ViewCtrl(ViewType.PAUSE, screenWidth, screenHeight) {

    var pauseUpImage : Texture? = null
    var pauseDownImage : Texture? = null
    var pauseCheckedImage : Texture? = null

    fun clickButton() : Button {

        var buttonStyle = ButtonStyle()

        if (pauseUpImage != null && pauseDownImage != null && pauseCheckedImage != null) {
            buttonStyle = ButtonStyle(
                TextureRegionDrawable(pauseUpImage!!)
                , TextureRegionDrawable(pauseDownImage!!)
                , TextureRegionDrawable(pauseCheckedImage!!))
        }

        val innerButton = Button(buttonStyle)

        innerButton.onClick {
            MessageChannel.LAYOUT_BRIDGE.send(null, ViewMessage(null, ViewMessage.TogglePause))
        }

        return innerButton
    }

    override fun build(bitmapFont: BitmapFont, batch: Batch) {
        this.add(clickButton()).width(this.tableWidth()).height(this.tableHeight())
    }
}