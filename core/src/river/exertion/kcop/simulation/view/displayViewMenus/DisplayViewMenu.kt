package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuNavParams
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.LogViewMessage
import river.exertion.kcop.system.messaging.messages.MenuNavMessage
import river.exertion.kcop.system.view.ShapeDrawerConfig

interface DisplayViewMenu {

    var screenWidth : Float
    var screenHeight : Float

    val backgroundColor : ColorPalette
    val sdcMap : MutableMap<Int, ShapeDrawerConfig?>

    val breadcrumbEntries : Map<String, String> //menu tags -> menu labels
    val navs : MutableList<ActionParam> //Button Label -> action
    val actions : MutableList<ActionParam> //Button Label -> log text + action to run

    fun menuPane(bitmapFont: BitmapFont) : Table?
    fun navButtonPane(bitmapFont: BitmapFont) : Table = Table().apply {
       // this.debug()

        this@DisplayViewMenu.navs.forEach { navEntry ->
            this.add(
                TextButton(navEntry.label, TextButton.TextButtonStyle().apply { this.font = bitmapFont} ).apply {
                    this.onClick {
                        navEntry.action()
                    this.center()
                    }
                }
            ).padTop(ViewType.padHeight(screenHeight))
            if (navEntry != this@DisplayViewMenu.navs.last()) this.row()
        }
    }

    fun actionButtonPane(bitmapFont: BitmapFont) : Table = Table().apply {
     //   this.debug()

        this@DisplayViewMenu.actions.forEach { actionEntry ->
            this.add(
                TextButton(actionEntry.label, TextButton.TextButtonStyle().apply { this.font = bitmapFont} ).apply {
                    this.onClick {
                        if (actionEntry.log != null)
                            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, actionEntry.log!!))
                        actionEntry.action()
                    }
                }
            ).right().padRight(ViewType.padWidth(screenWidth))
        }
    }

    fun breadcrumbPane(bitmapFont: BitmapFont) = Table().apply {
      //  this.debug()

        //TODO : singleton with three-sized bitmap fonts
        breadcrumbEntries.entries.reversed().forEach { menuLabel ->
            this.add(Label("${menuLabel.value} > ", Label.LabelStyle(bitmapFont.apply {this.data.setScale(FontSize.SMALL.fontScale())}
                , backgroundColor.label().color())).apply {
                this.onClick {
                    MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(menuLabel.key) ))
                }
            } )
        }
        this.right()

        return this
    }

    fun menuLayout(batch : Batch, bitmapFont: BitmapFont) : Table {
        if (sdcMap[0] != null) sdcMap[0]!!.dispose()
        if (sdcMap[1] != null) sdcMap[1]!!.dispose()

        sdcMap[0] = ShapeDrawerConfig(batch, backgroundColor.color())
        sdcMap[1] = ShapeDrawerConfig(batch, backgroundColor.triad().second.color())

        val background = TextureRegionDrawable(
            sdcMap[0]!!.textureRegion.apply {this.setRegion(0, 0,
                ViewType.secondWidth(screenWidth).toInt() - 1,
                ViewType.secondHeight(screenHeight).toInt() - 1)
            })

        val scrollBackground = TextureRegionDrawable(
            sdcMap[1]!!.textureRegion.apply {this.setRegion(0, 0, 20, 20) }
        )

        val scrollNine = NinePatch(TextureRegion(TextureRegion(Texture("images/kobold64.png")), 20, 20, 20, 20))
        val scrollPaneStyle = ScrollPane.ScrollPaneStyle(scrollBackground, null, null, null, NinePatchDrawable(scrollNine))

        val menuPane = menuPane(bitmapFont)

        return Table().apply {
            this.add(Stack().apply {
                this.add(Image(background))
                this.add(
                    Table().apply {
                        this.add(breadcrumbPane(bitmapFont)).right().growX()
                        this.add(
                            Table().apply { this.add(Label(this@DisplayViewMenu.label(), Label.LabelStyle(bitmapFont.apply { this.data.setScale(FontSize.MEDIUM.fontScale()) }, backgroundColor.label().color())).apply {
                                this.setAlignment(Align.center)
                            }).padRight(ViewType.padWidth(screenWidth))
                            }
                        ).center().right()
                        this.row()
                        this.add(
                            if (menuPane == null) { Table() }
                            else {

                                ScrollPane(menuPane, scrollPaneStyle).apply {
                                    // https://github.com/raeleus/skin-composer/wiki/ScrollPane
                                    this.fadeScrollBars = false
                                    this.setFlickScroll(false)
                                    this.validate()
                                    //https://gamedev.stackexchange.com/questions/96096/libgdx-scrollpane-wont-scroll-to-bottom-after-adding-children-to-contained-tab
                                    this.layout()
                                }
                            }
                        ).height(ViewType.secondHeight(screenHeight) - 3 * bitmapFont.lineHeight)
                            .width(ViewType.secondWidth(screenWidth) - 3 * bitmapFont.lineHeight)
                            .growY().top()
                        this.add(navButtonPane(bitmapFont)).top()
                        this.row()
                        this.add(actionButtonPane(bitmapFont)).colspan(2).right()
                  //      this.debug()
                    }
                )
            })
      //      this.debug()
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