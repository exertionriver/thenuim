package river.exertion.kcop.simulation.view

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.view.ViewType

class ViewLayout(var width : Float, var height : Float) : Telegraph {

    var bitmapFont : BitmapFont? = null
    var batch : Batch? = null

    var displayViewCtrl = ViewCtrl(ViewType.DISPLAY, width, height)
    var textViewCtrl = ViewCtrl(ViewType.TEXT, width, height)
    var logViewCtrl = ViewCtrl(ViewType.LOG, width, height)
    var menuViewCtrl = ViewCtrl(ViewType.MENU, width, height)
    var promptsViewCtrl = ViewCtrl(ViewType.PROMPTS, width, height)
    var inputsViewCtrl = ViewCtrl(ViewType.INPUTS, width, height)
    var aiViewCtrl = ViewCtrl(ViewType.AI, width, height)
    var pauseViewCtrl = ViewCtrl(ViewType.PAUSE, width, height)

    init {
        MessageChannel.LAYOUT_BRIDGE.enableReceive(this)
    }

    private fun createViewCtrl(layoutViewCtrl : ViewCtrl) : Table {
        if (layoutViewCtrl.bitmapFont == null) layoutViewCtrl.bitmapFont = bitmapFont
        if (layoutViewCtrl.batch == null) layoutViewCtrl.batch = batch

        layoutViewCtrl.create()

        return layoutViewCtrl
    }

    fun createDisplayViewCtrl() = createViewCtrl(displayViewCtrl)
    fun createTextViewCtrl() = createViewCtrl(textViewCtrl)
    fun createLogViewCtrl() = createViewCtrl(logViewCtrl)
    fun createMenuViewCtrl() = createViewCtrl(menuViewCtrl)
    fun createPromptsViewCtrl() = createViewCtrl(promptsViewCtrl)
    fun createInputsViewCtrl() = createViewCtrl(inputsViewCtrl)
    fun createAiViewCtrl() = createViewCtrl(aiViewCtrl)
    fun createPauseViewCtrl() = createViewCtrl(pauseViewCtrl)

    override fun handleMessage(msg: Telegram?): Boolean {
        return true
    }

}