package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuParams
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.profile.Profile
import river.exertion.kcop.system.view.ShapeDrawerConfig

class SaveProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu, ProfileReqMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("olive")

    override var profile : ProfileAsset? = null
    override var currentNarrativeAsset : NarrativeAsset? = null
    override val am = AssetManager()

    var filename : String? = null

    override fun menuPane(bitmapFont: BitmapFont) = Table().apply {
        val profileAssetInfo = profileAssetInfo()

        if (profileAssetInfo.isNotEmpty()) {
            this.add(Label(profileAssetTitle(), Label.LabelStyle(bitmapFont, backgroundColor.label().color())).apply {
                this.wrap = true
            }).colspan(2).growX()
            this.row()

            this.add(Label("name: ", Label.LabelStyle(bitmapFont, backgroundColor.label().color())))

            val nameTextField = TextField(profile!!.profile!!.name, TextField.TextFieldStyle(bitmapFont, backgroundColor.label().color(), null, null, null))
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

    override val navs = mapOf<String, MenuParams>()

    override val actions = mutableMapOf(
        "Yes" to Pair("Profile Saved!") { Switchboard.closeMenu() },
        "No" to Pair(null) {}
    )

    fun profileAssetTitle() = profile?.assetPath

    fun profileAssetInfo() : MutableList<String?> {

        val returnList = mutableListOf<String?>()

        if ((profile != null) && (profile!!.profile != null)) {
            returnList.add("current: ${profile!!.profile!!.currentImmersionId}: ${profile!!.profile!!.currentImmersionBlockId}")

            if (profile!!.profile!!.statuses.isNotEmpty()) returnList.add("\nstatuses:")

            val listMaxSize = profile!!.profile!!.statuses.size.coerceAtMost(8)

            profile!!.profile!!.statuses.sortedByDescending { it.value }.subList(0, listMaxSize).forEach {
                returnList.add("${it.key}: ${it.value} (${it.cumlImmersionTime})")
            }

            this.actions["Yes"] = Pair("Profile saved: ${profile!!.profile!!.name}", this.actions["Yes"]!!.second)
        }

        return returnList
    }

    override fun tag() = tag
    override fun label() = label

    override fun dispose() {
        am.dispose()
        super.dispose()
    }

    companion object {
        const val tag = "saveProfileMenu"
        const val label = "Save"
    }

}