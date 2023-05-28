package river.exertion.kcop.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.profile.ProfilePackage
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.profile.component.ProfileComponent
import river.exertion.kcop.profile.messaging.ProfileComponentMessage
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewPackage
import river.exertion.kcop.view.layout.MenuView
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.menu.MenuActionParam

object NewProfileMenu : DisplayViewMenu {

    override val tag = "newProfileMenu"
    override val label = "New"

    override val backgroundColor = ColorPalette.of("olive")

    var newName = ""

    override var menuPane = {

        val nameTextField = TextField(newName, KcopSkin.skin)

        updateNameFromTextField()

        nameTextField.setTextFieldListener {
            textField, _ -> this@NewProfileMenu.newName = textField.text
            updateNameFromTextField()
        }

        Table().apply {
            this.add(Label("profile name: ", KcopSkin.skin))
            this.add(nameTextField).growX().top()
            this.row()
            this.top()
        }
    }

    private fun updateNameFromTextField() {
        this@NewProfileMenu.actions.firstOrNull { it.label == "Create" }?.apply { this.log = "Profile Created : ${this@NewProfileMenu.newName}" }
    }

    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val assignableNavs = mutableListOf<MenuActionParam>()

    override val actions = mutableListOf(
        MenuActionParam("Create", {
            MenuView.closeMenu()
            MessageChannelHandler.send(ProfilePackage.ProfileBridge, ProfileComponentMessage(ProfileComponentMessage.ProfileMessageType.Inactivate))
            ProfileAsset.currentProfileAsset = ProfileAsset.new(newName)
            ProfileAsset.currentProfileAsset.save()
            ProfileComponent.ecsInit()
        }, "Profile Created!"),
        //go back a menu
        MenuActionParam("Cancel", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        })
    )

}