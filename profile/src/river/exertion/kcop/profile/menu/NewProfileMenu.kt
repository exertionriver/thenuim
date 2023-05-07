package river.exertion.kcop.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.ColorPalette
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewPackage.MenuViewBridge
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.menuParams.ActionParam
import river.exertion.kcop.view.switchboard.ViewSwitchboard

class NewProfileMenu : DisplayViewMenu {

    override val backgroundColor = ColorPalette.of("olive")

    var newName : String? = null

    fun newName() = newName //?: Profile.genName()

    override fun menuPane() = Table().apply {
        newName = newName()

        this.add(Label("profile name: ", KcopSkin.skin))
                //Label.LabelStyle(bitmapFont, backgroundColor.label().color())))

        val nameTextField = TextField(newName, KcopSkin.skin)
        //TextField.TextFieldStyle(bitmapFont, backgroundColor.label().color(), null, null, null)).apply {
//                this.alignment = Align.top
//        }
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

    override val navs = mutableListOf<ActionParam>()

    override val actions = mutableListOf(
        ActionParam("Create", {
            ViewSwitchboard.closeMenu()
//            Switchboard.newProfile(newName())
        }, "Profile Created: ${newName()}"),
        //go back a menu
        ActionParam("Cancel", {
            MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(breadcrumbEntries.keys.toList()[0]) )
        })
    )

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "newProfileMenu"
        const val label = "New"
    }

}