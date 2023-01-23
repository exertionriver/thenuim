package river.exertion.kcop.simulation.text1d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.narrative.navigation.NarrativeNavigation
import river.exertion.kcop.narrative.sequence.NarrativeSequence
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.text1d.Text1dType
import river.exertion.kcop.system.view.ViewType
import kotlin.reflect.jvm.javaMethod

class Text1dCtrl(val text1dType: Text1dType, var screenWidth: Float = 50f, var screenHeight: Float = 50f) : Table() {

    var sdc : ShapeDrawerConfig? = null
    var bitmapFont : BitmapFont? = null
    var batch : Batch? = null

    var text1dSequence : NarrativeSequence? = null
    var text1dNavigation : NarrativeNavigation? = null

    var textColor : ColorPalette? = null

    fun viewRect() = ViewType.TEXT.viewRect(screenWidth, screenHeight)

    fun tableWidth() = viewRect().width
    fun tableHeight() = viewRect().height
    fun tablePosX() = viewRect().x
    fun tablePosY() = viewRect().y

    fun clearTable(heightOverride : Float = tableHeight(), posYOverride : Float = tablePosY()) {
        this.clearChildren()

        if (sdc != null) { sdc!!.dispose(); sdc = null }

        this.setSize(tableWidth(), heightOverride)
        this.setPosition(tablePosX(), posYOverride)

        this.debug()
    }

    fun create() {
        clearTable()

        if (textColor != null) {
            if (bitmapFont == null) throw Exception("${::create.javaMethod?.name}: bitmapFont needs to be set")
            if (batch == null) throw Exception("${::create.javaMethod?.name}: batch needs to be set")
            if (sdc == null) sdc = ShapeDrawerConfig(batch!!, textColor!!.color())

//            val stack = Stack()

//            val backgroundImg = Image(sdc!!.textureRegion.apply {this.setRegion(tablePosX().toInt(), tablePosY().toInt(), tableWidth().toInt(), tableHeight().toInt()) })

            val viewLabel = Label(text1dSequence!!.currentText(), Label.LabelStyle(bitmapFont, textColor!!.label().color()))
            viewLabel.setAlignment(Align.left)

//            stack.add(viewLabel)

            this.add(viewLabel)
        }
    }
}