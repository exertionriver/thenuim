package river.exertion.kcop.view

import com.badlogic.gdx.graphics.g2d.Batch
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.asset.view.ColorPalette.Companion.randomW3cBasic
import river.exertion.kcop.asset.view.KcopSkin

object SdcHandler {

    var sdcRegister = mutableMapOf<String, ShapeDrawerConfig>()
    lateinit var batch : Batch

    private fun add(tag : String, baseCP : ColorPalette, alpha : Float? = 1f) { sdcRegister[tag] =
        ShapeDrawerConfig(batch, baseCP.color(), alpha)
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