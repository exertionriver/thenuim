package river.exertion.thenuim.sim.colorPalette

import river.exertion.thenuim.base.Id
import river.exertion.thenuim.base.KcopBase
import river.exertion.thenuim.sim.colorPalette.view.ColorPaletteInputProcessor
import river.exertion.thenuim.sim.colorPalette.view.ColorPaletteLayoutHandler
import river.exertion.thenuim.view.klop.IDisplayViewKlop

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