package river.exertion.thenuim.view.menu

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.thenuim.asset.view.ColorPalette
import river.exertion.thenuim.view.TnmSkin
import river.exertion.thenuim.view.SdcHandler
import river.exertion.thenuim.view.TnmFont
import river.exertion.thenuim.view.layout.LogView
import river.exertion.thenuim.view.layout.ViewType

interface DisplayViewMenu {

    val tag : String
    val label : String
    val backgroundColor : ColorPalette

    val breadcrumbEntries : Map<String, String> //menu tags -> menu labels
    val assignableActions : MutableList<MenuActionParam> //Button Label -> log text + action to run
    val assignableNavs : MutableList<MenuActionParam>

    var menuPane : () -> Table

    fun lastAssignableActionsIdx() = assignableActions.size - 1

    fun addAssignableAction(menuActionParam : MenuActionParam, idx : Int? = lastAssignableActionsIdx()) {

         assignableActions.add(idx!!, menuActionParam)
    }

    fun removeAssignableAction(menuActionParam : MenuActionParam) {

        assignableActions.remove(menuActionParam)
    }

    fun lastAssignableNavsIdx() = assignableNavs.size - 1

    fun addAssignableNav(menuActionParam : MenuActionParam, idx : Int? = lastAssignableNavsIdx()) {

        assignableNavs.add(idx!!, menuActionParam)
    }

    fun removeAssignableNav(menuActionParam : MenuActionParam) {

        assignableNavs.remove(menuActionParam)
    }

    fun menuActionButton(menuActionParam: MenuActionParam) : TextButton {
        return TextButton(menuActionParam.label, TnmSkin.skin).apply {
            if (menuActionParam.enabled) {
                this.onClick {
                    if (menuActionParam.log != null) LogView.addLog(menuActionParam.log!!)
                    menuActionParam.action()
                    this.center()
                }
            }
        }
    }

    fun navButtonPane() : Table = Table().apply {
        this@DisplayViewMenu.assignableNavs.forEach { navEntry ->
            this.add( menuActionButton(navEntry) )
                .padTop(ViewType.padHeight(TnmSkin.screenHeight))
                .padRight(ViewType.padWidth(TnmSkin.screenWidth))
                .right()
            if (navEntry != this@DisplayViewMenu.assignableNavs.last()) this.row()
        }
    }

    fun actionButtonPane() : Table = Table().apply {
        this@DisplayViewMenu.assignableActions.forEach { actionEntry ->
            this.add( menuActionButton(actionEntry) )
                .padBottom(ViewType.padHeight(TnmSkin.screenHeight))
                .padRight(ViewType.padWidth(TnmSkin.screenWidth))
                .right()
        }
    }

    fun breadcrumbLabel(menuTag : String, menuLabel: String) : Label {
        return Label("$menuLabel > ", TnmSkin.labelStyle(TnmFont.SMALL, backgroundColor.label()))
            .apply {
                this.onClick {
                    DisplayViewMenuHandler.currentMenuTag = menuTag
                }
            }
    }

    fun breadcrumbPane() = Table().apply {
        breadcrumbEntries.entries.reversed().forEach { breadcrumbEntry ->
            this.add( breadcrumbLabel(breadcrumbEntry.key, breadcrumbEntry.value) )
        }
        this.right()

        return this
    }

    fun menuColorTexture() : TextureRegion {
        return SdcHandler.updorad("menu_$tag", backgroundColor).textureRegion().apply {
            this.setRegion(0, 0, ViewType.secondWidth(TnmSkin.screenWidth).toInt() - 1, ViewType.secondHeight(TnmSkin.screenHeight).toInt() - 1)
        }
    }

    fun menuScrollPane() : ScrollPane {
        return ScrollPane(menuPane(), TnmSkin.skin).apply {
            // https://github.com/raeleus/skin-composer/wiki/ScrollPane
            this.fadeScrollBars = false
            this.setFlickScroll(false)
            this.validate()
            //https://gamedev.stackexchange.com/questions/96096/libgdx-scrollpane-wont-scroll-to-bottom-after-adding-children-to-contained-tab
            this.layout()
        }
    }

    fun menuLabel() : Label {
        return Label(this@DisplayViewMenu.label, TnmSkin.labelStyle(TnmFont.MEDIUM, backgroundColor.label()))
            .apply {
                this.setAlignment(Align.center)
            }
    }

    fun menuContent() : Table {
        return Table().apply {
            this.add(breadcrumbPane())
                .growX()
                .right()
            this.add(menuLabel())
                .padRight(ViewType.padWidth(TnmSkin.screenWidth))
                .right()
            this.row()
            //scrollPane / nav superpane
            if ( (assignableNavs.isEmpty()) && (assignableActions.isEmpty()) ) {
                this.add(menuScrollPane())
                    .padLeft(ViewType.padWidth(TnmSkin.screenWidth))
                    .padBottom(ViewType.padHeight(TnmSkin.screenHeight))
                    .grow().left().top().colspan(2)
            } else if (assignableNavs.isEmpty()) {
                this.add(menuScrollPane())
                    .padLeft(ViewType.padWidth(TnmSkin.screenWidth))
                    .grow().left().top().colspan(2)
            } else { //if (assignableActions.isEmpty())
                this.add(menuScrollPane())
                    .padLeft(ViewType.padWidth(TnmSkin.screenWidth))
                    .padBottom(ViewType.padHeight(TnmSkin.screenHeight))
                    .grow().left().top()
            }
            if (assignableNavs.isNotEmpty()) {
                this.add(navButtonPane()).top()
            }
            // action pane
            this.row()
            this.add(actionButtonPane()).colspan(2).right()
        }
    }

    fun menuLayout() : Table {

        return Table().apply {
            this.add(Stack().apply {
                this.add(Image(menuColorTexture()))
                this.add(menuContent())
            }).height(ViewType.secondHeight(TnmSkin.screenHeight) + ViewType.seventhHeight(TnmSkin.screenHeight))
                .width(ViewType.secondWidth(TnmSkin.screenWidth) + ViewType.seventhWidth(TnmSkin.screenWidth))
                .growY()
        }
    }
}