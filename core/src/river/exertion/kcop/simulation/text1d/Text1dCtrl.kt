package river.exertion.kcop.simulation.text1d

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.narrative.navigation.NarrativeNavigation
import river.exertion.kcop.narrative.sequence.NarrativeSequence
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.text1d.Text1dType
import river.exertion.kcop.system.view.ViewType
import kotlin.reflect.jvm.javaMethod

class Text1dCtrl(var text1dType: Text1dType, var screenWidth: Float = 50f, var screenHeight: Float = 50f) : Table() {

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
        this.clearListeners()

        if (sdc != null) { sdc!!.dispose(); sdc = null }

        this.setSize(tableWidth(), heightOverride)
        this.setPosition(tablePosX(), posYOverride)

        this.debug()
    }

    fun create() {
        clearTable()

        this.top()

        if (textColor != null) {
            if (bitmapFont == null) throw Exception("${::create.javaMethod?.name}: bitmapFont needs to be set")
            if (batch == null) throw Exception("${::create.javaMethod?.name}: batch needs to be set")
            if (sdc == null) sdc = ShapeDrawerConfig(batch!!, textColor!!.comp().color())

            if (this.text1dType == Text1dType.SEQUENCE) {
                val viewLabel = Label(text1dSequence!!.currentText(), Label.LabelStyle(bitmapFont, textColor!!.label().color()))
                this.add(viewLabel).growX().left().padLeft(ViewType.padWidth(width)).padRight(ViewType.padWidth(width)).padTop(ViewType.padHeight(height)).padBottom(ViewType.padHeight(height))
            } else {
                val viewLabel = Label(text1dNavigation!!.currentText(), Label.LabelStyle(bitmapFont, textColor!!.label().color()))
                this.add(viewLabel).growX().left().padLeft(ViewType.padWidth(width)).padRight(ViewType.padWidth(width)).padTop(ViewType.padHeight(height)).padBottom(ViewType.padHeight(height))

                val promptsMaxIdx = text1dNavigation!!.currentPrompts().size - 1

                text1dNavigation!!.currentPrompts().forEachIndexed { idx, prompt ->
                    this.row()
                    val promptLabel = Label(prompt, Label.LabelStyle(bitmapFont, textColor!!.label().color()))

                    if (idx < promptsMaxIdx)
                        this.add(promptLabel).growX().left().padLeft(ViewType.padWidth(width) * 2).padRight(ViewType.padWidth(width))
                    else
                        this.add(promptLabel).growX().left().padLeft(ViewType.padWidth(width) * 2).padRight(ViewType.padWidth(width)).padBottom(ViewType.padHeight(height))
                }
            }



        }
    }
}