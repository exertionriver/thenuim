package river.exertion.kcop.view.layout

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.FontSize
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.KcopSkin.Companion.KcopSkinBridge
import river.exertion.kcop.view.SdcHandler
import river.exertion.kcop.view.SdcHandler.Companion.SDCBridge
import river.exertion.kcop.view.messaging.DisplayModeMessage.Companion.DisplayModeBridge
import river.exertion.kcop.view.messaging.DisplayViewTextMessage
import river.exertion.kcop.view.messaging.DisplayViewTextMessage.Companion.DisplayViewTextBridge
import river.exertion.kcop.view.messaging.DisplayViewTextureMessage
import river.exertion.kcop.view.messaging.DisplayViewTextureMessage.Companion.DisplayViewTextureBridge
import river.exertion.kcop.view.messaging.LogViewMessage
import river.exertion.kcop.view.messaging.LogViewMessage.Companion.LogViewBridge
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.MenuViewMessage.Companion.MenuViewBridge

class DisplayView(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewBase(ViewType.DISPLAY, screenWidth, screenHeight) {

    val audioView = AudioView()

    init {
        MessageChannelHandler.enableReceive(DisplayViewTextureBridge, this)
        MessageChannelHandler.enableReceive(DisplayViewTextBridge, this)
        MessageChannelHandler.enableReceive(MenuViewBridge, this)

        MessageChannelHandler.enableReceive(DisplayModeBridge, this)
        MessageChannelHandler.enableReceive(SDCBridge, this)
        MessageChannelHandler.enableReceive(KcopSkinBridge, this)
    }
/*
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
*/
    var menuOpen = false
    var currentMenuIdx = 0

    var currentText = ""
    var currentFontSize = FontSize.SMALL
/*
    fun setMenuIdxByTag(tag : String) {
        currentMenuIdx = displayViewMenus.indexOf(displayViewMenus.firstOrNull { it.tag() == tag } ?: displayViewMenus[currentMenuIdx])
    }
*/
    fun clearText() {
        currentText = ""
    }

    override fun buildCtrl() {
        this.add(
            Stack().apply {
                this.add(Table().apply {
                    this.add(backgroundColorImg()).grow()
                })
  /*              this.add(dvLayoutHandler.run {
                    this.currentLayoutMode = this@DisplayViewCtrl.currentLayoutMode
                    this.currentText = this@DisplayViewCtrl.currentText
                    this.currentFontSize = this@DisplayViewCtrl.currentFontSize
                    this.buildLayout()
               })
                if (menuOpen) this.add(displayViewMenus[currentMenuIdx].menuLayout())
*/            }).size(this.tableWidth(), this.tableHeight())

        this.clip()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(SDCBridge, msg.message) ) -> {
                    super.sdcHandler = MessageChannelHandler.receiveMessage(SDCBridge, msg.extraInfo)
                    return true
                }
                (MessageChannelHandler.isType(KcopSkinBridge, msg.message) ) -> {
                    super.kcopSkin = MessageChannelHandler.receiveMessage(KcopSkinBridge, msg.extraInfo)
                    return true
                }
                (MessageChannelHandler.isType(DisplayModeBridge, msg.message) ) -> {
                    this.currentLayoutMode = MessageChannelHandler.receiveMessage(DisplayModeBridge, msg.extraInfo)
/*
                    dvLayoutHandler.currentDvLayout.clearImagePaneContent()
                    dvLayoutHandler.currentDvLayout.clearTextPaneContent()
                    dvLayoutHandler.currentDvLayout.clearAlphaPaneContent()

                    MessageChannelEnum.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, "DisplayMode set to: ${if (currentLayoutMode) "Layout" else "Clear"}" ))
*/

                    build()
                    return true
                }
                (MessageChannelHandler.isType(DisplayViewTextBridge, msg.message) ) -> {
                    val displayViewTextMessage: DisplayViewTextMessage = MessageChannelHandler.receiveMessage(DisplayViewTextBridge, msg.extraInfo)
/*
                    if (dvLayoutHandler.currentDvLayout.name != displayViewTextMessage.layoutTag ) {
                        dvLayoutHandler.currentDvLayout = kcopSkin.layoutByName(displayViewTextMessage.layoutTag)
                    }
*/
                    if (displayViewTextMessage.displayText != null) currentText = displayViewTextMessage.displayText
                    if (displayViewTextMessage.displayFontSize != null) currentFontSize = displayViewTextMessage.displayFontSize

                    build()
                    return true
                }

                (MessageChannelHandler.isType(DisplayViewTextureBridge, msg.message) ) -> {
                    val displayViewTextureMessage: DisplayViewTextureMessage = MessageChannelHandler.receiveMessage(
                        DisplayViewTextureBridge, msg.extraInfo)

/*                    when (displayViewTextureMessage.messageType) {
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
                                audioView.stopMusic()
                            }
                        DisplayViewTextureMessage.DisplayViewTextureMessageType.Rebuild -> { } // rebuild the display
                    }
*/
                    if (!menuOpen) build()

                    return true
                }
                (MessageChannelHandler.isType(MenuViewBridge, msg.message) ) -> {
                    val menuViewMessage: MenuViewMessage = MessageChannelHandler.receiveMessage(MenuViewBridge, msg.extraInfo)

                    if (menuViewMessage.menuButtonIdx != null) {
                        if (menuViewMessage.menuButtonIdx == 0) {
                            menuOpen = (menuViewMessage.isChecked)
                            MessageChannelHandler.send(LogViewBridge, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, "Menu ${if (menuOpen) "Opened" else "Closed"}" ))
                        }
                        if (menuViewMessage.menuButtonIdx == 2) {
//                            dvLayoutHandler.currentDvLayout = kcopSkin.nextLayout(dvLayoutHandler.currentDvLayout!!.name)

//                            MessageChannelHandler.send(LogViewBridge, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, "Layout set to: ${dvLayoutHandler.currentDvLayout!!.name}" ))
                        }
                    }

//                    if (displayViewMenuMessage.targetMenuTag != null) {
//                        setMenuIdxByTag(displayViewMenuMessage.targetMenuTag)
 ///                   }

                    build()
                    return true
                }
            }
        }
        return false
    }

    override fun dispose() {
//        dvLayoutHandler.dispose()
//        displayViewMenus.forEach { it.dispose() }
        audioView.dispose()
    }

    companion object {
        var currentSim = Table()
    }

}