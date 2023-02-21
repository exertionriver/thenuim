package river.exertion.kcop.simulation.view

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.view.DisplayViewPane
import river.exertion.kcop.system.view.ViewType
import java.lang.Math.round
import kotlin.math.roundToInt

class DisplayViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : ViewCtrl(ViewType.DISPLAY, screenWidth, screenHeight) {

    val tables : MutableList<Table> = mutableListOf()

    fun paneColorTexture(batch : Batch, pane : DisplayViewPane) : TextureRegion {
        this.sdc = ShapeDrawerConfig(batch, pane.defaultColor().color())


        return if ( pane.ordinal in listOf(33, 31, 28) )
            //for some reason, top border misses a pixel for small blocks stacked on each other
            sdc!!.textureRegion.apply {this.setRegion(0, 0, pane.width(screenWidth).roundToInt(), pane.height(screenHeight).roundToInt() + 1) }
        else
            sdc!!.textureRegion.apply {this.setRegion(0, 0, pane.width(screenWidth).roundToInt(), pane.height(screenHeight).roundToInt()) }
    }

    fun buildTables(batch: Batch) : Table {

        val innerTable = Table()

        DisplayViewPane.values().forEach { displayViewPane ->
            tables.add(Table().apply { this.background = TextureRegionDrawable(paneColorTexture(batch, displayViewPane)) })
        }

        val tlSpiral0 = Table()
        tlSpiral0.add(tables[30]).fill()
        tlSpiral0.add(tables[24]).fill()

        val tlSpiral1 = Table()
        tlSpiral1.add(tlSpiral0)
        tlSpiral1.row()
        tlSpiral1.add(tables[18]).fill()

        val tlSpiral2 = Table()
        tlSpiral2.add(tlSpiral1)
        tlSpiral2.add(tables[11]).fill()

        val tlSpiral3 = Table()
        tlSpiral3.add(tables[7]).fill()
        tlSpiral3.row()
        tlSpiral3.add(tlSpiral2)

        val tlSpiral4 = Table()
        tlSpiral4.add(tables[2]).fill()
        tlSpiral4.add(tlSpiral3)

        innerTable.add(tlSpiral4).colspan(13)

        val trSpiral0a = Table()
        trSpiral0a.add(tables[33]).fill()
        trSpiral0a.row()
        trSpiral0a.add(tables[27]).fill()

        val trSpiral0b = Table()
        trSpiral0b.add(tables[26]).fill()
        trSpiral0b.add(tables[32]).fill()

        val trSpiral1a = Table()
        trSpiral1a.add(tables[21]).fill()
        trSpiral1a.add(trSpiral0a)

        val trSpiral1b = Table()
        trSpiral1b.add(trSpiral0b)
        trSpiral1b.row()
        trSpiral1b.add(tables[20]).fill()

        val trSpiral2a = Table()
        trSpiral2a.add(tables[14]).fill()
        trSpiral2a.row()
        trSpiral2a.add(trSpiral1a)
        trSpiral2a.row()
        trSpiral2a.add(tables[15]).fill()

        val trSpiral2b = Table()
        trSpiral2b.add(trSpiral1b)
        trSpiral2b.add(tables[13]).fill()

        val trSpiral3b = Table()
        trSpiral3b.add(tables[4]).fill()
        trSpiral3b.row()
        trSpiral3b.add(trSpiral2b)

        val trSpiral4 = Table()
        trSpiral4.add(trSpiral2a)
        trSpiral4.add(trSpiral3b)

        innerTable.add(trSpiral4).colspan(8)
        innerTable.row()

        val lSpiral0 = Table()
        lSpiral0.add(tables[28]).fill()
        lSpiral0.row()
        lSpiral0.add(tables[22]).fill()

        val lSpiral1 = Table()
        lSpiral1.add(lSpiral0)
        lSpiral1.add(tables[16]).fill()

        val lSpiral2 = Table()
        lSpiral2.add(tables[9]).fill()
        lSpiral2.row()
        lSpiral2.add(lSpiral1)

        val lSpiral3 = Table()
        lSpiral3.add(tables[5]).fill()
        lSpiral3.add(lSpiral2)

        val rSpiral0 = Table()
        rSpiral0.add(tables[31]).fill()
        rSpiral0.row()
        rSpiral0.add(tables[25]).fill()

        val rSpiral1 = Table()
        rSpiral1.add(tables[19]).fill()
        rSpiral1.add(rSpiral0)

        val rSpiral2 = Table()
        rSpiral2.add(rSpiral1)
        rSpiral2.row()
        rSpiral2.add(tables[12]).fill()

        val rSpiral3 = Table()
        rSpiral3.add(rSpiral2)
        rSpiral3.add(tables[8]).fill()

        innerTable.add(lSpiral3).colspan(8)
        innerTable.add(tables[0]).fill().colspan(5)
        innerTable.add(rSpiral3).colspan(8)

        innerTable.row()

        val brSpiral0 = Table()
        brSpiral0.add(tables[23]).fill()
        brSpiral0.add(tables[29]).fill()

        val brSpiral1 = Table()
        brSpiral1.add(brSpiral0)
        brSpiral1.row()
        brSpiral1.add(tables[17]).fill()

        val brSpiral2 = Table()
        brSpiral2.add(tables[10]).fill()
        brSpiral2.add(brSpiral1)

        val brSpiral3 = Table()
        brSpiral3.add(brSpiral2)
        brSpiral3.row()
        brSpiral3.add(tables[6]).fill()

        innerTable.add(tables[1]).fill().colspan(8)
        innerTable.add(brSpiral3).colspan(5)
        innerTable.add(tables[3]).fill().colspan(8)

        innerTable.validate()
        innerTable.layout()
        return innerTable
    }

    override fun build(bitmapFont: BitmapFont, batch: Batch) {

        val stack = Stack()

        stack.add(buildTables(batch))
        stack.add(Table().apply { this.add((Label("testing1231231234123412341234123412341", Label.LabelStyle(bitmapFont, backgroundColor.label().color())))) })

        this.add(stack)

    }
}