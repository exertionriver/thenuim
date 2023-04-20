package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.DisplayViewMenuMessage
import river.exertion.kcop.system.messaging.messages.LogViewMessage
import river.exertion.kcop.system.view.SdcHandler

interface DisplayViewMenu {

    var sdcHandler : SdcHandler
    var kcopSkin : KcopSkin

    fun skin() = kcopSkin.skin

    var screenWidth : Float
    var screenHeight : Float

    val backgroundColor : ColorPalette
//    val sdcMap : MutableMap<Int, ShapeDrawerConfig?>

    val breadcrumbEntries : Map<String, String> //menu tags -> menu labels
    val navs : MutableList<ActionParam> //Button Label -> action
    val actions : MutableList<ActionParam> //Button Label -> log text + action to run

    fun menuPane() : Table?
    fun navButtonPane() : Table = Table().apply {
       // this.debug()

        this@DisplayViewMenu.navs.forEach { navEntry ->
            this.add(
                TextButton(navEntry.label, skin())
                        //TextButton.TextButtonStyle().apply { this.font = bitmapFont} )
                        .apply {
                    this.onClick {
                        navEntry.action()
                    this.center()
                    }
                }
            ).padTop(ViewType.padHeight(screenHeight))
            if (navEntry != this@DisplayViewMenu.navs.last()) this.row()
        }
    }

    fun actionButtonPane() : Table = Table().apply {
     //   this.debug()

        this@DisplayViewMenu.actions.forEach { actionEntry ->
            this.add(
                TextButton(actionEntry.label, skin())
                        //TextButton.TextButtonStyle().apply { this.font = bitmapFont} )
                        .apply {
                    this.onClick {
                        if (actionEntry.log != null)
                            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, actionEntry.log!!))
                        actionEntry.action()
                    }
                }
            ).right().padRight(ViewType.padWidth(screenWidth))
        }
    }

    fun breadcrumbPane() = Table().apply {
      //  this.debug()

        //TODO : singleton with three-sized bitmap fonts
        breadcrumbEntries.entries.reversed().forEach { menuLabel ->
            this.add(Label("${menuLabel.value} > ", skin())
                    //Label.LabelStyle(bitmapFont.apply {this.data.setScale(FontSize.SMALL.fontScale())}
                //, backgroundColor.label().color()))
                .apply {
                this.onClick {
                    MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(menuLabel.key) )
                }
            } )
        }
        this.right()

        return this
    }

    fun menuColorTexture() : TextureRegion {
        return sdcHandler.get("menu_${tag()}", backgroundColor).textureRegion().apply {
            this.setRegion(0, 0, ViewType.secondWidth(screenWidth).toInt() - 1, ViewType.secondHeight(screenHeight).toInt() - 1)
        }
    }

    fun menuLayout() : Table {

        val menuPane = menuPane()

        return Table().apply {
            this.add(Stack().apply {
                this.add(Image(menuColorTexture()))
                this.add(
                    Table().apply {
                        this.add(breadcrumbPane()).right().growX()
                        this.add(
                            Table().apply { this.add(Label(this@DisplayViewMenu.label(), skin())
                                    //Label.LabelStyle(bitmapFont.apply { this.data.setScale(FontSize.MEDIUM.fontScale()) }, backgroundColor.label().color()))
                                    .apply {
                                this.setAlignment(Align.center)
                            }).padRight(ViewType.padWidth(screenWidth))
                            }
                        ).center().right()
                        this.row()
                        this.add(
                            if (menuPane == null) { Table() }
                            else {
//                                ScrollPane(menuPane, scrollPaneStyle).apply {
                                ScrollPane(menuPane, skin()).apply {
                                    // https://github.com/raeleus/skin-composer/wiki/ScrollPane
                                    this.fadeScrollBars = false
                                    this.setFlickScroll(false)
                                    this.validate()
                                    //https://gamedev.stackexchange.com/questions/96096/libgdx-scrollpane-wont-scroll-to-bottom-after-adding-children-to-contained-tab
                                    this.layout()
                                }
                            }
                        ).height(ViewType.secondHeight(screenHeight) - 3 * kcopSkin.fontPackage.large.lineHeight)
                            .width(ViewType.secondWidth(screenWidth) - 3 * kcopSkin.fontPackage.large.lineHeight)
                            .growY().top()
                        this.add(navButtonPane()).top()
                        this.row()
                        this.add(actionButtonPane()).colspan(2).right()
                  //      this.debug()
                    }
                )
            })
      //      this.debug()
        }
    }

    fun tag() : String //= tag //need to override this in implementing menu
    fun label() : String //= label //need to override this in implementing menu

    fun dispose() {
        sdcHandler.dispose()
        kcopSkin.dispose()
    }
}