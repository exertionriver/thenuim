package river.exertion.thenuim.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import river.exertion.thenuim.ecs.component.IComponent
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.profile.ProfileKlop
import river.exertion.thenuim.profile.ProfileKlop.ProfileMenuBackgroundColor
import river.exertion.thenuim.profile.asset.ProfileAsset
import river.exertion.thenuim.profile.component.ProfileComponent
import river.exertion.thenuim.profile.messaging.ProfileComponentMessage
import river.exertion.thenuim.view.KcopSkin
import river.exertion.thenuim.view.KcopFont
import river.exertion.thenuim.view.layout.ButtonView
import river.exertion.thenuim.view.menu.DisplayViewMenu
import river.exertion.thenuim.view.menu.DisplayViewMenuHandler
import river.exertion.thenuim.view.menu.MainMenu
import river.exertion.thenuim.view.menu.MenuActionParam

object NewProfileMenu : DisplayViewMenu {

    override val tag = "newProfileMenu"
    override val label = "New"

    override val backgroundColor = ProfileMenuBackgroundColor

    var newName = ""

    override var menuPane = {

        val nameTextField = TextField(newName, KcopSkin.skin)

        updateNameFromTextField()

        nameTextField.setTextFieldListener {
            textField, _ -> this@NewProfileMenu.newName = textField.text
            updateNameFromTextField()
        }

        Table().apply {
            this.add(Label("profile name: ", KcopSkin.labelStyle(KcopFont.SMALL, ProfileKlop.ProfileMenuText)))
            this.add(nameTextField).growX().top()
            this.row()
            this.top()
        }
    }

    private fun updateNameFromTextField() {
        this@NewProfileMenu.assignableActions.firstOrNull { it.label == "Create" }?.apply { this.log = "Profile Created : ${this@NewProfileMenu.newName}" }
    }

    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val assignableNavs = mutableListOf<MenuActionParam>()

    override val assignableActions = mutableListOf(
        MenuActionParam("Create", {
            ButtonView.closeMenu()
            MessageChannelHandler.send(ProfileKlop.ProfileBridge, ProfileComponentMessage(ProfileComponentMessage.ProfileMessageType.Inactivate))
            ProfileAsset.currentProfileAsset = ProfileAsset.new(newName)
            ProfileAsset.currentProfileAsset.save()
            IComponent.ecsInit<ProfileComponent>()
        }, "Profile Created!"),
        //go back a menu
        MenuActionParam("Cancel", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        })
    )

}