package river.exertion.kcop.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.asset.view.KcopSkin
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.profile.Profile
import river.exertion.kcop.profile.ProfilePackage
import river.exertion.kcop.profile.settings.ProfileSettingEntry
import river.exertion.kcop.profile.settings.ProfileSettingEntry.Companion.optionBySelectionKey
import river.exertion.kcop.profile.settings.ProfileSettingEntry.Companion.updateSetting
import river.exertion.kcop.view.ViewPackage
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.DisplayViewMessage
import river.exertion.kcop.view.messaging.menuParams.ActionParam
import river.exertion.kcop.view.switchboard.ViewSwitchboard

object ProfileSettingsMenu : DisplayViewMenu {

    override val tag = "profileSettingsMenu"
    override val label = "Profile Settings"

    override val backgroundColor = ColorPalette.of("olive")

    var settings : MutableList<ProfileSettingEntry> = mutableListOf()

    override fun menuPane() = Table().apply {
        Profile.availableSettings().forEach { setting ->
            this.add(Label(setting.selectionLabel, KcopSkin.skin).apply {
                this.wrap
            } ).left()
            this.add(
                SelectBox<String>(KcopSkin.skin
            ).apply {
                this.items = setting.options.map { it.optionValue }.toGdxArray()
                this.selected = settings.optionBySelectionKey(setting.selectionKey)
                this.onChange { settings.updateSetting(setting.selectionKey, this.selected) }
            })
            this.row()
        }

        this.top()
    }

    override val breadcrumbEntries = mapOf(
        MainMenu.tag to MainMenu.label
    )

    override fun navs() = mutableListOf<ActionParam>()

    override val actions = mutableListOf(
        ActionParam("Update", {
            ViewSwitchboard.closeMenu()
            ProfilePackage.currentProfileAsset.settings = settings
            ProfilePackage.currentProfileAsset.save()

            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
            MessageChannelHandler.send(ViewPackage.DisplayViewBridge, DisplayViewMessage(DisplayViewMessage.DisplayViewMessageType.Rebuild) )
        }, "Settings Updated!"),
        ActionParam("Cancel", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
            MessageChannelHandler.send(ViewPackage.DisplayViewBridge, DisplayViewMessage(DisplayViewMessage.DisplayViewMessageType.Rebuild) )
        })
    )
}