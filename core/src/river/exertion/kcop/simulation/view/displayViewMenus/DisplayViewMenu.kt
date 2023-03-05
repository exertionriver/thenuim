package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette

interface DisplayViewMenu {

    val tag : String
    val label : String
    var screenWidth : Float
    var screenHeight : Float

    val backgroundColor : ColorPalette

    var menuTable : Table
    fun breadcrumb() = Table().apply { this.debug() }

    val sdcMap : MutableMap<Int, ShapeDrawerConfig?>

    fun getMenu(batch : Batch, bitmapFont: BitmapFont) : Table

    fun genericLayout(batch : Batch, bitmapFont: BitmapFont) : Table {

        if (sdcMap[0] != null) sdcMap[0]!!.dispose()

        sdcMap[0] = ShapeDrawerConfig(batch, backgroundColor.color())

        bitmapFont.data.setScale(FontSize.LARGE.fontScale())

        return Table().apply {
            this.add(Stack().apply {
                this.add(
                    Image(
                        TextureRegionDrawable(
                        sdcMap[0]!!.textureRegion.apply {this.setRegion(0, 0,
                            ViewType.secondWidth(screenWidth).toInt() - 1,
                            ViewType.secondHeight(screenHeight).toInt() - 1)
                        })
                    )
                )
                this.add(
                    Table().apply {
                        this.add(breadcrumb()).top().right().growX()
                        this.add(
                            Label(this@DisplayViewMenu.label, Label.LabelStyle(bitmapFont, backgroundColor.label().color()))
                        ).top().right().padRight(ViewType.padWidth(screenWidth)).padTop(ViewType.padHeight(screenHeight))
                        this.row()
                        this.add(menuTable).colspan(2).grow()
                        this.debug()
                    }
                )
            })
            this.debug()
        }
    }


    fun dispose() {
        sdcMap.values.forEach { it?.dispose() }
    }
}