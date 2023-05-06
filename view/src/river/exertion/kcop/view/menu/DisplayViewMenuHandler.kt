package river.exertion.kcop.view.menu

import com.badlogic.gdx.scenes.scene2d.ui.Table

object DisplayViewMenuHandler {

    val displayViewMenus = mutableListOf<DisplayViewMenu>()

    var currentMenuTag = MainMenu.tag

    fun checkByTag(menuTag : String) : DisplayViewMenu? {
        return displayViewMenus.firstOrNull { it.tag() == menuTag }
    }

    fun byTag(menuTag : String) : DisplayViewMenu {
        return checkByTag(menuTag) ?: throw Exception("${this::class.simpleName}:${DisplayViewMenuHandler::byTag.name} : displayViewMenu $menuTag not found")
    }

    fun buildByTag(menuTag : String) : Table = byTag(menuTag).menuLayout()

    fun addMenu(displayViewMenu : DisplayViewMenu) {
        val menuEntryCheck = checkByTag(displayViewMenu.tag())

        if (menuEntryCheck == null) displayViewMenus.add(displayViewMenu) else throw Exception("${this::class.simpleName}:${DisplayViewMenuHandler::addMenu.name} : displayViewMenu ${displayViewMenu.tag()} already added")
    }

    fun addMenus(displayViewMenus: List<DisplayViewMenu>) {
        displayViewMenus.forEach { addMenu(it) }
    }

    fun removeMenu(displayViewMenu: DisplayViewMenu) {
        val menuEntryToRemove = byTag(displayViewMenu.tag())

        displayViewMenus.remove(menuEntryToRemove)
    }

    fun removeMenus(displayViewMenus : List<DisplayViewMenu>) {
        displayViewMenus.forEach { removeMenu(it) }
    }
}