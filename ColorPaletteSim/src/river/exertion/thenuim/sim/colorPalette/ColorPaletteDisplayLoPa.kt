package river.exertion.thenuim.sim.colorPalette

import river.exertion.thenuim.base.Id
import river.exertion.thenuim.base.TnmBase
import river.exertion.thenuim.sim.colorPalette.view.ColorPaletteInputProcessor
import river.exertion.thenuim.sim.colorPalette.view.ColorPaletteLayoutHandler
import river.exertion.thenuim.view.IDisplayViewLoPa

object ColorPaletteDisplayLoPa : IDisplayViewLoPa {

    override val id = Id.randomId()
    override val tag = "colorPalette"
    override val name = TnmBase.appName
    override val version = TnmBase.appVersion

    override fun load() { }

    override fun unload() { hideView() }

    override fun inputProcessor() = ColorPaletteInputProcessor

    override fun displayViewLayoutHandler() = ColorPaletteLayoutHandler

    override fun dispose() { }
}