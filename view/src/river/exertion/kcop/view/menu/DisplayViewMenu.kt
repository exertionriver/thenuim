package river.exertion.kcop.view.menu

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.SdcHandler
import river.exertion.kcop.view.KcopFont
import river.exertion.kcop.view.layout.LogView
import river.exertion.kcop.view.layout.ViewType

interface DisplayViewMenu {

    val tag : String
    val label : String
    val backgroundColor : ColorPalette

    val breadcrumbEntries : Map<String, String> //menu tags -> menu labels
    val assignableActions : MutableList<MenuActionParam> //Button Label -> log text + action to run
    val assignableNavs : MutableList<MenuActionParam>

    var menuPane : () -> Table

    fun navButtonPane() : Table = Table().apply {
       // this.debug()

        this@DisplayViewMenu.assignableNavs.forEach { navEntry ->
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
            if (navEntry != this@DisplayViewMenu.assignableNavs.last()) this.row()
        }
    }

    fun actionButtonPane() : Table = Table().apply {
     //   this.debug()

        this@DisplayViewMenu.assignableActions.forEach { actionEntry ->
            this.add(
                TextButton(actionEntry.label, KcopSkin.skin)
                        //TextButton.TextButtonStyle().apply { this.font = bitmapFont} )
                        .apply {
                    if (actionEntry.enabled) {
                        this.onClick {
                            if (actionEntry.log != null)
                                LogView.addLog(actionEntry.log!!)
                            actionEntry.action()
                        }
                    }
                }
            ).right().padRight(ViewType.padWidth(KcopSkin.screenWidth))
        }
    }

    fun breadcrumbPane() = Table().apply {
      //  this.debug()

        breadcrumbEntries.entries.reversed().forEach { menuLabel ->
            this.add(Label("${menuLabel.value} > ", KcopSkin.labelStyle(KcopFont.SMALL, backgroundColor.label()))
                    .apply {
                this.onClick {
                    DisplayViewMenuHandler.currentMenuTag = menuLabel.key
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

        return Table().apply {
            this.add(Stack().apply {
                this.add(Image(menuColorTexture()))
                this.add(
                    Table().apply {
                        this.add(breadcrumbPane()).right().growX()
                        this.add(
                            Table().apply {
                                this.add(Label(this@DisplayViewMenu.label, KcopSkin.labelStyle(KcopFont.MEDIUM, backgroundColor.label()))
                            .apply {
                                this.setAlignment(Align.center)
                            }).padRight(ViewType.padWidth(KcopSkin.screenWidth))
                            }
                        ).center().right()
                        this.row()
                        this.add(
                            ScrollPane(menuPane(), KcopSkin.skin).apply {
                                // https://github.com/raeleus/skin-composer/wiki/ScrollPane
                                this.fadeScrollBars = false
                                this.setFlickScroll(false)
                                this.validate()
                                //https://gamedev.stackexchange.com/questions/96096/libgdx-scrollpane-wont-scroll-to-bottom-after-adding-children-to-contained-tab
                                this.layout()
                            }
                        ).height(ViewType.secondHeight(KcopSkin.screenHeight) - 3 * KcopFont.large().lineHeight)
                            .width(ViewType.secondWidth(KcopSkin.screenWidth) - 3 * KcopFont.large().lineHeight)
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