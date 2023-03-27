package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.simulation.view.displayViewMenus.params.ProfileReqMenu
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.view.ShapeDrawerConfig

class SaveProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu,
    ProfileReqMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("olive")

    override var profileAssets = listOf<ProfileAsset>()
    override var narrativeAssets = listOf<NarrativeAsset>()

    override var selectedProfileAsset : ProfileAsset? = null
    override var selectedNarrativeAsset : NarrativeAsset? = null

    var filename : String? = null

    override fun menuPane(bitmapFont: BitmapFont) = Table().apply {
        val profileAssetInfo = selectedProfileAsset?.profileAssetInfo()

        if (profileAssetInfo?.isNotEmpty() == true) {
            this.add(Label(selectedProfileAsset?.profileAssetTitle(), Label.LabelStyle(bitmapFont, backgroundColor.label().color())).apply {
                this.wrap = true
            }).colspan(2).growX()
            this.row()

            this.add(Label("name: ", Label.LabelStyle(bitmapFont, backgroundColor.label().color())))

            val nameTextField = TextField(selectedProfileAsset?.profile!!.name, TextField.TextFieldStyle(bitmapFont, backgroundColor.label().color(), null, null, null))
            nameTextField.setTextFieldListener { textField, key -> filename = textField.text }
            this.add(nameTextField).growX()
            this.row()

            profileAssetInfo.forEach { profileEntry ->
                this.add(Label(profileEntry, Label.LabelStyle(bitmapFont, backgroundColor.label().color())).apply {
                    this.wrap = true
                }).colspan(2).growX().left()
                this.row()
            }
        } else {
            this.add(Label("no profile entry found", Label.LabelStyle(bitmapFont, backgroundColor.label().color())).apply {
                this.wrap = true
            }).growX().left()
            this.row()
        }
        this.top()
//        this.debug()
    }

    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val navs = mutableListOf<ActionParam>()

    fun yesOrErrorAction() : ActionParam {
        return if ( selectedProfileAsset != null ) {
            ActionParam("Yes", {
                Switchboard.saveProfile(selectedProfileAsset!!)
                Switchboard.closeMenu()
            }, "Profile Saved : ${selectedProfileAsset!!.profile!!.name}")
        } else
            ActionParam("Error", {})
    }

    override val actions = mutableListOf(
        yesOrErrorAction(),
        ActionParam("No", {})
    )

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "saveProfileMenu"
        const val label = "Save"
    }

}