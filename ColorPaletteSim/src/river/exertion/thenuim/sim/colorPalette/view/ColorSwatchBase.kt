package river.exertion.thenuim.sim.colorPalette.view

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.thenuim.asset.view.ColorPalette
import river.exertion.thenuim.view.TnmSkin
import river.exertion.thenuim.view.SdcHandler
import river.exertion.thenuim.view.TnmFont

class ColorSwatchBase(var topX: Float = 0f, var topY: Float = 0f, var swatchWidth: Float = 50f, var swatchHeight: Float = 50f) : Table() {

    var swatchEntries: Map<String, ColorPalette> = mapOf()

    fun tableWidth() = swatchWidth
    fun tableHeight() = swatchHeight * swatchEntries.size
    fun tablePosX() = topX
    fun tablePosY() = topY - swatchHeight * (swatchEntries.size - 1)

    fun clearTable(heightOverride : Float = tableHeight(), posYOverride : Float = tablePosY()) {
        this.clearChildren()

        this.setSize(tableWidth(), heightOverride)
        this.setPosition(tablePosX(), posYOverride)
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

        val swatchTexture = SdcHandler.updorad("cpSwatch_${colorPaletteEntry.key}", colorPaletteEntry.value).textureRegion().apply {
            this.setRegion(topX.toInt(), (topY - swatchHeight * idx).toInt(), swatchWidth.toInt(), swatchHeight.toInt())
        }

        val swatchImg = Image(swatchTexture)

        val stack = Stack()

        if (colorPaletteEntry.key != null) {
            stack.onClick {
                ColorPaletteLayoutHandler.setBaseColor(colorPaletteEntry.key!!, colorPaletteEntry.value)
            }
        }

        val swatchLabel = Label(colorPaletteEntry.key, TnmSkin.labelStyle(TnmFont.TEXT, colorPaletteEntry.value.label()))
        swatchLabel.setAlignment(Align.center)

        stack.add(swatchImg)
        stack.add(swatchLabel)
        this.add(stack)
        this.row()
    }
}