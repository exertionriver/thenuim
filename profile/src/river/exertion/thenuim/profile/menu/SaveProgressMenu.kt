package river.exertion.thenuim.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.thenuim.profile.ProfileLoPa
import river.exertion.thenuim.profile.ProfileLoPa.ProfileMenuBackgroundColor
import river.exertion.thenuim.profile.asset.ProfileAsset
import river.exertion.thenuim.view.TnmSkin
import river.exertion.thenuim.view.TnmFont
import river.exertion.thenuim.view.layout.ButtonView
import river.exertion.thenuim.view.menu.DisplayViewMenu
import river.exertion.thenuim.view.menu.DisplayViewMenuHandler
import river.exertion.thenuim.view.menu.MainMenu
import river.exertion.thenuim.view.menu.MenuActionParam

object SaveProgressMenu : DisplayViewMenu {

    override val tag = "saveProgressMenu"
    override val label = "Save Progress"

    override val backgroundColor = ProfileMenuBackgroundColor

    override var menuPane = {

        Table().apply {

            ProfileAsset.currentProfileAsset.assetInfo().forEach { profileEntry ->
                this.add(Label(profileEntry, TnmSkin.labelStyle(TnmFont.SMALL, ProfileLoPa.ProfileMenuText)
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

    override val assignableActions = mutableListOf(
        MenuActionParam(SaveLabel, {
            ProfileAsset.currentProfileAsset.save()
            ButtonView.closeMenu()
        }, "Progress Saved!"),
        MenuActionParam("Cancel", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        })
    )

}