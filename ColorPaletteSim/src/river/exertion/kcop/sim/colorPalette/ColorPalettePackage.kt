package river.exertion.kcop.sim.colorPalette

import river.exertion.kcop.messaging.Id
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IDisplayPackage
import river.exertion.kcop.plugin.IInternalPackage
import river.exertion.kcop.sim.colorPalette.messaging.ColorPaletteMessage
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