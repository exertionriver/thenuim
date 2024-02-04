package river.exertion.kcop.sim.colorPalette

import river.exertion.kcop.base.Id
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteInputProcessor
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayoutHandler
import river.exertion.kcop.view.klop.IDisplayViewKlop

object ColorPaletteDisplayKlop : IDisplayViewKlop {

    override val id = Id.randomId()
    override val tag = "colorPalette"
    override val name = KcopBase.appName
    override val version = KcopBase.appVersion

    override fun load() { }

    override fun unload() { hideView() }

    override fun inputProcessor() = ColorPaletteInputProcessor

    override fun displayViewLayoutHandler() = ColorPaletteLayoutHandler

    override fun dispose() { }
}