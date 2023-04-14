package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuNavParams
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.profile.PSSelection
import river.exertion.kcop.system.profile.settings.PSShowTimer
import river.exertion.kcop.system.profile.ProfileSetting
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.AMHLoadMessage
import river.exertion.kcop.system.messaging.messages.MenuNavMessage
import river.exertion.kcop.system.profile.settings.PSCompStatus
import river.exertion.kcop.system.view.ShapeDrawerConfig

class ProfileSettingsMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("olive")

    var profileSettings : MutableMap<String, String> = mutableMapOf()

    fun profileSettings() = profileSettings

    var psSelections: List<PSSelection> = listOf(
        PSShowTimer, PSCompStatus
    )

    val scrollNine = NinePatch(TextureRegion(TextureRegion(Texture("images/kobold64.png")), 20, 20, 20, 20))
    val scrollBG = TextureRegionDrawable(TextureRegion(TextureRegion(Texture("images/kobold64.png")), 20, 20, 20, 20))
    val scrollPaneStyle = ScrollPane.ScrollPaneStyle(scrollBG, null, null, null, NinePatchDrawable(scrollNine))

    override fun menuPane(bitmapFont: BitmapFont) = Table().apply {

        psSelections.forEach { selection ->
            this.add(Label(selection.selectionLabel, Label.LabelStyle(bitmapFont, backgroundColor.label().color())).apply { this.wrap } ).left()
            this.add(SelectBox<String>(SelectBox.SelectBoxStyle(
                    bitmapFont, backgroundColor.label().color(), null,
                    scrollPaneStyle,
                    com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle().apply {
                        this.font = bitmapFont
                        this.selection = TextureRegionDrawable(TextureRegion(Texture("images/kobold64.png")))
                    }
            )).apply {
                this.items = selection.options.map { it.optionValue }.toGdxArray()
                this.selected = profileSettings[selection.selectionKey]
                this.onChange { this@ProfileSettingsMenu.profileSettings[selection.selectionKey] = this.selected }
            })
            this.row()
        }

        this.top()
    }

    override val breadcrumbEntries = mapOf(
        MainMenu.tag to MainMenu.label
    )

    override val navs = mutableListOf<ActionParam>()

    override val actions = mutableListOf(
        ActionParam("Update", {
            Switchboard.closeMenu()
            Switchboard.updateSettings(profileSettings().entries.map { ProfileSetting(it.key, it.value) }.toMutableList())
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.UpdateSelectedProfileFromComponents))
        }, "Settings Updated!"),
        ActionParam("Cancel", { MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(breadcrumbEntries.keys.toList()[0]) ))})
    )

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "profileSettingsMenu"
        const val label = "Profile Settings"
    }

}