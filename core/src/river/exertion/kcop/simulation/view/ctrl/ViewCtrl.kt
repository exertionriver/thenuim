package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.view.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette

open class ViewCtrl(val viewType : ViewType, var screenWidth: Float = 50f, var screenHeight: Float = 50f) : Table() {

    var sdc : ShapeDrawerConfig? = null
    lateinit var fontPackage : FontPackage
    lateinit var batch : Batch

    fun viewRect() = viewType.viewRect(screenWidth, screenHeight)

    fun tableWidth() = viewRect().width
    fun tableHeight() = viewRect().height
    fun tablePosX() = viewRect().x
    fun tablePosY() = viewRect().y

    var backgroundColor : ColorPalette = viewType.defaultColor()
    fun backgroundColorTexture() : TextureRegion {
        if (this.sdc == null) this.sdc = ShapeDrawerConfig(batch, backgroundColor.color())

        return sdc!!.textureRegion.apply {this.setRegion(0, 0, tableWidth().toInt() - 1, tableHeight().toInt() - 1) }
    }

    fun backgroundColorImg() : Image = Image(backgroundColorTexture())

    fun clearTable() {
        this.clearChildren()
        this.clearListeners()

        this.setSize(tableWidth(), tableHeight())
        this.setPosition(tablePosX(), tablePosY())
    }

    fun build() {
        clearTable()
        buildCtrl()
    }

    open fun buildCtrl() {
        val stack = Stack()

        val viewLabel = Label(viewType.name, Label.LabelStyle(fontPackage.font(FontSize.TEXT), backgroundColor.label().color()))
        viewLabel.setAlignment(Align.center)

        stack.onClick {
            println("layout View:${viewType.name}")
            println("x:${Gdx.input.getX()}, y:${Gdx.input.getY()}")
        }

        stack.add(backgroundColorImg())
        stack.add(viewLabel)

        this.add(stack).size(this.tableWidth(), this.tableHeight())
        this.clip()
    }

    open fun dispose() {
        if (sdc != null) { sdc!!.dispose(); sdc = null }
    }
}