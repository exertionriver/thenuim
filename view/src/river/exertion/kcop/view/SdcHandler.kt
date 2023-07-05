package river.exertion.kcop.view

import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.asset.view.ColorPalette.Companion.randomW3cBasic
import river.exertion.kcop.base.KcopBase

object SdcHandler {

    var sdcRegister = mutableMapOf<String, ShapeDrawerConfig>()

    private fun add(tag : String, baseCP : ColorPalette, alpha : Float? = 1f) { sdcRegister[tag] =
        ShapeDrawerConfig(KcopBase.twoBatch, baseCP.color(), alpha)
    }

    fun getBlackAlpha(tag : String, alpha: Float? = 1f) : ShapeDrawerConfig {
        return if ( sdcRegister.containsKey(tag) ) sdcRegister[tag]!!.apply { this.setAlpha(alpha!!) }
        else {
            add(tag, KcopSkin.BackgroundColor, alpha)
            sdcRegister[tag]!!
        }
    }

    fun get(tag: String, baseCP: ColorPalette, alpha: Float? = 1f) : ShapeDrawerConfig {
        return if ( sdcRegister.containsKey(tag) ) sdcRegister[tag]!!.apply { this.setColor(baseCP.color(), alpha) }
        else {
            add(tag, baseCP, alpha)
            sdcRegister[tag]!!
        }
    }

    fun get(tag : String) : ShapeDrawerConfig {
        return if ( sdcRegister.containsKey(tag) ) sdcRegister[tag]!!
        else {
            add(tag, randomW3cBasic())
            sdcRegister[tag]!!
        }
    }

    fun dispose() {
        sdcRegister.values.forEach { it.dispose() }
        sdcRegister.clear()
    }

}