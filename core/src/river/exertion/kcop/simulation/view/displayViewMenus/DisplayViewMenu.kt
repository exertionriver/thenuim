package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.system.ShapeDrawerConfig

interface DisplayViewMenu {

    val tag : String
    var screenWidth : Float
    var screenHeight : Float

    val sdcMap : MutableMap<Int, ShapeDrawerConfig?>

    fun getMenu(batch : Batch) : Table

    fun dispose() {
        sdcMap.values.forEach { it?.dispose() }
    }
}