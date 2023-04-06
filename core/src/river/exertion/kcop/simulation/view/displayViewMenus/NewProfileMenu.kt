package river.exertion.kcop.simulation.view.displayViewMenus

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
import river.exertion.kcop.system.messaging.messages.MenuNavMessage
import river.exertion.kcop.system.view.ShapeDrawerConfig

class NewProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("olive")

    var selectedProfileAssetName : String? = null

    fun selectedProfileAssetName() = selectedProfileAssetName ?: NameTypes.COMMON.nextName()

    override fun menuPane(bitmapFont: BitmapFont) = Table().apply {
        selectedProfileAssetName = selectedProfileAssetName()

        this.add(Label("profile name: ", Label.LabelStyle(bitmapFont, backgroundColor.label().color())))

        val nameTextField = TextField(selectedProfileAssetName(), TextField.TextFieldStyle(bitmapFont, backgroundColor.label().color(), null, null, null)).apply {
//                this.alignment = Align.top
        }
        nameTextField.setTextFieldListener {
           textField, _ -> selectedProfileAssetName = textField.text
           this@NewProfileMenu.actions.firstOrNull { it.label == "Create" }?.apply { this.log = "Profile Created : $selectedProfileAssetName" }
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
            Switchboard.newProfile(selectedProfileAssetName())
        }, "Profile Created!"),
        //go back a menu
        ActionParam("Cancel", { MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(breadcrumbEntries.keys.toList()[0]) ))})
    )

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "newProfileMenu"
        const val label = "New"
    }

}