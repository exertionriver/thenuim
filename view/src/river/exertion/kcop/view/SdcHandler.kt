package river.exertion.kcop.view

import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.asset.view.ColorPalette.Companion.randomW3cBasic
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.base.Log

object SdcHandler {

    private var sdcRegister = mutableMapOf<String, ShapeDrawerConfig>()

    private fun add(tag : String, baseCP : ColorPalette, alpha : Float? = 1f) { sdcRegister[tag] =
        ShapeDrawerConfig(KcopBase.twoBatch, baseCP.color(), alpha)
    }

    fun has(tag : String) = sdcRegister.keys.firstOrNull { keyTag -> keyTag == tag } != null

    fun getoradBackgroundAlpha(tag : String, alpha: Float? = 1f, baseCP: ColorPalette? = KcopSkin.BackgroundColor) : ShapeDrawerConfig {
        return getorad(tag, baseCP!!, alpha!!)
    }

    fun getorad(tag: String, baseCP: ColorPalette, alpha: Float? = 1f) : ShapeDrawerConfig {
        return if ( sdcRegister.containsKey(tag) ) sdcRegister[tag]!!.apply { this.setColor(baseCP.color(), alpha) }
        else {
            add(tag, baseCP, alpha)
            sdcRegister[tag]!!
        }
    }

    fun dispose() {
        sdcRegister.values.forEach { it.dispose() }
        sdcRegister.clear()
    }

}