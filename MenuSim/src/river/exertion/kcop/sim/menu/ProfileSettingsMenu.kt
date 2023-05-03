package river.exertion.kcop.sim.menu

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.menu.messaging.MenuDataMessage
import river.exertion.kcop.sim.menu.messaging.MenuDataMessage.Companion.MenuDataBridge
import river.exertion.kcop.sim.menu.params.ActionParam
import river.exertion.kcop.view.ColorPalette
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.KcopSkin.Companion.KcopSkinBridge
import river.exertion.kcop.view.SdcHandler
import river.exertion.kcop.view.SdcHandler.Companion.SDCBridge
import river.exertion.kcop.view.layout.MenuView
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.MenuViewMessage.Companion.MenuViewBridge

class ProfileSettingsMenu(override var screenWidth: Float, override var screenHeight: Float) : Telegraph,
    DisplayViewMenu {

    init {
        MessageChannelHandler.enableReceive(MenuDataBridge, this)

        MessageChannelHandler.enableReceive(SDCBridge, this)
        MessageChannelHandler.enableReceive(KcopSkinBridge, this)

    }

    override lateinit var sdcHandler : SdcHandler
    override lateinit var kcopSkin: KcopSkin

    override val backgroundColor = ColorPalette.of("olive")

    var profileSettings : MutableMap<String, String> = mutableMapOf()

    fun profileSettings() = profileSettings
/*
    var psSelections: List<PSSelection> = listOf(
        PSShowTimer, PSCompStatus
    )
*/
//    val scrollNine = NinePatch(TextureRegion(TextureRegion(Texture("images/kobold64.png")), 20, 20, 20, 20))
//    val scrollBG = TextureRegionDrawable(TextureRegion(TextureRegion(Texture("images/kobold64.png")), 20, 20, 20, 20))
//    val scrollPaneStyle = ScrollPane.ScrollPaneStyle(scrollBG, null, null, null, NinePatchDrawable(scrollNine))

    override fun menuPane() = Table().apply {
/*
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
*/
        this.top()
    }

    override val breadcrumbEntries = mapOf(
        MainMenu.tag to MainMenu.label
    )

    override val navs = mutableListOf<ActionParam>()

    override val actions = mutableListOf(
        ActionParam("Update", {
            MenuView.closeMenu()
//            Switchboard.updateSettings(profileSettings().entries.map { ProfileSetting(it.key, it.value) }.toMutableList())
//            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.UpdateSelectedProfileFromComponents))
        }, "Settings Updated!"),
        ActionParam("Cancel", {
            MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(breadcrumbEntries.keys.toList()[0]) )
        })
    )

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(SDCBridge, msg.message) ) -> {
                    sdcHandler = MessageChannelHandler.receiveMessage(SDCBridge, msg.extraInfo)
                    return true
                }
                (MessageChannelHandler.isType(KcopSkinBridge, msg.message) ) -> {
                    kcopSkin = MessageChannelHandler.receiveMessage(KcopSkinBridge, msg.extraInfo)
                    return true
                }
                (MessageChannelHandler.isType(MenuDataBridge, msg.message)) -> {
                    val menuDataMessage: MenuDataMessage = MessageChannelHandler.receiveMessage(MenuDataBridge, msg.extraInfo)

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