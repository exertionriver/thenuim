package river.exertion.thenuim.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.thenuim.profile.Profile
import river.exertion.thenuim.profile.ProfileLoPa
import river.exertion.thenuim.profile.ProfileLoPa.ProfileMenuBackgroundColor
import river.exertion.thenuim.profile.asset.ProfileAsset
import river.exertion.thenuim.profile.settings.ProfileSettingEntry
import river.exertion.thenuim.profile.settings.ProfileSettingEntry.Companion.optionBySelectionKey
import river.exertion.thenuim.profile.settings.ProfileSettingEntry.Companion.updateSetting
import river.exertion.thenuim.view.TnmSkin
import river.exertion.thenuim.view.TnmFont
import river.exertion.thenuim.view.layout.ButtonView
import river.exertion.thenuim.view.menu.DisplayViewMenu
import river.exertion.thenuim.view.menu.DisplayViewMenuHandler
import river.exertion.thenuim.view.menu.MainMenu
import river.exertion.thenuim.view.menu.MenuActionParam

object ProfileSettingsMenu : DisplayViewMenu {

    override val tag = "profileSettingsMenu"
    override val label = "Profile Settings"

    override val backgroundColor = ProfileMenuBackgroundColor

    var settings : MutableList<ProfileSettingEntry> = mutableListOf()

    override var menuPane = {
        Table().apply {
            Profile.availableSettings.forEach { setting ->
                this.add(Label(setting.selectionLabel, TnmSkin.labelStyle(
                    TnmFont.SMALL,
                    ProfileLoPa.ProfileMenuText
                )).apply {
                    this.wrap
                } ).left()

                this.add(
                    SelectBox<String>(TnmSkin.skin
                ).apply {
                    this.items = setting.options.map { it.optionValue }.toGdxArray()
                    this.selected = settings.optionBySelectionKey(setting.selectionKey)
                    this.onChange { settings.updateSetting(setting.selectionKey, this.selected) }
                }).right()
                this.row()
            }

            this.top().left()
        }
    }

    override val breadcrumbEntries = mapOf(
        MainMenu.tag to MainMenu.label
    )

    override val assignableNavs = mutableListOf<MenuActionParam>()

    override val assignableActions = mutableListOf(
        MenuActionParam("Update", {
            ButtonView.closeMenu()
            ProfileAsset.currentProfileAsset.settings = settings
            ProfileAsset.currentProfileAsset.profile.execSettings()

            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        }, "Settings Updated!"),
        MenuActionParam("Cancel", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        })
    )
}