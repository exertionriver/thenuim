package river.exertion.kcop.view

import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.base.KcopBase

object SdcHandler {

    private var sdcRegister = mutableMapOf<String, ShapeDrawerConfig>()

    private fun add(tag : String, baseCP : ColorPalette, alpha : Float? = 1f) { sdcRegister[tag] =
        ShapeDrawerConfig(KcopBase.twoBatch, baseCP.color(), alpha)
    }

    fun has(tag : String) = sdcRegister.keys.firstOrNull { keyTag -> keyTag == tag } != null

    fun updoradBackgroundAlpha(tag : String, alpha: Float? = 1f) : ShapeDrawerConfig {
        return getorad(tag).apply { this.setColor(this.baseColor, alpha)}
    }

    fun updorad(tag: String, baseCP: ColorPalette? = KcopSkin.BackgroundColor, alpha: Float? = 1f) : ShapeDrawerConfig {
        return getorad(tag, baseCP, alpha).apply { this.setColor(baseCP!!.color(), alpha!!) }
    }

    fun getorad(tag: String, baseCP: ColorPalette? = KcopSkin.BackgroundColor, alpha: Float? = 1f) : ShapeDrawerConfig {
        return if ( sdcRegister.containsKey(tag) ) sdcRegister[tag]!!
        else {
            add(tag, baseCP!!, alpha!!)
            sdcRegister[tag]!!
        }
    }

    fun dispose() {
        sdcRegister.values.forEach { it.dispose() }
        sdcRegister.clear()
    }

}