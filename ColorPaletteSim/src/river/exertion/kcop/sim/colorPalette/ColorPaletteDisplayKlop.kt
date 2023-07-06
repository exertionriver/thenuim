package river.exertion.kcop.sim.colorPalette

import com.badlogic.gdx.InputMultiplexer
import river.exertion.kcop.base.Id
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteInputProcessor
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayout
import river.exertion.kcop.view.klop.IDisplayViewKlop
import river.exertion.kcop.view.layout.DisplayView
import river.exertion.kcop.view.layout.ViewLayout

object ColorPaletteDisplayKlop : IDisplayViewKlop {

    override var id = Id.randomId()

    override var name = this::class.simpleName.toString()

    override var inputMultiplexer: InputMultiplexer? = null

    override fun load() { }

    override fun unload() { hideView() }

    override fun inputProcessor() = ColorPaletteInputProcessor

    override fun displayViewLayoutHandler() = ColorPaletteLayout

    override fun dispose() { }
}