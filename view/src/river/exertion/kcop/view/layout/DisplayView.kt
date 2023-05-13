package river.exertion.kcop.view.layout

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.ViewPackage.DisplayViewBridge
import river.exertion.kcop.view.ViewPackage.DisplayViewTextBridge
import river.exertion.kcop.view.ViewPackage.DisplayViewTextureBridge
import river.exertion.kcop.view.ViewPackage.LogViewBridge
import river.exertion.kcop.view.ViewPackage.MenuViewBridge
import river.exertion.kcop.view.asset.FontSize
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.DisplayViewMenuHandler.currentMenuTag
import river.exertion.kcop.view.messaging.*

class DisplayView : Telegraph, ViewBase(ViewType.DISPLAY) {

    val audioView = AudioView()

    init {
        MessageChannelHandler.enableReceive(DisplayViewBridge, this)
        MessageChannelHandler.enableReceive(DisplayViewTextureBridge, this)
        MessageChannelHandler.enableReceive(DisplayViewTextBridge, this)
        MessageChannelHandler.enableReceive(MenuViewBridge, this)
    }

    var menuOpen = false

    var currentText = ""
    var currentFontSize = FontSize.SMALL

    fun clearText() {
        currentText = ""
    }

    override fun buildCtrl() {
        this.add(
            Stack().apply {
                this.add(Table().apply {
                    this.add(backgroundColorImg()).grow()
                })
                if (currentDisplayView != null) {
                    this.add(Table().apply {
                        this.add(currentDisplayView).grow()
                    })
                }
                if (menuOpen) {
                    this.add(Table().apply {
                        this.add(DisplayViewMenuHandler.buildByTag(currentMenuTag)).grow()
                    })
                }
           }).size(this.tableWidth(), this.tableHeight())

        this.clip()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(DisplayViewBridge, msg.message) ) -> {
                    val displayViewMessage: DisplayViewMessage = MessageChannelHandler.receiveMessage(DisplayViewBridge, msg.extraInfo)

                    when (displayViewMessage.messageType) {
                        DisplayViewMessage.DisplayViewMessageType.Rebuild -> build()
                    }

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

    companion object {
        var currentDisplayView : Actor? = null
    }

}