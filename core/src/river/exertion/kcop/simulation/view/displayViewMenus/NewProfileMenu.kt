package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import river.exertion.kcop.assets.AssetManagerHandler
import river.exertion.kcop.narrative.character.NameTypes
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuNavParams
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.DisplayViewMenuMessage
import river.exertion.kcop.system.messaging.messages.MenuDataMessage
import river.exertion.kcop.system.messaging.messages.MenuNavMessage
import river.exertion.kcop.system.profile.Profile
import river.exertion.kcop.system.view.ShapeDrawerConfig

class NewProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("olive")

    var newName : String? = null

    fun newName() = newName ?: Profile.genName()

    override fun menuPane(bitmapFont: BitmapFont) = Table().apply {
        newName = newName()

        this.add(Label("profile name: ", Label.LabelStyle(bitmapFont, backgroundColor.label().color())))

        val nameTextField = TextField(newName, TextField.TextFieldStyle(bitmapFont, backgroundColor.label().color(), null, null, null)).apply {
//                this.alignment = Align.top
        }
        nameTextField.setTextFieldListener {
           textField, _ -> newName = textField.text
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
            Switchboard.closeMenu()
            Switchboard.newProfile(newName())
        }, "Profile Created: ${newName()}"),
        //go back a menu
        ActionParam("Cancel", {
            MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(breadcrumbEntries.keys.toList()[0]) )
        })
    )

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "newProfileMenu"
        const val label = "New"
    }

}