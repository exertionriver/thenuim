package river.exertion.kcop.profile.menu

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.profile.ProfilePackage.Companion.ProfileMenuDataBridge
import river.exertion.kcop.profile.messaging.ProfileMenuDataMessage
import river.exertion.kcop.view.ColorPalette
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewPackage.MenuViewBridge
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.menuParams.ActionParam
import river.exertion.kcop.view.switchboard.ViewSwitchboard

class LoadProfileMenu : Telegraph, DisplayViewMenu {

    init {
        MessageChannelHandler.enableReceive(ProfileMenuDataBridge, this)
    }

    override val backgroundColor = ColorPalette.of("teal")

    var selectedProfileAssetInfo: List<String>? = null
    var selectedProfileAssetName : String? = null

    fun selectedProfileAssetName() = selectedProfileAssetName ?: throw Exception("LoadProfileMenu requires valid selectProfileAssetName")

    override fun menuPane() = Table().apply {

        if (selectedProfileAssetInfo != null) {
            selectedProfileAssetInfo!!.forEach { profileEntry ->
                this.add(Label(profileEntry, KcopSkin.skin)
                        //LabelStyle(bitmapFont, backgroundColor.label().color()))
                        .apply {
                    this.wrap = true
                }).growX().left()
                this.row()
            }
//        this.debug()
            this@LoadProfileMenu.actions.firstOrNull { it.label == "Yes" }?.apply { this.log = "Profile Loaded : ${selectedProfileAssetName()}" }
        } else {
            this.add(Label("no profile info found", KcopSkin.skin)
//                    LabelStyle(bitmapFont, backgroundColor.label().color()))
            ).growX().left()
            this@LoadProfileMenu.actions.firstOrNull { it.label == "Yes" }?.apply { this.label = "Error"; this.action = {} }
        }
        this.top()
    }

    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val navs = mutableListOf<ActionParam>()

    override val actions = mutableListOf(
        ActionParam("Yes", {
            ViewSwitchboard.closeMenu()
//            Switchboard.loadSelectedProfile()
        }, "Profile Loaded!"),
        //go back a menu
        ActionParam("No", {
            MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(breadcrumbEntries.keys.toList()[0]) )
        })
    )

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(ProfileMenuDataBridge, msg.message)) -> {
                    val menuDataMessage: ProfileMenuDataMessage = MessageChannelHandler.receiveMessage(ProfileMenuDataBridge, msg.extraInfo)

                    if ( menuDataMessage.profileMenuDataParams != null ) {

                        if (menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo != null) {
                            selectedProfileAssetInfo = menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo
                        }
                        if (menuDataMessage.profileMenuDataParams!!.selectedProfileAssetName != null) {
                            selectedProfileAssetName = menuDataMessage.profileMenuDataParams!!.selectedProfileAssetName
                        }
                    } else {
                        selectedProfileAssetInfo = null
                        selectedProfileAssetName = null
                    }

                    return true
                }
            }
        }
        return false
    }

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "loadProfileMenu"
        const val label = "Load"
    }
}