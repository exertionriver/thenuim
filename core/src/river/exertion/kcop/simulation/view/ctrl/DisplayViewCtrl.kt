package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.simulation.view.ctrl.DisplayViewCtrlTextureHandler.clearImages
import river.exertion.kcop.simulation.view.ctrl.DisplayViewCtrlTextureHandler.crossFadeImage
import river.exertion.kcop.simulation.view.ctrl.DisplayViewCtrlTextureHandler.fadeImageIn
import river.exertion.kcop.simulation.view.ctrl.DisplayViewCtrlTextureHandler.fadeImageOut
import river.exertion.kcop.simulation.view.ctrl.DisplayViewCtrlTextureHandler.showImage
import river.exertion.kcop.simulation.view.displayViewLayouts.DVLBasicPictureNarrative
import river.exertion.kcop.simulation.view.displayViewLayouts.DVLGoldenRatio
import river.exertion.kcop.simulation.view.displayViewLayouts.DisplayViewLayout
import river.exertion.kcop.simulation.view.displayViewMenus.*
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.DisplayViewMenuMessage
import river.exertion.kcop.system.messaging.messages.DisplayViewTextMessage
import river.exertion.kcop.system.messaging.messages.DisplayViewTextureMessage
import river.exertion.kcop.system.messaging.messages.LogViewMessage

class DisplayViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewCtrl(ViewType.DISPLAY, screenWidth, screenHeight) {

    val audioCtrl = AudioCtrl()

    init {
        MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.enableReceive(this)
        MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.enableReceive(this)
        MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.enableReceive(this)

        MessageChannel.SDC_BRIDGE.enableReceive(this)
        MessageChannel.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    var displayViewLayouts : MutableList<DisplayViewLayout> = mutableListOf(
        DVLGoldenRatio(screenWidth, screenHeight),
        DVLBasicPictureNarrative(screenWidth, screenHeight),
    )

    var displayViewMenus : MutableList<DisplayViewMenu> = mutableListOf(
        MainMenu(screenWidth, screenHeight),
        ProfileMenu(screenWidth, screenHeight),
        LoadProfileMenu(screenWidth, screenHeight),
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

    fun clearText() {
        currentText = ""
    }

    override fun buildCtrl() {
        this.add(
            Stack().apply {
                this.add(displayViewLayouts[currentLayoutIdx].buildPaneTable(currentLayoutMode, currentText, currentFontSize))
                if (menuOpen) this.add(displayViewMenus[currentMenuIdx].menuLayout())
            }).size(this.tableWidth(), this.tableHeight())

        this.clip()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.SDC_BRIDGE.isType(msg.message) ) -> {
                    super.sdcHandler = MessageChannel.SDC_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannel.KCOP_SKIN_BRIDGE.isType(msg.message) ) -> {
                    super.kcopSkin = MessageChannel.KCOP_SKIN_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.isType(msg.message) ) -> {
                    val displayViewTextMessage: DisplayViewTextMessage = MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.receiveMessage(msg.extraInfo)

                    setLayoutIdxByTag(displayViewTextMessage.layoutTag)
                    displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha.clear()
                    if (displayViewTextMessage.displayText != null) currentText = displayViewTextMessage.displayText
                    if (displayViewTextMessage.displayFontSize != null) currentFontSize = displayViewTextMessage.displayFontSize

                    build()
                    return true
                }

                (MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.isType(msg.message) ) -> {
                    val displayViewTextureMessage: DisplayViewTextureMessage = MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.receiveMessage(msg.extraInfo)

                    when (displayViewTextureMessage.messageType) {
                        DisplayViewTextureMessage.DisplayViewTextureMessageType.ShowImage -> showImage(displayViewTextureMessage.layoutPaneIdx!!, displayViewTextureMessage.texture)
                        DisplayViewTextureMessage.DisplayViewTextureMessageType.HideImage -> showImage(displayViewTextureMessage.layoutPaneIdx!!, null)
                        DisplayViewTextureMessage.DisplayViewTextureMessageType.FadeInImage -> fadeImageIn(displayViewTextureMessage.layoutPaneIdx!!, displayViewTextureMessage.texture)
                        DisplayViewTextureMessage.DisplayViewTextureMessageType.FadeOutImage -> fadeImageOut(displayViewTextureMessage.layoutPaneIdx!!, displayViewTextureMessage.texture)
                        DisplayViewTextureMessage.DisplayViewTextureMessageType.CrossFadeImage -> crossFadeImage(displayViewTextureMessage.layoutPaneIdx!!, displayViewTextureMessage.texture)
                        DisplayViewTextureMessage.DisplayViewTextureMessageType.ClearAll -> { clearImages(); clearText(); audioCtrl.stopMusic() }
                        }

                    if (!menuOpen) build()
                    return true
                }
                (MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.isType(msg.message) ) -> {
                    val displayViewMenuMessage: DisplayViewMenuMessage = MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.receiveMessage(msg.extraInfo)

                    if (displayViewMenuMessage.menuButtonIdx != null) {
                        if (displayViewMenuMessage.menuButtonIdx == 0) {
                            menuOpen = (displayViewMenuMessage.isChecked)
                            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, "Menu ${if (menuOpen) "Opened" else "Closed"}" ))
                        }
                        if (displayViewMenuMessage.menuButtonIdx == 1) {
                            currentLayoutMode = (displayViewMenuMessage.isChecked)
                            displayViewLayouts[currentLayoutIdx].paneTextures.clear()
                            displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha.clear()
                            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, "DisplayMode set to: ${if (currentLayoutMode) "Background Box" else "Wireframe"}" ))
                        }
                        if (displayViewMenuMessage.menuButtonIdx == 2) {
                            currentLayoutIdx = if (currentLayoutIdx < displayViewLayouts.size - 1) currentLayoutIdx + 1 else 0
                            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, "Layout set to: ${displayViewLayouts[currentLayoutIdx].tag}" ))
                        }
                    }

                    if (displayViewMenuMessage.targetMenuTag != null) {
                        setMenuIdxByTag(displayViewMenuMessage.targetMenuTag)
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