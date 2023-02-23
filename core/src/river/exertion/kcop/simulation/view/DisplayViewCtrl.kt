package river.exertion.kcop.simulation.view

import com.badlogic.gdx.graphics.Texture
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
import kotlin.math.roundToInt

class DisplayViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : ViewCtrl(ViewType.DISPLAY, screenWidth, screenHeight) {

    val panes : MutableList<Stack> = mutableListOf()
    var fullImage : Texture? = null
    var largeImage : Texture? = null
    var mediumImage : Texture? = null
    var smallImage : Texture? = null
    var tinyImage : Texture? = null

    var layoutView = false

    //used for panes large
    val imgIdx = listOf(2, 4, 12, 16)

    fun paneColorTexture(batch : Batch, pane : DisplayViewPane, overrideColor : ColorPalette? = null) : TextureRegion {

        if (overrideColor == null)
            this.sdc = ShapeDrawerConfig(batch, pane.defaultColor().color())
        else
            this.sdc = ShapeDrawerConfig(batch, overrideColor.color())

        return if ( pane.ordinal in listOf(33, 31, 28) )
            //for some reason, top border misses a pixel for small blocks stacked on each other
            sdc!!.textureRegion.apply {this.setRegion(0, 0, pane.width(screenWidth).roundToInt(), pane.height(screenHeight).roundToInt() + 1) }
        else if ( pane.ordinal == 16)
            sdc!!.textureRegion.apply {this.setRegion(0, 0, pane.width(screenWidth).roundToInt() + 1, pane.height(screenHeight).roundToInt()) }
        else
            sdc!!.textureRegion.apply {this.setRegion(0, 0, pane.width(screenWidth).roundToInt(), pane.height(screenHeight).roundToInt()) }
    }

