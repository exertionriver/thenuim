package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.*
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.system.profile.PSSelection
import river.exertion.kcop.system.profile.settings.PSShowTimer
import river.exertion.kcop.system.profile.ProfileSetting
import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.AMHLoadMessage
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.system.messaging.messages.MenuDataMessage
import river.exertion.kcop.system.profile.settings.PSCompStatus
import river.exertion.kcop.base.view.SdcHandler
import river.exertion.kcop.view.ColorPalette

class ProfileSettingsMenu(override var screenWidth: Float, override var screenHeight: Float) : Telegraph, DisplayViewMenu {

    init {
        MessageChannelEnum.INTER_MENU_BRIDGE.enableReceive(this)

        MessageChannelEnum.SDC_BRIDGE.enableReceive(this)
        MessageChannelEnum.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    override lateinit var sdcHandler : SdcHandler
    override lateinit var kcopSkin: KcopSkin

    override val backgroundColor = ColorPalette.of("olive")

    var profileSettings : MutableMap<String, String> = mutableMapOf()

    fun profileSettings() = profileSettings

    var psSelections: List<PSSelection> = listOf(
        PSShowTimer, PSCompStatus
    )

//    val scrollNine = NinePatch(TextureRegion(TextureRegion(Texture("images/kobold64.png")), 20, 20, 20, 20))
//    val scrollBG = TextureRegionDrawable(TextureRegion(TextureRegion(Texture("images/kobold64.png")), 20, 20, 20, 20))
//    val scrollPaneStyle = ScrollPane.ScrollPaneStyle(scrollBG, null, null, null, NinePatchDrawable(scrollNine))

    override fun menuPane() = Table().apply {

        psSelections.forEach { selection ->
            this.add(Label(selection.selectionLabel, skin())
            .apply { this.wrap } ).left()
            this.add(SelectBox<String>(skin()
            ).apply {
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
            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.UpdateSelectedProfileFromComponents))
        }, "Settings Updated!"),
        ActionParam("Cancel", {
            MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, MenuViewMessage(breadcrumbEntries.keys.toList()[0]) )
        })
    )

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelEnum.SDC_BRIDGE.isType(msg.message) ) -> {
                    sdcHandler = MessageChannelEnum.SDC_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannelEnum.KCOP_SKIN_BRIDGE.isType(msg.message) ) -> {
                    kcopSkin = MessageChannelEnum.KCOP_SKIN_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannelEnum.INTER_MENU_BRIDGE.isType(msg.message)) -> {
                    val menuDataMessage: MenuDataMessage = MessageChannelEnum.INTER_MENU_BRIDGE.receiveMessage(msg.extraInfo)

                    if ( menuDataMessage.profileMenuDataParams != null ) {

                        if (menuDataMessage.profileMenuDataParams!!.profileAssetTitles != null && menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo != null) {
                            profileSettings = menuDataMessage.profileMenuDataParams!!.profileAssetTitles!!.zip(menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo!!).toMap().toMutableMap()
                        }
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "profileSettingsMenu"
        const val label = "Profile Settings"
    }

}