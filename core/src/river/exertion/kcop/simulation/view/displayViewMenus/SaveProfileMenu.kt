package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import river.exertion.kcop.assets.AssetManagerHandler
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuNavParams
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.MenuNavMessage
import river.exertion.kcop.system.view.ShapeDrawerConfig

class SaveProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("olive")

    var selectedProfileAssetInfo: List<String>? = null
    var selectedProfileAssetName : String? = null

    fun selectedProfileAssetName() = selectedProfileAssetName ?: throw Exception("SaveProfileMenu requires valid selectProfileAssetName")

    override fun menuPane(bitmapFont: BitmapFont) = Table().apply {
        if (selectedProfileAssetInfo != null) {
            this.add(Label("save name: ", Label.LabelStyle(bitmapFont, backgroundColor.label().color())))

            val nameTextField = TextField(selectedProfileAssetName(), TextField.TextFieldStyle(bitmapFont, backgroundColor.label().color(), null, null, null)).apply {
//                this.alignment = Align.top
            }
            nameTextField.setTextFieldListener {
                textField, _ -> selectedProfileAssetName = textField.text
                this@SaveProfileMenu.actions.firstOrNull { it.label == "Overwrite" }?.apply { this.log = "Profile Saved : $selectedProfileAssetName" }
            }
            this.add(nameTextField).growX().top()
            this.row()

            selectedProfileAssetInfo!!.forEach { profileEntry ->
                this.add(Label(profileEntry, Label.LabelStyle(bitmapFont, backgroundColor.label().color())).apply {
                    this.wrap = true
                }).colspan(2).growX()
                this.row()
            }
            this@SaveProfileMenu.actions.firstOrNull { it.label == "Overwrite" }?.apply { this.log = "Profile Saved : $selectedProfileAssetName" }
            //      this.debug()
        } else {
            this.add(Label("no profile info found", Label.LabelStyle(bitmapFont, backgroundColor.label().color()))
            ).growX().left()
            this@SaveProfileMenu.actions.firstOrNull { it.label == "Overwrite" }?.apply { this.label = "Error"; this.action = {} }
        }
        this.top()
    }

    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val navs = mutableListOf<ActionParam>()

    override val actions = mutableListOf(
        ActionParam("Overwrite", {
            Switchboard.closeMenu()
            Switchboard.saveSelectedProfile(selectedProfileAssetName(), AssetManagerHandler.SaveType.Overwrite)
        }, "Profile Saved!"),
/*        ActionParam("Merge", {
            Switchboard.closeMenu()
            Switchboard.saveProfile(selectedProfileAsset!!, AssetManagerHandler.SaveType.Merge)
        }, "Profile Saved : ${selectedProfileAsset?.profile?.name}"),
 */        //go back a menu
        ActionParam("Cancel", { MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(breadcrumbEntries.keys.toList()[0]) ))})
    )

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "saveProfileMenu"
        const val label = "Save"
    }

}