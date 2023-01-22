package river.exertion.kcop.simulation.layout

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.layout.LayoutView

class Layout(var width : Float, var height : Float) : Telegraph {

    var bitmapFont : BitmapFont? = null
    var batch : Batch? = null

    val firstDefaultColor = ColorPalette.Color402
    val secondDefaultColor = ColorPalette.Color635

    var displayViewCtrl = LayoutViewCtrl(LayoutView.DISPLAY, width, height)
    var textViewCtrl = LayoutViewCtrl(LayoutView.TEXT, width, height)
    var logViewCtrl = LayoutViewCtrl(LayoutView.LOG, width, height)
    var menuViewCtrl = LayoutViewCtrl(LayoutView.MENU, width, height)
    var promptsViewCtrl = LayoutViewCtrl(LayoutView.PROMPTS, width, height)
    var inputsViewCtrl = LayoutViewCtrl(LayoutView.INPUTS, width, height)
    var aiViewCtrl = LayoutViewCtrl(LayoutView.AI, width, height)
    var pauseViewCtrl = LayoutViewCtrl(LayoutView.PAUSE, width, height)

    init {
        MessageChannel.LAYOUT_BRIDGE.enableReceive(this)
    }

    fun createView(layoutViewCtrl : LayoutViewCtrl, backgroundColor: ColorPalette) {
        if (layoutViewCtrl.bitmapFont == null) layoutViewCtrl.bitmapFont = bitmapFont
        if (layoutViewCtrl.batch == null) layoutViewCtrl.batch = batch

        layoutViewCtrl.backgroundColor = backgroundColor
        layoutViewCtrl.create()
    }

    fun createDisplayViewCtrl() : Actor {
        createView(displayViewCtrl, firstDefaultColor)
        return displayViewCtrl
    }

    fun createTextViewCtrl() : Actor {
        createView(textViewCtrl, firstDefaultColor.comp())
        return textViewCtrl
    }

    fun createLogViewCtrl() : Actor {
        createView(logViewCtrl, firstDefaultColor.triad().first)
        return logViewCtrl
    }

    fun createMenuViewCtrl() : Actor {
        createView(menuViewCtrl, firstDefaultColor.triad().second)
        return menuViewCtrl
    }

    fun createPromptsViewCtrl() : Actor {
        createView(promptsViewCtrl, secondDefaultColor)
        return promptsViewCtrl
    }

    fun createInputsViewCtrl() : Actor {
        createView(inputsViewCtrl, secondDefaultColor.comp())
        return inputsViewCtrl
    }

    fun createAiViewCtrl() : Actor {
        createView(aiViewCtrl, secondDefaultColor.triad().first)
        return aiViewCtrl
    }

    fun createPauseViewCtrl() : Actor {
        createView(pauseViewCtrl, secondDefaultColor.triad().second)
        return pauseViewCtrl
    }

/*
    fun textViewRowHeight() = LayoutView.seventhHeight(height) / 2
    fun textViewFirstCol() = LayoutView.firstWidth(width) + textViewRowHeight()
    fun textViewFirstRow() = LayoutView.firstHeight(height)
    fun textViewLastRow() = LayoutView.thirdHeight(height) + 3 * textViewRowHeight()

    fun logViewRowHeight() = textViewRowHeight()
    fun logViewFirstCol() = LayoutView.firstWidth(width) + LayoutView.fourthWidth(width) + textViewRowHeight()
    fun logViewFirstRow() = LayoutView.thirdHeight(height)
    fun logViewLastRow() = 3 * logViewRowHeight()
*/
    override fun handleMessage(msg: Telegram?): Boolean {
        return true
    }

}