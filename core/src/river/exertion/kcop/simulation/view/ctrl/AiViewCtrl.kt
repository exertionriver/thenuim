package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.actors.onClick
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.view.LogViewMessage
import river.exertion.kcop.system.view.LogViewMessageType
import river.exertion.kcop.system.view.ViewMessage

class AiViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : ViewCtrl(ViewType.AI, screenWidth, screenHeight) {

    var aiUpImage : Texture? = null
    var aiDownImage : Texture? = null
    var aiCheckedImage : Texture? = null

    var isChecked = false

    fun clickButton() : Button {

        var buttonStyle = ButtonStyle()

        if (aiUpImage != null && aiDownImage != null && aiCheckedImage != null) {
            buttonStyle = ButtonStyle(
                TextureRegionDrawable(aiUpImage!!)
                , TextureRegionDrawable(aiDownImage!!)
                , TextureRegionDrawable(aiCheckedImage!!))
        }

        val innerButton = Button(buttonStyle)

        //override from ctrl
        innerButton.isChecked = this@AiViewCtrl.isChecked

        innerButton.onClick {
            this@AiViewCtrl.isChecked = !this@AiViewCtrl.isChecked
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "AI set to: ${if (this.isChecked) "On" else "Off"}" ))
        }

        return innerButton
    }

    override fun build(bitmapFont: BitmapFont, batch: Batch) {
        this.add(clickButton()).width(this.tableWidth()).height(this.tableHeight())
        this.clip()
    }
}