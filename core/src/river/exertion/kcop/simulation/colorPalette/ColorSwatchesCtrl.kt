package river.exertion.kcop.simulation.colorPalette

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
import kotlin.reflect.jvm.javaMethod

class ColorSwatchesCtrl(var topX: Float = 0f, var topY: Float = 0f, var swatchWidth: Float = 50f, var swatchHeight: Float = 50f) : Table() {

    var swatchEntries: Map<String, ColorPalette> = mapOf()
    val sdcList = mutableListOf<ShapeDrawerConfig>()

    var bitmapFont : BitmapFont? = null
    var batch : Batch? = null

    fun tableWidth() = swatchWidth
    fun tableHeight() = swatchHeight * swatchEntries.size
    fun tablePosX() = topX
    fun tablePosY() = topY - swatchHeight * (swatchEntries.size - 1)

    fun clearTable(heightOverride : Float = tableHeight(), posYOverride : Float = tablePosY()) {
        this.clearChildren()

        sdcList.forEach { it.dispose() }
        sdcList.clear()

        this.setSize(tableWidth(), heightOverride)
        this.setPosition(tablePosX(), posYOverride)

//        this.debug()
    }

    fun create() {
        clearTable()

        swatchEntries.entries.forEachIndexed { idx, colorPaletteEntry ->
            addSwatch(idx, colorPaletteEntry)
        }
    }

    fun createWithHeader(headerColorName : String, headerColor : ColorPalette) {
        clearTable(tableHeight() + 2 * swatchHeight, tablePosY() - 2 * swatchHeight)

        addSwatch(0, mapOf(headerColorName to headerColor).entries.first() )
        addSwatch(1, mapOf(null to ColorPalette.Color000).entries.first() )

        swatchEntries.entries.forEachIndexed { idx, colorPaletteEntry ->
            addSwatch(idx + 2, colorPaletteEntry)
        }
    }

    fun addSwatch(idx : Int, colorPaletteEntry : Map.Entry<String?, ColorPalette>) {
        if (bitmapFont == null) throw Exception("${::addSwatch.javaMethod?.name}: bitmapFont needs to be set")
        if (batch == null) throw Exception("${::addSwatch.javaMethod?.name}: batch needs to be set")

        val sdc = ShapeDrawerConfig(batch!!, colorPaletteEntry.value.color())
        sdcList.add(sdc)

        val stack = Stack()

        val swatchImg = Image(sdc.textureRegion.apply {this.setRegion(topX.toInt(), (topY - swatchHeight * idx).toInt(), swatchWidth.toInt(), swatchHeight.toInt()) })

        if (colorPaletteEntry.key != null) {
            stack.onClick {
                MessageChannel.COLOR_PALETTE_BRIDGE.send(null, ColorPaletteMessage(colorPaletteEntry.key!!, colorPaletteEntry.value))
            }
        }

        val swatchLabel = Label(colorPaletteEntry.key, Label.LabelStyle(bitmapFont, colorPaletteEntry.value.label().color()))
        swatchLabel.setAlignment(Align.center)

        stack.add(swatchImg)
        stack.add(swatchLabel)
        this.add(stack)
        this.row()
    }
}