package river.exertion.kcop.sim.colorPalette

import river.exertion.kcop.asset.Id
import river.exertion.kcop.bundle.IDisplayPackage
import river.exertion.kcop.bundle.IInternalPackage
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteInputProcessor
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayout

object ColorPalettePackage : IInternalPackage, IDisplayPackage {

    override var id = Id.randomId()

    override var name = this::class.simpleName.toString()

    override fun loadChannels() { }

    override fun inputProcessor() = ColorPaletteInputProcessor

    override fun displayViewLayoutHandler() = ColorPaletteLayout

    override fun dispose() { }
}