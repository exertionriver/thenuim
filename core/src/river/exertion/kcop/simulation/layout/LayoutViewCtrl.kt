package river.exertion.kcop.simulation.layout

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.colorPalette.ColorPaletteMessage
import river.exertion.kcop.system.layout.LayoutView
import kotlin.reflect.jvm.javaMethod

class LayoutViewCtrl(val layoutView : LayoutView, var screenWidth: Float = 50f, var screenHeight: Float = 50f) : Table() {

    var sdc : ShapeDrawerConfig? = null
    var bitmapFont : BitmapFont? = null
    var batch : Batch? = null

    var backgroundColor : ColorPalette? = null

    fun viewRect() = layoutView.viewRect(screenWidth, screenHeight)

    fun tableWidth() = viewRect().width
    fun tableHeight() = viewRect().height
    fun tablePosX() = viewRect().x
    fun tablePosY() = viewRect().y

    fun clearTable() {
        this.clearChildren()

        this.setSize(tableWidth(), tableHeight())
        this.setPosition(tablePosX(), tablePosY())
    }

    fun create() {
        clearTable()

        if (backgroundColor != null) {
            if (bitmapFont == null) throw Exception("${::create.javaMethod?.name}: bitmapFont needs to be set")
            if (batch == null) throw Exception("${::create.javaMethod?.name}: batch needs to be set")
            if (sdc == null) sdc = ShapeDrawerConfig(batch!!, backgroundColor!!.color())

            val stack = Stack()

            val backgroundImg = Image(sdc!!.textureRegion.apply {this.setRegion(tablePosX().toInt(), tablePosY().toInt(), tableWidth().toInt(), tableHeight().toInt()) })

            val viewLabel = Label(layoutView.name, Label.LabelStyle(bitmapFont, backgroundColor!!.label().color()))
            viewLabel.setAlignment(Align.center)

            stack.onClick {
                println("layout View:${layoutView.name}")
                println("x:${Gdx.input.getX()}, y:${Gdx.input.getY()}")
            }

            stack.add(backgroundImg)
            stack.add(viewLabel)

            this.add(stack)
        }

    }
}