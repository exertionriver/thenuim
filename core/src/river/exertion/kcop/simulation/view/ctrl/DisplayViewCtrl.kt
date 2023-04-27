package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.simulation.view.displayViewLayout.DVLayoutHandler
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

        MessageChannel.DISPLAY_MODE_BRIDGE.enableReceive(this)

        MessageChannel.SDC_BRIDGE.enableReceive(this)
        MessageChannel.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    var dvLayoutHandler = DVLayoutHandler(screenWidth, screenHeight)

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

    var currentText = ""
    var currentFontSize = FontSize.SMALL

    fun setMenuIdxByTag(tag : String) {
        currentMenuIdx = displayViewMenus.indexOf(displayViewMenus.firstOrNull { it.tag() == tag } ?: displayViewMenus[currentMenuIdx])
    }

    fun clearText() {
        currentText = ""
    }

    override fun buildCtrl() {
        this.add(
            Stack().apply {
                this.add(Table().apply {
                    this.add(backgroundColorImg()).grow()
                })
                this.add(dvLayoutHandler.run {
                    this.currentLayoutMode = this@DisplayViewCtrl.currentLayoutMode
                    this.currentText = this@DisplayViewCtrl.currentText
                    this.currentFontSize = this@DisplayViewCtrl.currentFontSize
                    this.buildLayout()
                })
                if (menuOpen) this.add(displayViewMenus[currentMenuIdx].menuLayout())
            }).size(this.tableWidth(), this.tableHeight())

        this.clip()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.SDC_BRIDGE.isType(msg.message) ) -> {
                    this.sdcHandler = MessageChannel.SDC_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannel.KCOP_SKIN_BRIDGE.isType(msg.message) ) -> {
                    this.kcopSkin = MessageChannel.KCOP_SKIN_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannel.DISPLAY_MODE_BRIDGE.isType(msg.message) ) -> {
                    this.currentLayoutMode = MessageChannel.DISPLAY_MODE_BRIDGE.receiveMessage(msg.extraInfo)

                    dvLayoutHandler.currentDvLayout?.clearImagePaneContent()
                    dvLayoutHandler.currentDvLayout?.clearTextPaneContent()
                    dvLayoutHandler.currentDvLayout?.clearAlphaPaneContent()

                    MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, "DisplayMode set to: ${if (currentLayoutMode) "Layout" else "Clear"}" ))

                    build()
                    return true
                }
                (MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.isType(msg.message) ) -> {
                    val displayViewTextMessage: DisplayViewTextMessage = MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.receiveMessage(msg.extraInfo)

                    if (dvLayoutHandler.currentDvLayout == null || dvLayoutHandler.currentDvLayout!!.name != displayViewTextMessage.layoutTag ) {
                        dvLayoutHandler.currentDvLayout = kcopSkin.layoutByName(displayViewTextMessage.layoutTag)
                    }

                    if (displayViewTextMessage.displayText != null) currentText = displayViewTextMessage.displayText
                    if (displayViewTextMessage.displayFontSize != null) currentFontSize = displayViewTextMessage.displayFontSize

                    build()
                    return true
                }

                (MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.isType(msg.message) ) -> {
                    val displayViewTextureMessage: DisplayViewTextureMessage = MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.receiveMessage(msg.extraInfo)

                    when (displayViewTextureMessage.messageType) {
                        DisplayViewTextureMessage.DisplayViewTextureMessageType.ShowImage -> dvLayoutHandler.setImagePaneContent(displayViewTextureMessage.layoutPaneIdx!!, displayViewTextureMessage.texture)
                        DisplayViewTextureMessage.DisplayViewTextureMessageType.HideImage -> dvLayoutHandler.setImagePaneContent(displayViewTextureMessage.layoutPaneIdx!!, null)
                        DisplayViewTextureMessage.DisplayViewTextureMessageType.FadeInImage -> dvLayoutHandler.fadeImageIn(displayViewTextureMessage.layoutPaneIdx!!, displayViewTextureMessage.texture)
                        DisplayViewTextureMessage.DisplayViewTextureMessageType.FadeOutImage -> dvLayoutHandler.fadeImageOut(displayViewTextureMessage.layoutPaneIdx!!, displayViewTextureMessage.texture)
                        DisplayViewTextureMessage.DisplayViewTextureMessageType.CrossFadeImage -> dvLayoutHandler.setImagePaneContent(displayViewTextureMessage.layoutPaneIdx!!, displayViewTextureMessage.texture)
                            //crossFadeImage(displayViewTextureMessage.layoutPaneIdx!!, displayViewTextureMessage.texture)
                        DisplayViewTextureMessage.DisplayViewTextureMessageType.ClearAll -> {
                                dvLayoutHandler.currentDvLayout?.clearImagePaneContent()
                                dvLayoutHandler.currentDvLayout?.clearTextPaneContent()
                                dvLayoutHandler.currentDvLayout?.clearAlphaPaneContent()
                                audioCtrl.stopMusic()
                            }
                        DisplayViewTextureMessage.DisplayViewTextureMessageType.Rebuild -> { } // rebuild the display
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
                        if (displayViewMenuMessage.menuButtonIdx == 2) {
                            dvLayoutHandler.currentDvLayout = kcopSkin.nextLayout(dvLayoutHandler.currentDvLayout!!.name)

                            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, "Layout set to: ${dvLayoutHandler.currentDvLayout!!.name}" ))
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

    override fun dispose() {
        dvLayoutHandler.dispose()
        displayViewMenus.forEach { it.dispose() }
        audioCtrl.dispose()
    }
}