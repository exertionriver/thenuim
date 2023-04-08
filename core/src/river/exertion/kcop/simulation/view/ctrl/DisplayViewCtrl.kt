package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.Timer
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.simulation.view.displayViewLayouts.DVLBasicPictureNarrative
import river.exertion.kcop.simulation.view.displayViewLayouts.DVLGoldenRatio
import river.exertion.kcop.simulation.view.displayViewLayouts.DisplayViewLayout
import river.exertion.kcop.simulation.view.displayViewMenus.*
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.*

class DisplayViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewCtrl(ViewType.DISPLAY, screenWidth, screenHeight) {

    val audioCtrl = AudioCtrl()

    init {
        MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.enableReceive(this)
        MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.enableReceive(this)
        MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.enableReceive(this)
        MessageChannel.INTRA_MENU_BRIDGE.enableReceive(this)
        MessageChannel.INTER_MENU_BRIDGE.enableReceive(this)
        MessageChannel.TWO_BATCH_BRIDGE.enableReceive(this)
        MessageChannel.FONT_BRIDGE.enableReceive(this)
    }

    var displayViewLayouts : MutableList<DisplayViewLayout> = mutableListOf(
        DVLGoldenRatio(screenWidth, screenHeight),
        DVLBasicPictureNarrative(screenWidth, screenHeight),
    )

    var displayViewMenus : MutableList<DisplayViewMenu> = mutableListOf(
        MainMenu(screenWidth, screenHeight),
        ProfileMenu(screenWidth, screenHeight),
        LoadProfileMenu(screenWidth, screenHeight),
        SaveProfileMenu(screenWidth, screenHeight),
        NarrativeMenu(screenWidth, screenHeight),
        LoadNarrativeMenu(screenWidth, screenHeight),
        NewProfileMenu(screenWidth, screenHeight),
        SaveProgressMenu(screenWidth, screenHeight),
        RestartProgressMenu(screenWidth, screenHeight),
        ProfileSettingsMenu(screenWidth, screenHeight),
    )

    var menuOpen = false
    var currentMenuIdx = 0
    var currentLayoutIdx = 0

    var currentLayoutMode = false
    var currentText = ""
    var currentFontSize = FontSize.SMALL

    fun setLayoutIdxByTag(tag : String) {
        currentLayoutIdx = displayViewLayouts.indexOf(displayViewLayouts.firstOrNull { it.tag == tag } ?: displayViewLayouts[currentLayoutIdx])
    }

    fun setMenuIdxByTag(tag : String) {
        currentMenuIdx = displayViewMenus.indexOf(displayViewMenus.firstOrNull { it.tag() == tag } ?: displayViewMenus[currentMenuIdx])
    }

