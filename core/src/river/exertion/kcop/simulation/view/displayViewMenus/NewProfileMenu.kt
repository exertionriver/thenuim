package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.DisplayViewMenuMessage
import river.exertion.kcop.system.profile.Profile
import river.exertion.kcop.system.view.SdcHandler

class NewProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : Telegraph, DisplayViewMenu {

    init {
        MessageChannel.SDC_BRIDGE.enableReceive(this)
        MessageChannel.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    override lateinit var sdcHandler : SdcHandler
    override lateinit var kcopSkin: KcopSkin

    override val backgroundColor = ColorPalette.of("olive")

    var newName : String? = null

    fun newName() = newName ?: Profile.genName()

    override fun menuPane() = Table().apply {
        newName = newName()

        this.add(Label("profile name: ", skin()))
                //Label.LabelStyle(bitmapFont, backgroundColor.label().color())))

        val nameTextField = TextField(newName, skin())
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
            Switchboard.closeMenu()
            Switchboard.newProfile(newName())
        }, "Profile Created: ${newName()}"),
        //go back a menu
        ActionParam("Cancel", {
            MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(breadcrumbEntries.keys.toList()[0]) )
        })
    )

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.SDC_BRIDGE.isType(msg.message) ) -> {
                    sdcHandler = MessageChannel.SDC_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannel.KCOP_SKIN_BRIDGE.isType(msg.message) ) -> {
                    kcopSkin = MessageChannel.KCOP_SKIN_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
            }
        }
        return false
    }

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "newProfileMenu"
        const val label = "New"
    }

}