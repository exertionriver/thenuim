package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.view.DisplayViewMenuMessage
import river.exertion.kcop.system.view.LogViewMessage
import river.exertion.kcop.system.view.LogViewMessageType

interface DisplayViewMenu {

    var screenWidth : Float
    var screenHeight : Float

    val backgroundColor : ColorPalette

    var menuTable : Table
    val breadcrumbEntries : Map<String, String> //menu tags -> labels

    val sdcMap : MutableMap<Int, ShapeDrawerConfig?>

    fun getMenu(batch : Batch, bitmapFont: BitmapFont) : Table

    fun breadcrumb(bitmapFont: BitmapFont) = Table().apply {
        this.debug()

        //TODO : singleton with three-sized bitmap fonts
        breadcrumbEntries.entries.reversed().forEach { menuLabel ->
            this.add(Label("${menuLabel.value} > ", Label.LabelStyle(bitmapFont.apply {this.data.setScale(FontSize.SMALL.fontScale())}
                , backgroundColor.label().color())).apply {
                this.onClick {
                    MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(null, menuLabel.key ))
                }
            } )
        }
        this.right()

        return this
    }

    fun genericLayout(batch : Batch, bitmapFont: BitmapFont) : Table {

        if (sdcMap[0] != null) sdcMap[0]!!.dispose()

        sdcMap[0] = ShapeDrawerConfig(batch, backgroundColor.color())

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
                        this.add(breadcrumb(bitmapFont)).center().right().growX()
                        this.add(
                            Table().apply { this.add(Label(this@DisplayViewMenu.label(), Label.LabelStyle(bitmapFont.apply { this.data.setScale(FontSize.LARGE.fontScale()) }, backgroundColor.label().color())))}
                        ).center().right().padRight(ViewType.padWidth(screenWidth))
                        this.row()
                        this.add(menuTable).colspan(2).grow()
                        this.debug()
                    }
                )
            })
            this.debug()
        }
    }

    fun tag() = tag //need to override this in implementing menu
    fun label() = label //need to override this in implementing menu

    companion object {
        const val tag = "DisplayViewMenu"
        const val label = "DisplayViewMenu"
    }

    fun dispose() {
        sdcMap.values.forEach { it?.dispose() }
    }
}