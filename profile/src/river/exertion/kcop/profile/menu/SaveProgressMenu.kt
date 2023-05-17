package river.exertion.kcop.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.layout.MenuView
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.menu.MenuActionParam

object SaveProgressMenu : DisplayViewMenu {

    override val tag = "saveProgressMenu"
    override val label = "Save Progress"

    override val backgroundColor = ColorPalette.of("olive")

    override fun menuPane() = Table().apply {

        ProfileAsset.currentProfileAsset.assetInfo().forEach { profileEntry ->
            this.add(Label(profileEntry, KcopSkin.skin
            //        Label.LabelStyle(bitmapFont, backgroundColor.label().color())
            ).apply {
                this.wrap = true
            }).colspan(2).growX()
            this.row()
        }

        this.top()
    }

    override val breadcrumbEntries = mapOf(
        MainMenu.tag to MainMenu.label
    )

    override val assignableNavs = mutableListOf<MenuActionParam>()

    override val actions = mutableListOf(
        MenuActionParam("Save", {
            ProfileAsset.currentProfileAsset.save()
            MenuView.closeMenu()
        }, "Progress Saved!"),
        MenuActionParam("Cancel", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        })
    )
}