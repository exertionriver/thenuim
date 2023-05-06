package river.exertion.kcop.view

import com.badlogic.gdx.graphics.g2d.Batch
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.ColorPalette.Companion.randomW3cBasic

class SdcHandler(var batch : Batch, var backgroundColorPalette: ColorPalette) {

    var sdcRegister = mutableMapOf<String, ShapeDrawerConfig>()

    private fun add(tag : String, baseCP : ColorPalette, alpha : Float? = 1f) { sdcRegister[tag] =
        ShapeDrawerConfig(batch, baseCP.color(), alpha)
    }

    fun getBlackAlpha(tag : String, alpha: Float? = 1f) : ShapeDrawerConfig {
        return if ( sdcRegister.containsKey(tag) ) sdcRegister[tag]!!.apply { this.setAlpha(alpha!!) }
        else {
            add(tag, backgroundColorPalette, alpha)
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