package river.exertion.kcop.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.profile.Profile
import river.exertion.kcop.profile.ProfilePackage
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.view.ColorPalette
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewPackage
import river.exertion.kcop.view.ViewPackage.MenuViewBridge
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.DisplayViewMessage
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.menuParams.ActionParam
import river.exertion.kcop.view.switchboard.ViewSwitchboard

object NewProfileMenu : DisplayViewMenu {

    override val backgroundColor = ColorPalette.of("olive")

    var newName = ""

    override fun menuPane() = Table().apply {

        this.add(Label("profile name: ", KcopSkin.skin))
                //Label.LabelStyle(bitmapFont, backgroundColor.label().color())))

        val nameTextField = TextField(newName, KcopSkin.skin)

        nameTextField.setTextFieldListener {
           textField, _ -> this@NewProfileMenu.newName = textField.text
            this@NewProfileMenu.actions.first { it.label == "Create"}.log = "Profile Created: ${this@NewProfileMenu.newName}"
        }
        this.add(nameTextField).growX().top()
        this.row()

//      this.debug()

        this.top()
    }

    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override fun navs() = mutableListOf<ActionParam>()

    override val actions = mutableListOf(
        ActionParam("Create", {
            ViewSwitchboard.closeMenu()
            ProfilePackage.currentProfileAsset = ProfileAsset.new(newName)
            ProfilePackage.currentProfileAsset.save()
        }, "Profile Created: $newName"),
        //go back a menu
        ActionParam("Cancel", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
            MessageChannelHandler.send(ViewPackage.DisplayViewBridge, DisplayViewMessage(DisplayViewMessage.DisplayViewMessageType.Rebuild) )
        })
    )

    override fun tag() = tag
    override fun label() = label

    const val tag = "newProfileMenu"
    const val label = "New"

}