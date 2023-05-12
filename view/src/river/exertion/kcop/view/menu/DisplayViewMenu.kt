package river.exertion.kcop.view.menu

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.asset.view.FontSize
import river.exertion.kcop.asset.view.KcopSkin
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.SdcHandler
import river.exertion.kcop.view.ViewPackage
import river.exertion.kcop.view.ViewPackage.LogViewBridge
import river.exertion.kcop.view.layout.ViewType
import river.exertion.kcop.view.messaging.DisplayViewMessage
import river.exertion.kcop.view.messaging.LogViewMessage
import river.exertion.kcop.view.messaging.menuParams.ActionParam

interface DisplayViewMenu {

    val tag : String
    val label : String
    val backgroundColor : ColorPalette

    val breadcrumbEntries : Map<String, String> //menu tags -> menu labels
    fun navs() : MutableList<ActionParam> //Button Label -> action
    val actions : MutableList<ActionParam> //Button Label -> log text + action to run

    fun menuPane() : Table?
    fun navButtonPane() : Table = Table().apply {
       // this.debug()

        this@DisplayViewMenu.navs().forEach { navEntry ->
            this.add(
                TextButton(navEntry.label, KcopSkin.skin)
                        //TextButton.TextButtonStyle().apply { this.font = bitmapFont} )
                        .apply {
                    this.onClick {
                        navEntry.action()
                    this.center()
                    }
                }
            ).padTop(ViewType.padHeight(KcopSkin.screenHeight))
            if (navEntry != this@DisplayViewMenu.navs().last()) this.row()
        }
    }

    fun actionButtonPane() : Table = Table().apply {
     //   this.debug()

        this@DisplayViewMenu.actions.forEach { actionEntry ->
            this.add(
                TextButton(actionEntry.label, KcopSkin.skin)
                        //TextButton.TextButtonStyle().apply { this.font = bitmapFont} )
                        .apply {
                    this.onClick {
                        if (actionEntry.log != null)
                            MessageChannelHandler.send(LogViewBridge, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, actionEntry.log!!))
                        actionEntry.action()
                    }
                }
            ).right().padRight(ViewType.padWidth(KcopSkin.screenWidth))
        }
    }

    fun breadcrumbPane() = Table().apply {
      //  this.debug()

        //TODO : singleton with three-sized bitmap fonts
        breadcrumbEntries.entries.reversed().forEach { menuLabel ->
            this.add(Label("${menuLabel.value} > ", KcopSkin.labelStyle(FontSize.SMALL, backgroundColor.label()))
                    .apply {
                this.onClick {
                    DisplayViewMenuHandler.currentMenuTag = menuLabel.key
                    MessageChannelHandler.send(ViewPackage.DisplayViewBridge, DisplayViewMessage(DisplayViewMessage.DisplayViewMessageType.Rebuild) )
                }
            } )
        }
        this.right()

        return this
    }

    fun menuColorTexture() : TextureRegion {
        return SdcHandler.get("menu_$tag", backgroundColor).textureRegion().apply {
            this.setRegion(0, 0, ViewType.secondWidth(KcopSkin.screenWidth).toInt() - 1, ViewType.secondHeight(KcopSkin.screenHeight).toInt() - 1)
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
                            Table().apply {
                                this.add(Label(this@DisplayViewMenu.label, KcopSkin.labelStyle(FontSize.MEDIUM, backgroundColor.label()))
                            .apply {
                                this.setAlignment(Align.center)
                            }).padRight(ViewType.padWidth(KcopSkin.screenWidth))
                            }
                        ).center().right()
                        this.row()
                        this.add(
                            if (menuPane == null) { Table() }
                            else {
                                ScrollPane(menuPane, KcopSkin.skin).apply {
                                    // https://github.com/raeleus/skin-composer/wiki/ScrollPane
                                    this.fadeScrollBars = false
                                    this.setFlickScroll(false)
                                    this.validate()
                                    //https://gamedev.stackexchange.com/questions/96096/libgdx-scrollpane-wont-scroll-to-bottom-after-adding-children-to-contained-tab
                                    this.layout()
                                }
                            }
                        ).height(ViewType.secondHeight(KcopSkin.screenHeight) - 3 * KcopSkin.fontPackage.large.lineHeight)
                            .width(ViewType.secondWidth(KcopSkin.screenWidth) - 3 * KcopSkin.fontPackage.large.lineHeight)
                            .growY().top()
                        this.add(navButtonPane()).top()
                        this.row()
                        this.add(actionButtonPane()).colspan(2).right()
             //           this.debug()
                    }
                )
            })
          //  this.debug()
        }
    }
}