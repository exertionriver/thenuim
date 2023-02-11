package river.exertion.kcop.simulation.text1d

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.text1d.Text1dType
import kotlin.reflect.jvm.javaMethod
import river.exertion.kcop.system.text1d.Text1dMessage


class Text1dLayout(var width : Float, var height : Float) : Telegraph {

    var bitmapFont : BitmapFont? = null
    var batch : Batch? = null

    val firstDefaultColor = ColorPalette.Color402
    var textColor = firstDefaultColor.comp().label()

    var text1dNarrativeIdx = 0
    var text1dNarratives = mutableListOf<Narrative>()

    var textBlockCtrl = Text1dCtrl(width, height)

    init {
        MessageChannel.TEXT1D_BRIDGE.enableReceive(this)
    }

    private fun createTextCtrl(textCtrl : Text1dCtrl) : Table {
        if (bitmapFont == null) throw Exception("${::createTextCtrl.javaMethod?.name}: bitmapFont needs to be set")
        if (batch == null) throw Exception("${::createTextCtrl.javaMethod?.name}: batch needs to be set")

        if (textCtrl.bitmapFont == null) textCtrl.bitmapFont = bitmapFont
        if (textCtrl.batch == null) textCtrl.batch = batch

        textCtrl.textColor = textColor
        textCtrl.text1dNarrative = text1dNarratives[text1dNarrativeIdx]

        textCtrl.create()
        return textCtrl
    }

    fun createTextBlockCtrl() = createTextCtrl(textBlockCtrl)

    fun prevNarrativeIdx() {
        text1dNarrativeIdx = (text1dNarrativeIdx - 1).coerceAtLeast(0)
        createTextBlockCtrl()
    }

    fun nextNarrativeIdx() {
        text1dNarrativeIdx = (text1dNarrativeIdx + 1).coerceAtMost(text1dNarratives.size - 1)
        createTextBlockCtrl()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if ( (msg != null) && (MessageChannel.TEXT1D_BRIDGE.isType(msg.message) ) ) {
            val text1dMessage : Text1dMessage = MessageChannel.TEXT1D_BRIDGE.receiveMessage(msg.extraInfo)

            text1dNarratives[text1dNarrativeIdx].next(text1dMessage.key)

            createTextBlockCtrl()
        }
        return true
    }
}