package river.exertion.kcop.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.profile.ProfilePackage
import river.exertion.kcop.profile.ProfilePackage.ProfileMenuBackgroundColor
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.asset.FontSize
import river.exertion.kcop.view.layout.MenuView
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.menu.MenuActionParam

object SaveProgressMenu : DisplayViewMenu {

    override val tag = "saveProgressMenu"
    override val label = "Save Progress"

    override val backgroundColor = ProfileMenuBackgroundColor

    override var menuPane = {

        Table().apply {

            ProfileAsset.currentProfileAsset.assetInfo().forEach { profileEntry ->
                this.add(Label(profileEntry, KcopSkin.labelStyle(FontSize.SMALL, ProfilePackage.ProfileMenuText)
                ).apply {
                    this.wrap = true
                }).colspan(2).growX()
                this.row()
            }

            this.top()
        }
    }

    override val breadcrumbEntries = mapOf(
        MainMenu.tag to MainMenu.label
    )

    override val assignableNavs = mutableListOf<MenuActionParam>()

    const val SaveLabel = "Save"

    override val actions = mutableListOf(
        MenuActionParam(SaveLabel, {
            ProfileAsset.currentProfileAsset.save()
            MenuView.closeMenu()
        }, "Progress Saved!"),
        MenuActionParam("Cancel", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        })
    )

}