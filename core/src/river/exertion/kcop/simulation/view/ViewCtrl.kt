package river.exertion.kcop.simulation.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import kotlin.reflect.jvm.javaMethod

open class ViewCtrl(val viewType : ViewType, var screenWidth: Float = 50f, var screenHeight: Float = 50f) : Table() {

    var sdc : ShapeDrawerConfig? = null
    var bitmapFont : BitmapFont? = null
    var batch : Batch? = null

    var isInitialized = false

    fun viewRect() = viewType.viewRect(screenWidth, screenHeight)

    fun tableWidth() = viewRect().width
    fun tableHeight() = viewRect().height
    fun tablePosX() = viewRect().x
    fun tablePosY() = viewRect().y

    var backgroundColor : ColorPalette = viewType.defaultColor()
    fun backgroundColorTexture(batch : Batch) : TextureRegion {
        if (this.sdc == null) this.sdc = ShapeDrawerConfig(batch, backgroundColor.color())

        return sdc!!.textureRegion.apply {this.setRegion(tablePosX().toInt(), tablePosY().toInt(), tableWidth().toInt(), tableHeight().toInt()) }
    }

    fun backgroundColorImg(batch : Batch) : Image = Image(backgroundColorTexture(batch))

    fun clearTable() {
        this.clearChildren()
        this.clearListeners()

        this.setSize(tableWidth(), tableHeight())
        this.setPosition(tablePosX(), tablePosY())

        this.debug()
    }

    fun recreate() {
        if (!isInitialized) throw Exception("${::recreate.javaMethod?.name}: view needs to be initialized with " + ::initCreate.javaMethod?.name)

        clearTable()

        build(this.bitmapFont!!, this.batch!!)
    }

    fun initCreate(bitmapFont: BitmapFont, batch: Batch) {
        if (this.batch == null) this.batch = batch
        if (this.bitmapFont == null) this.bitmapFont = bitmapFont

        isInitialized = true

        clearTable()

        build(bitmapFont, batch)
    }

    open fun build(bitmapFont: BitmapFont, batch: Batch) {

        val stack = Stack()

        val viewLabel = Label(viewType.name, Label.LabelStyle(bitmapFont, backgroundColor.label().color()))
        viewLabel.setAlignment(Align.center)

        stack.onClick {
            println("layout View:${viewType.name}")
            println("x:${Gdx.input.getX()}, y:${Gdx.input.getY()}")
        }

        stack.add(backgroundColorImg(batch))
        stack.add(viewLabel)

        this.add(stack)
    }

    open fun dispose() {
        if (sdc != null) { sdc!!.dispose(); sdc = null }
    }
}