    //clears if texture is null
    fun showImage(layoutPaneIdx : Int, texture : Texture?) {

        if (texture != null) {
            displayViewLayouts[currentLayoutIdx].paneTextures[layoutPaneIdx] = texture
            displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] = 0f
        } else {
            displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] = 1f
        }
    }

    fun fadeImageIn(layoutPaneIdx : Int, texture : Texture?) {
        if (displayViewLayouts[currentLayoutIdx].paneTextures[layoutPaneIdx] != texture && displayViewLayouts[currentLayoutIdx].paneImageFading[layoutPaneIdx] != true) {
            displayViewLayouts[currentLayoutIdx].paneTextures[layoutPaneIdx] = texture
            displayViewLayouts[currentLayoutIdx].paneImageFading[layoutPaneIdx] = true
            displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] = 1f
            Timer.schedule(object : Timer.Task() {
                override fun run() {
                    if (displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx]!! >= .1f)
                        displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] =
                            displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx]?.minus(.1f)
                    else {
                        displayViewLayouts[currentLayoutIdx].paneImageFading[layoutPaneIdx] = false
                        this.cancel()
                    }
                    this@DisplayViewCtrl.build()
                }
            }, 0f, .05f)
        }
    }

    fun fadeImageOut(layoutPaneIdx : Int, texture : Texture?) {
        if (displayViewLayouts[currentLayoutIdx].paneTextures[layoutPaneIdx] != texture && displayViewLayouts[currentLayoutIdx].paneImageFading[layoutPaneIdx] != true) {
            displayViewLayouts[currentLayoutIdx].paneImageFading[layoutPaneIdx] = true
            displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] = 0f
            Timer.schedule(object : Timer.Task() {
                override fun run() {
                    if (displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx]!! <= .9f)
                        displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] =
                            displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx]?.plus(.1f)
                    else {
                        displayViewLayouts[currentLayoutIdx].paneTextures[layoutPaneIdx] = texture
                        displayViewLayouts[currentLayoutIdx].paneImageFading[layoutPaneIdx] = false
                        this.cancel()
                    }
                    this@DisplayViewCtrl.build()
                }
            }, 0f, .05f)
        }
    }

    //TODO: look into fade
    fun crossFadeImage(layoutPaneIdx : Int, texture : Texture?) {
            showImage(layoutPaneIdx, texture)
    }

    fun clearImages() {
        displayViewLayouts[currentLayoutIdx].paneTextures.clear()
        displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha.entries.forEach { it.setValue(1f) }
    }

    fun clearText() {
        currentText = ""
    }


    override fun buildCtrl() {
        this.add(
            Stack().apply {
                this.add(displayViewLayouts[currentLayoutIdx].buildPaneTable(fontPackage.font(FontSize.SMALL), batch, currentLayoutMode, currentText, currentFontSize))
                if (menuOpen) this.add(displayViewMenus[currentMenuIdx].menuLayout(batch, fontPackage.font(FontSize.SMALL)))
            }).size(this.tableWidth(), this.tableHeight())

        this.clip()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.TWO_BATCH_BRIDGE.isType(msg.message) ) -> {
                    val twoBatch: PolygonSpriteBatch = MessageChannel.TWO_BATCH_BRIDGE.receiveMessage(msg.extraInfo)
                    super.batch = twoBatch
                    return true
                }
                (MessageChannel.FONT_BRIDGE.isType(msg.message) ) -> {
                    val fontPackage: FontPackage = MessageChannel.FONT_BRIDGE.receiveMessage(msg.extraInfo)
                    super.fontPackage = fontPackage
                    return true
                }
                (MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.isType(msg.message) ) -> {
                    val displayViewTextMessage: DisplayViewTextMessage = MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.receiveMessage(msg.extraInfo)

                    setLayoutIdxByTag(displayViewTextMessage.layoutTag)
                    if (displayViewTextMessage.displayText != null) currentText = displayViewTextMessage.displayText
                    if (displayViewTextMessage.displayFontSize != null) currentFontSize = displayViewTextMessage.displayFontSize

                    build()
                    return true
                }

                (MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.isType(msg.message) ) -> {
                    val displayViewTextureMessage: DisplayViewTextureMessage = MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.receiveMessage(msg.extraInfo)

                    when (displayViewTextureMessage.messageType) {
                        DisplayViewTextureMessageType.SHOW_IMAGE -> showImage(displayViewTextureMessage.layoutPaneIdx, displayViewTextureMessage.texture)
                        DisplayViewTextureMessageType.FADE_IMAGE_IN -> fadeImageIn(displayViewTextureMessage.layoutPaneIdx, displayViewTextureMessage.texture)
                        DisplayViewTextureMessageType.FADE_IMAGE_OUT -> fadeImageOut(displayViewTextureMessage.layoutPaneIdx, displayViewTextureMessage.texture)
                        DisplayViewTextureMessageType.CROSSFADE_IMAGE -> crossFadeImage(displayViewTextureMessage.layoutPaneIdx, displayViewTextureMessage.texture)
                        DisplayViewTextureMessageType.CLEAR_ALL -> { clearImages(); clearText(); audioCtrl.stopMusic() }
                        }

                    if (!menuOpen) build()
                    return true
                }
                (MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.isType(msg.message) ) -> {
                    val displayViewMenuMessage: DisplayViewMenuMessage = MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.receiveMessage(msg.extraInfo)

                    if (displayViewMenuMessage.menuButtonIdx == 0) {
                        menuOpen = (displayViewMenuMessage.isChecked)
                        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "Menu ${if (menuOpen) "Opened" else "Closed"}" ))
                    }
                    if (displayViewMenuMessage.menuButtonIdx == 1) {
                        currentLayoutMode = (displayViewMenuMessage.isChecked)
                        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "DisplayMode set to: ${if (currentLayoutMode) "Background Box" else "Wireframe"}" ))
                    }
                    if (displayViewMenuMessage.menuButtonIdx == 2) {
                        currentLayoutIdx = if (currentLayoutIdx < displayViewLayouts.size - 1) currentLayoutIdx + 1 else 0
                        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "Layout set to: ${displayViewLayouts[currentLayoutIdx].tag}" ))
                    }
                    if (displayViewMenuMessage.menuButtonIdx == 3) {
                        currentMenuIdx = if (currentMenuIdx < displayViewMenus.size - 1) currentMenuIdx + 1 else 0
                        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "Menu set to: ${displayViewMenus[currentMenuIdx].tag()}" ))
                    }

                    build()
                    return true
                }
                (MessageChannel.INTER_MENU_BRIDGE.isType(msg.message) ) -> {
                    val menuDataMessage: MenuDataMessage = MessageChannel.INTER_MENU_BRIDGE.receiveMessage(msg.extraInfo)

                    //to menus from elsewhere
                    if (menuDataMessage.profileMenuDataParams != null && menuDataMessage.narrativeMenuDataParams != null) {
                        displayViewMenus.filter { it is SaveProgressMenu }.forEach { saveProgressMenu ->

                            val assetsInfo = (menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo ?: listOf("")) + (menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetInfo ?: listOf(""))
                            if (menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo != null) {
                                (saveProgressMenu as SaveProgressMenu).progressAssetsInfo = assetsInfo
                            }
                        }
                    } else {
                        if ( menuDataMessage.profileMenuDataParams != null ) {
                            displayViewMenus.filter { it is ProfileMenu }.forEach { profileMenu ->
                                if (menuDataMessage.profileMenuDataParams!!.profileAssetTitles != null) {
                                    (profileMenu as ProfileMenu).profileAssetTitles = menuDataMessage.profileMenuDataParams!!.profileAssetTitles
                                }
                                if (menuDataMessage.profileMenuDataParams!!.selectedProfileAssetTitle != null) {
                                    (profileMenu as ProfileMenu).selectedProfileAssetTitle = menuDataMessage.profileMenuDataParams!!.selectedProfileAssetTitle
                                }
                            }
                            displayViewMenus.filter { it is LoadProfileMenu }.forEach { loadProfileMenu ->
                                if (menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo != null) {
                                    (loadProfileMenu as LoadProfileMenu).selectedProfileAssetInfo = menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo
                                }
                                if (menuDataMessage.profileMenuDataParams!!.selectedProfileAssetName != null) {
                                    (loadProfileMenu as LoadProfileMenu).selectedProfileAssetName = menuDataMessage.profileMenuDataParams!!.selectedProfileAssetName
                                }
                            }
                            displayViewMenus.filter { it is SaveProfileMenu }.forEach { saveProfileMenu ->
                                if (menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo != null) {
                                    (saveProfileMenu as SaveProfileMenu).selectedProfileAssetInfo = menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo
                                }
                                if (menuDataMessage.profileMenuDataParams!!.selectedProfileAssetName != null) {
                                    (saveProfileMenu as SaveProfileMenu).selectedProfileAssetName = menuDataMessage.profileMenuDataParams!!.selectedProfileAssetName
                                }
                            }
                            displayViewMenus.filter { it is ProfileSettingsMenu }.forEach { profileSettingsMenu ->
                                if (menuDataMessage.profileMenuDataParams!!.profileAssetTitles != null && menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo != null) {
                                    (profileSettingsMenu as ProfileSettingsMenu).profileSettings = menuDataMessage.profileMenuDataParams!!.profileAssetTitles!!.zip(menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo!!).toMap().toMutableMap()
                                }
                            }
                        } else {
                            displayViewMenus.filter { it is ProfileMenu }.forEach { profileMenu ->
                                (profileMenu as ProfileMenu).profileAssetTitles = null
                                profileMenu.selectedProfileAssetTitle = null
                            }
                            displayViewMenus.filter { it is LoadProfileMenu }.forEach { loadProfileMenu ->
                                (loadProfileMenu as LoadProfileMenu).selectedProfileAssetInfo = null
                                loadProfileMenu.selectedProfileAssetInfo = null
                                loadProfileMenu.selectedProfileAssetName = null
                            }
                            displayViewMenus.filter { it is SaveProfileMenu }.forEach { saveProfileMenu ->
                                (saveProfileMenu as SaveProfileMenu).selectedProfileAssetInfo = null
                                saveProfileMenu.selectedProfileAssetInfo = null
                                saveProfileMenu.selectedProfileAssetName = null

                            }
                        }

                        if ( menuDataMessage.narrativeMenuDataParams != null ) {
                            displayViewMenus.filter { it is NarrativeMenu }.forEach { narrativeMenu ->
                                if (menuDataMessage.narrativeMenuDataParams!!.narrativeAssetTitles != null) {
                                    (narrativeMenu as NarrativeMenu).narrativeAssetTitles = menuDataMessage.narrativeMenuDataParams!!.narrativeAssetTitles
                                }
                                if (menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetTitle != null) {
                                    (narrativeMenu as NarrativeMenu).selectedNarrativeAssetTitle = menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetTitle
                                }
                            }
                            displayViewMenus.filter { it is LoadNarrativeMenu }.forEach { loadNarrativeMenu ->
                                if (menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetInfo != null) {
                                    (loadNarrativeMenu as LoadNarrativeMenu).selectedNarrativeAssetInfo = menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetInfo
                                }
                                if (menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetName != null) {
                                    (loadNarrativeMenu as LoadNarrativeMenu).selectedNarrativeAssetName = menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetName
                                }
                            }
                            displayViewMenus.filter { it is RestartProgressMenu }.forEach { restartProgressMenu ->

                                if (menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetInfo != null) {
                                    (restartProgressMenu as RestartProgressMenu).progressAssetsInfo = menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetInfo
                                }
                            }
                        } else {
                            displayViewMenus.filter { it is NarrativeMenu }.forEach { narrativeMenu ->
                                (narrativeMenu as NarrativeMenu).narrativeAssetTitles = null
                                narrativeMenu.selectedNarrativeAssetTitle = null
                            }
                            displayViewMenus.filter { it is LoadNarrativeMenu }.forEach { loadNarrativeMenu ->
                                (loadNarrativeMenu as LoadNarrativeMenu).selectedNarrativeAssetInfo = null
                                loadNarrativeMenu.selectedNarrativeAssetName = null
                            }
                        }
                    }

                    build()
                    return true
                }
                (MessageChannel.INTRA_MENU_BRIDGE.isType(msg.message) ) -> {
                    val menuNavMessage: MenuNavMessage = MessageChannel.INTRA_MENU_BRIDGE.receiveMessage(msg.extraInfo)

                    //between menus
                    if (menuNavMessage.menuNavParams != null) {
                        setMenuIdxByTag(menuNavMessage.menuNavParams!!.targetMenuTag)

                        displayViewMenus.filter { it is ProfileMenu }.forEach { profileMenu ->
                            (profileMenu as ProfileMenu).selectedProfileAssetTitle = menuNavMessage.menuNavParams!!.selectedAssetTitle
                        }
                        displayViewMenus.filter { it is NarrativeMenu }.forEach { narrativeMenu ->
                            (narrativeMenu as NarrativeMenu).selectedNarrativeAssetTitle = menuNavMessage.menuNavParams!!.selectedAssetTitle
                        }
                    } else {
                        displayViewMenus.filter { it is ProfileMenu }.forEach { profileMenu ->
                            (profileMenu as ProfileMenu).selectedProfileAssetTitle = null
                        }
                        displayViewMenus.filter { it is NarrativeMenu }.forEach { narrativeMenu ->
                            (narrativeMenu as NarrativeMenu).selectedNarrativeAssetTitle = null
                        }
                    }

                    build()
                    return true
                }
            }
        }
        return false
    }

    companion object {
        const val defaultLayoutTag = DVLGoldenRatio.tag
    }

    override fun dispose() {
        displayViewLayouts.forEach { it.dispose() }
        displayViewMenus.forEach { it.dispose() }
        audioCtrl.dispose()
    }
}