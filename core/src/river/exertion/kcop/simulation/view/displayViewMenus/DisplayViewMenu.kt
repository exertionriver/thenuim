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
    val sdcMap : MutableMap<Int, ShapeDrawerConfig?>

    fun menuPane(bitmapFont: BitmapFont) : Table
    fun navButtonPane(bitmapFont: BitmapFont) : Table = Table().apply {
       // this.debug()

        this@DisplayViewMenu.navs.entries.forEach { navEntry ->
            this.add(
                TextButton(navEntry.key, TextButton.TextButtonStyle().apply { this.font = bitmapFont} ).apply {
                    this.onClick {
                        MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(null, navEntry.value))
                    this.center()
                    }
                }
            ).padTop(ViewType.padHeight(screenHeight))
            if (navEntry != this@DisplayViewMenu.navs.entries.last()) this.row()
        }
    }

    fun actionButtonPane(bitmapFont: BitmapFont) : Table = Table().apply {
     //   this.debug()

        this@DisplayViewMenu.actions.entries.forEach { actionEntry ->
            this.add(
                TextButton(actionEntry.key, TextButton.TextButtonStyle().apply { this.font = bitmapFont} ).apply {
                    this.onClick {
                        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, actionEntry.value))
                        //go back a menu
                        MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(null, ProfileMenuParams(breadcrumbEntries.keys.toList()[0]) ))
                    }
                }
            ).padRight(ViewType.padWidth(screenWidth))
        }
    }


    val breadcrumbEntries : Map<String, String> //menu tags -> menu labels
    val navs : Map<String, MenuParams> //Button Label -> menu params
    val actions : Map<String, String> //Button Label -> log text

    fun breadcrumbPane(bitmapFont: BitmapFont) = Table().apply {
      //  this.debug()

        //TODO : singleton with three-sized bitmap fonts
        breadcrumbEntries.entries.reversed().forEach { menuLabel ->
            this.add(Label("${menuLabel.value} > ", Label.LabelStyle(bitmapFont.apply {this.data.setScale(FontSize.SMALL.fontScale())}
                , backgroundColor.label().color())).apply {
                this.onClick {
                    MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(null, ProfileMenuParams(menuLabel.key) ))
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

                        ScrollPane(menuPane(bitmapFont), scrollPaneStyle).apply {
                            // https://github.com/raeleus/skin-composer/wiki/ScrollPane
                            this.fadeScrollBars = false
                            this.setFlickScroll(false)
                            this.validate()
                            //https://gamedev.stackexchange.com/questions/96096/libgdx-scrollpane-wont-scroll-to-bottom-after-adding-children-to-contained-tab
                            this.layout()
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