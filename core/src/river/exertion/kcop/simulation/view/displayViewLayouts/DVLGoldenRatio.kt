package river.exertion.kcop.simulation.view.displayViewLayouts

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import river.exertion.kcop.simulation.view.DisplayViewPane
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import kotlin.math.roundToInt

class DVLGoldenRatio(var screenWidth: Float = 50f, var screenHeight: Float = 50f) : DisplayViewLayout {

    override var layoutMode = true
    override val maskPixmap = Pixmap(16, 16, Pixmap.Format.RGBA8888)

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()
    override val paneTextures : MutableMap<Int, Texture?> = mutableMapOf()
    override val paneTextureMaskAlpha : MutableMap<Int, Float?> = mutableMapOf()
    override val paneRefiners : MutableMap<Int, Vector2?> = mutableMapOf(
        16 to Vector2(1f, 0f),
        28 to Vector2(0f, 1f),
        31 to Vector2(0f, 1f),
        33 to Vector2(0f, 1f)
    )

    override fun definePanes() : MutableMap<Int, DisplayViewPane> {
        val panes : MutableMap<Int, DisplayViewPane> = mutableMapOf()

        panes[0] = DisplayViewPane.MEDIUM_BY_MEDIUM
        panes[1] = DisplayViewPane.LARGE_BY_LARGE
        panes[2] = DisplayViewPane.LARGE_BY_LARGE
        panes[3] = DisplayViewPane.LARGE_BY_LARGE
        panes[4] = DisplayViewPane.MEDIUM_BY_MEDIUM
        panes[5] = DisplayViewPane.MEDIUM_BY_MEDIUM
        panes[6] = DisplayViewPane.MEDIUM_BY_MEDIUM
        panes[7] = DisplayViewPane.MEDIUM_BY_MEDIUM
        panes[8] = DisplayViewPane.MEDIUM_BY_MEDIUM
        panes[9] = DisplayViewPane.SMALL_BY_SMALL
        panes[10] = DisplayViewPane.SMALL_BY_SMALL
        panes[11] = DisplayViewPane.SMALL_BY_SMALL
        panes[12] = DisplayViewPane.SMALL_BY_SMALL
        panes[13] = DisplayViewPane.SMALL_BY_SMALL
        panes[14] = DisplayViewPane.SMALL_BY_SMALL
        panes[15] = DisplayViewPane.SMALL_BY_SMALL
        panes[16] = DisplayViewPane.TINY_BY_TINY
        panes[17] = DisplayViewPane.TINY_BY_TINY
        panes[18] = DisplayViewPane.TINY_BY_TINY
        panes[19] = DisplayViewPane.TINY_BY_TINY
        panes[20] = DisplayViewPane.TINY_BY_TINY
        panes[21] = DisplayViewPane.TINY_BY_TINY
        panes[22] = DisplayViewPane.UNIT_BY_UNIT
        panes[23] = DisplayViewPane.UNIT_BY_UNIT
        panes[24] = DisplayViewPane.UNIT_BY_UNIT
        panes[25] = DisplayViewPane.UNIT_BY_UNIT
        panes[26] = DisplayViewPane.UNIT_BY_UNIT
        panes[27] = DisplayViewPane.UNIT_BY_UNIT
        panes[28] = DisplayViewPane.UNIT_BY_UNIT
        panes[29] = DisplayViewPane.UNIT_BY_UNIT
        panes[30] = DisplayViewPane.UNIT_BY_UNIT
        panes[31] = DisplayViewPane.UNIT_BY_UNIT
        panes[32] = DisplayViewPane.UNIT_BY_UNIT
        panes[33] = DisplayViewPane.UNIT_BY_UNIT

        return panes
    }

    //used for panes large
    override fun paneColorTexture(batch : Batch, pane : MutableMap.MutableEntry<Int, DisplayViewPane>, overrideColor : ColorPalette?) : TextureRegion {

        val paneColor = overrideColor ?: ColorPalette.of("black")

        if (sdcMap[pane.key] != null) sdcMap[pane.key]!!.dispose()

        sdcMap[pane.key] = ShapeDrawerConfig(batch, paneColor.color())

        return sdcMap[pane.key]!!.textureRegion.apply {this.setRegion(0, 0,
            pane.value.width(screenWidth).roundToInt() + (paneRefiners[pane.key]?.x ?: 0f).toInt(),
            pane.value.height(screenHeight).roundToInt() + (paneRefiners[pane.key]?.y ?: 0f).toInt())
        }
    }

    override fun buildPaneCtrls(bitmapFont: BitmapFont, batch: Batch) : MutableMap<Int, Stack> {

    val paneCtrls : MutableMap<Int, Stack> = mutableMapOf()

    definePanes().entries.sortedBy { it.key }.forEach { displayViewPane ->
        paneCtrls[displayViewPane.key] =
            Stack().apply {
                if (layoutMode) { //random color
                    this.add(Table().apply { this.background = TextureRegionDrawable(paneColorTexture(batch, displayViewPane, ColorPalette.randomW3cBasic())) })
                } else if (paneTextures[displayViewPane.key] != null) { //image present
                    this.add(Table().apply { this.add(Image(TextureRegionDrawable(TextureRegion(paneTextures[displayViewPane.key])))).size(
                        displayViewPane.value.width(this@DVLGoldenRatio.screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f).toInt(),
                        displayViewPane.value.height(this@DVLGoldenRatio.screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f).toInt())
                    })
                    if (paneTextureMaskAlpha[displayViewPane.key] != null) {
                        this.add(Table().apply { this.add(Image(TextureRegionDrawable(TextureRegion(Texture(maskPixmap.apply { this.setAlpha(paneTextureMaskAlpha[displayViewPane.key]); this.fill() }))))).size(
                            displayViewPane.value.width(this@DVLGoldenRatio.screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f).toInt(),
                            displayViewPane.value.height(this@DVLGoldenRatio.screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f).toInt(),)})
                    }
                } else { //background black
                    this.add(Table().apply { this.background = TextureRegionDrawable(paneColorTexture(batch, displayViewPane, null)) })
                }
            }
        }

        return paneCtrls
    }

    override fun buildPaneTable(bitmapFont : BitmapFont, batch : Batch) : Table {

        val panes = buildPaneCtrls(bitmapFont, batch)

        val innerTable = Table()

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
}