    fun buildTables(bitmapFont: BitmapFont, batch: Batch) : Table {

        val innerTable = Table()

        panes.clear()

        DisplayViewPane.values().forEach { displayViewPane ->
            when (displayViewPane.ordinal) {
                imgIdx[0] -> if (largeImage != null) panes.add(
                    Stack().apply {
                        this.add(Table().apply { this.add(Image(TextureRegionDrawable(TextureRegion(largeImage)))).size(displayViewPane.width(screenWidth), displayViewPane.height(screenHeight))})
                    }) else panes.add (
                    Stack().apply {
                        this.add(Table().apply { this.background = TextureRegionDrawable(paneColorTexture(batch, displayViewPane, ColorPalette.of("black"))) })
                    })
                imgIdx[1] -> if (mediumImage != null) panes.add(
                    Stack().apply {
                        this.add(Table().apply { this.add(Image(TextureRegionDrawable(TextureRegion(mediumImage)))).size(displayViewPane.width(screenWidth), displayViewPane.height(screenHeight))})
                    }) else panes.add (
                    Stack().apply {
                        this.add(Table().apply { this.background = TextureRegionDrawable(paneColorTexture(batch, displayViewPane, ColorPalette.of("black"))) })
                    })
                imgIdx[2] -> if (smallImage != null) panes.add(
                    Stack().apply {
                        this.add(Table().apply { this.add(Image(TextureRegionDrawable(TextureRegion(smallImage)))).size(displayViewPane.width(screenWidth), displayViewPane.height(screenHeight))})
                    }) else panes.add (
                    Stack().apply {
                        this.add(Table().apply { this.background = TextureRegionDrawable(paneColorTexture(batch, displayViewPane, ColorPalette.of("black"))) })
                    })
                imgIdx[3] -> if (tinyImage != null) panes.add(
                    Stack().apply {
                        this.add(Table().apply { this.add(Image(TextureRegionDrawable(TextureRegion(tinyImage)))).size(displayViewPane.width(screenWidth), displayViewPane.height(screenHeight))})
                    }) else panes.add (
                    Stack().apply {
                        this.add(Table().apply { this.background = TextureRegionDrawable(paneColorTexture(batch, displayViewPane, ColorPalette.of("black"))) })
                    })
                else -> if (layoutView) panes.add(
                    Stack().apply {
                        this.add(Table().apply { this.background = TextureRegionDrawable(paneColorTexture(batch, displayViewPane, null)) })
                        this.add(Label(displayViewPane.ordinal.toString(), Label.LabelStyle(bitmapFont, displayViewPane.defaultColor().label().color())))
                    }) else panes.add (
                    Stack().apply {
                        this.add(Table().apply { this.background = TextureRegionDrawable(paneColorTexture(batch, displayViewPane, ColorPalette.of("black"))) })
                    })
                }
            }


        val tlSpiral0 = Table()
        tlSpiral0.add(panes[30]).fill()
        tlSpiral0.add(panes[24]).fill()

        val tlSpiral1 = Table()
        tlSpiral1.add(tlSpiral0).fill()
        tlSpiral1.row()
        tlSpiral1.add(panes[18]).fill()

        val tlSpiral2 = Table()
        tlSpiral2.add(tlSpiral1).fill()
        tlSpiral2.add(panes[11]).fill()

        val tlSpiral3 = Table()
        tlSpiral3.add(panes[7]).fill()
        tlSpiral3.row()
        tlSpiral3.add(tlSpiral2).fill()

        val tlSpiral4 = Table()
        tlSpiral4.add(panes[2]).fill()
        tlSpiral4.add(tlSpiral3).fill()

        innerTable.add(tlSpiral4).fill().colspan(13)

        val trSpiral0a = Table()
        trSpiral0a.add(panes[33]).fill()
        trSpiral0a.row()
        trSpiral0a.add(panes[27]).fill()

        val trSpiral0b = Table()
        trSpiral0b.add(panes[26]).fill()
        trSpiral0b.add(panes[32]).fill()

        val trSpiral1a = Table()
        trSpiral1a.add(panes[21]).fill()
        trSpiral1a.add(trSpiral0a).fill()

        val trSpiral1b = Table()
        trSpiral1b.add(trSpiral0b).fill()
        trSpiral1b.row()
        trSpiral1b.add(panes[20]).fill()

        val trSpiral2a = Table()
        trSpiral2a.add(panes[14]).fill()
        trSpiral2a.row()
        trSpiral2a.add(trSpiral1a).fill()
        trSpiral2a.row()
        trSpiral2a.add(panes[15]).fill()

        val trSpiral2b = Table()
        trSpiral2b.add(trSpiral1b).fill()
        trSpiral2b.add(panes[13]).fill()

        val trSpiral3b = Table()
        trSpiral3b.add(panes[4]).fill()
        trSpiral3b.row()
        trSpiral3b.add(trSpiral2b).fill()

        val trSpiral4 = Table()
        trSpiral4.add(trSpiral2a).fill()
        trSpiral4.add(trSpiral3b).fill()

        innerTable.add(trSpiral4).fill().colspan(8)
        innerTable.row()

        val lSpiral0 = Table()
        lSpiral0.add(panes[28]).fill()
        lSpiral0.row()
        lSpiral0.add(panes[22]).fill()

        val lSpiral1 = Table()
        lSpiral1.add(lSpiral0).fill()
        lSpiral1.add(panes[16]).fill()

        val lSpiral2 = Table()
        lSpiral2.add(panes[9]).fill()
        lSpiral2.row()
        lSpiral2.add(lSpiral1).fill()

        val lSpiral3 = Table()
        lSpiral3.add(panes[5]).fill()
        lSpiral3.add(lSpiral2).fill()

        val rSpiral0 = Table()
        rSpiral0.add(panes[31]).fill()
        rSpiral0.row()
        rSpiral0.add(panes[25]).fill()

        val rSpiral1 = Table()
        rSpiral1.add(panes[19]).fill()
        rSpiral1.add(rSpiral0).fill()

        val rSpiral2 = Table()
        rSpiral2.add(rSpiral1).fill()
        rSpiral2.row()
        rSpiral2.add(panes[12]).fill()

        val rSpiral3 = Table()
        rSpiral3.add(rSpiral2).fill()
        rSpiral3.add(panes[8]).fill()

        innerTable.add(lSpiral3).fill().colspan(8)
        innerTable.add(panes[0]).fill().colspan(5)
        innerTable.add(rSpiral3).fill().colspan(8)

        innerTable.row()

        val brSpiral0 = Table()
        brSpiral0.add(panes[23]).fill()
        brSpiral0.add(panes[29]).fill()

        val brSpiral1 = Table()
        brSpiral1.add(brSpiral0).fill()
        brSpiral1.row()
        brSpiral1.add(panes[17]).fill()

        val brSpiral2 = Table()
        brSpiral2.add(panes[10]).fill()
        brSpiral2.add(brSpiral1).fill()

        val brSpiral3 = Table()
        brSpiral3.add(brSpiral2).fill()
        brSpiral3.row()
        brSpiral3.add(panes[6]).fill()

        innerTable.add(panes[1]).fill().colspan(8)
        innerTable.add(brSpiral3).fill().colspan(5)
        innerTable.add(panes[3]).fill().colspan(8)

        innerTable.validate()
        innerTable.layout()
        return innerTable
    }

    override fun build(bitmapFont: BitmapFont, batch: Batch) {

        val stack = Stack()

        stack.add(buildTables(bitmapFont, batch))
//        stack.add(Table().apply { this.add((Label("testing1231231234123412341234123412341", Label.LabelStyle(bitmapFont, backgroundColor.label().color())))) })

        this.add(stack)

    }
}