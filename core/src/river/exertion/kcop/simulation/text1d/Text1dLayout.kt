package river.exertion.kcop.simulation.text1d

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.narrative.navigation.NarrativeNavigation
import river.exertion.kcop.narrative.sequence.NarrativeSequence
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

    var text1dType = Text1dType.SEQUENCE

    var text1dSequence : NarrativeSequence? = null
    var text1dNavigation : NarrativeNavigation? = null

    var textBlockCtrl = Text1dCtrl(text1dType, width, height)

    init {
        MessageChannel.TEXT1D_BRIDGE.enableReceive(this)
    }

    private fun createTextCtrl(textCtrl : Text1dCtrl) : Table {
        if (bitmapFont == null) throw Exception("${::createTextCtrl.javaMethod?.name}: bitmapFont needs to be set")
        if (batch == null) throw Exception("${::createTextCtrl.javaMethod?.name}: batch needs to be set")

        if (textCtrl.bitmapFont == null) textCtrl.bitmapFont = bitmapFont
        if (textCtrl.batch == null) textCtrl.batch = batch

        textCtrl.textColor = textColor
        textCtrl.text1dType = text1dType
        textCtrl.text1dSequence = text1dSequence
        textCtrl.text1dNavigation = text1dNavigation

        textCtrl.create()
        return textCtrl
    }

    fun createTextBlockCtrl() = createTextCtrl(textBlockCtrl)

    fun nextType() { text1dType = text1dType.next(); createTextBlockCtrl() }
    fun prevType() { text1dType = text1dType.prev(); createTextBlockCtrl() }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            val text1dMessage : Text1dMessage = MessageChannel.TEXT1D_BRIDGE.receiveMessage(msg.extraInfo)

            if (text1dType == Text1dType.SEQUENCE)
                text1dSequence!!.next(text1dMessage.key)
            else
                text1dNavigation!!.next(text1dMessage.key)

            createTextBlockCtrl()
        }
        return true
    }
}