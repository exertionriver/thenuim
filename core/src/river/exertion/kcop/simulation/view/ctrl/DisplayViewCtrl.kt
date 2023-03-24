package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.Timer
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.simulation.view.displayViewLayouts.DVLBasicPictureNarrative
import river.exertion.kcop.simulation.view.displayViewLayouts.DVLGoldenRatio
import river.exertion.kcop.simulation.view.displayViewLayouts.DisplayViewLayout
import river.exertion.kcop.simulation.view.displayViewMenus.*
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.*

class DisplayViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewCtrl(ViewType.DISPLAY, screenWidth, screenHeight) {

    val audioCtrl = AudioCtrl()

    init {
        MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.enableReceive(this)
        MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.enableReceive(this)
        MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.enableReceive(this)
        MessageChannel.DISPLAY_VIEW_CONFIG_BRIDGE.enableReceive(this)
        MessageChannel.MENU_BRIDGE.enableReceive(this)
    }

    var displayViewLayouts : MutableList<DisplayViewLayout> = mutableListOf(
        DVLGoldenRatio(screenWidth, screenHeight),
        DVLBasicPictureNarrative(screenWidth, screenHeight),
    )

    var displayViewMenus : MutableList<DisplayViewMenu> = mutableListOf(
        MainMenu(screenWidth, screenHeight),
        ProfileMenu(screenWidth, screenHeight),
        LoadProfileMenu(screenWidth, screenHeight),
        SaveProfileMenu(screenWidth, screenHeight)
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

    var currentMusic : Music? = null
    var currentSound : Music? = null

    //clears if texture is null
    fun showImage(layoutPaneIdx : Int, texture : Texture?) {
        displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] = 0f
        displayViewLayouts[currentLayoutIdx].paneTextures[layoutPaneIdx] = texture

        this.recreate()
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
                    this@DisplayViewCtrl.recreate()
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
                    this@DisplayViewCtrl.recreate()
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
        this.recreate()
    }

    override fun build(bitmapFont: BitmapFont, batch: Batch) {
        this.add(
            Stack().apply {
                this.add(displayViewLayouts[currentLayoutIdx].buildPaneTable(bitmapFont, batch, currentLayoutMode, currentText, currentFontSize))
                if (menuOpen) this.add(displayViewMenus[currentMenuIdx].menuLayout(batch, bitmapFont))
            }).size(this.tableWidth(), this.tableHeight())

        this.clip()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.DISPLAY_VIEW_CONFIG_BRIDGE.isType(msg.message) ) -> {
                    val viewMessage: ViewMessage = MessageChannel.DISPLAY_VIEW_CONFIG_BRIDGE.receiveMessage(msg.extraInfo)

                    setLayoutIdxByTag(viewMessage.messageContent)
                    currentLayoutMode = false

                    if (isInitialized) recreate()
                    return true
                }
                (MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.isType(msg.message) ) -> {
                    val textViewMessage: TextViewMessage = MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.receiveMessage(msg.extraInfo)

                    currentText = textViewMessage.displayText
                    currentFontSize = textViewMessage.displayFontSize


                    if (isInitialized) recreate()
                    return true
                }

                (MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.isType(msg.message) ) -> {
                    val displayViewTextureMessage: DisplayViewTextureMessage = MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.receiveMessage(msg.extraInfo)

                    when (displayViewTextureMessage.messageType) {
                        DisplayViewTextureMessageType.SHOW_IMAGE -> showImage(displayViewTextureMessage.layoutPaneIdx, displayViewTextureMessage.texture)
                        DisplayViewTextureMessageType.FADE_IMAGE_IN -> fadeImageIn(displayViewTextureMessage.layoutPaneIdx, displayViewTextureMessage.texture)
                        DisplayViewTextureMessageType.FADE_IMAGE_OUT -> fadeImageOut(displayViewTextureMessage.layoutPaneIdx, displayViewTextureMessage.texture)
                        DisplayViewTextureMessageType.CROSSFADE_IMAGE -> crossFadeImage(displayViewTextureMessage.layoutPaneIdx, displayViewTextureMessage.texture)
                        DisplayViewTextureMessageType.CLEAR_ALL -> clearImages()
                    }

                    if (isInitialized) recreate()
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

                    if (isInitialized) recreate()
                    return true
                }
                (MessageChannel.MENU_BRIDGE.isType(msg.message) ) -> {
                    val menuMessage: MenuMessage = MessageChannel.MENU_BRIDGE.receiveMessage(msg.extraInfo)

                        currentMenuIdx = displayViewMenus.indexOf(displayViewMenus.firstOrNull { it.tag() == menuMessage.menuParams!!.targetMenuTag } ?: 0)

                        if ( listOf(LoadProfileMenu.tag, SaveProfileMenu.tag).contains(menuMessage.menuParams!!.targetMenuTag) ) {
                            (displayViewMenus[currentMenuIdx] as LoadProfileMenu).profileAsset = (menuMessage.menuParams as ProfileMenuParams).profile
                        }

                    if (isInitialized) recreate()
                    return true
                }
            }
        }
        return false
    }

    override fun dispose() {
        displayViewLayouts.forEach { it.dispose() }
        displayViewMenus.forEach { it.dispose() }
        audioCtrl.dispose()
    }
}