package river.exertion.kcop.view.layout

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.*
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.ColorPalette
import river.exertion.kcop.view.FontSize
import river.exertion.kcop.view.ViewPackage.DisplayModeBridge
import river.exertion.kcop.view.ViewPackage.KcopSkinBridge
import river.exertion.kcop.view.ViewPackage.SDCBridge
import river.exertion.kcop.view.ViewPackage.StatusViewBridge
import river.exertion.kcop.view.messaging.StatusViewMessage
import kotlin.math.roundToInt


class StatusView(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewBase(ViewType.STATUS, screenWidth, screenHeight) {

    init {
        MessageChannelHandler.enableReceive(StatusViewBridge, this)
        MessageChannelHandler.enableReceive(DisplayModeBridge, this)

        MessageChannelHandler.enableReceive(SDCBridge,this)
        MessageChannelHandler.enableReceive(KcopSkinBridge, this)
    }

    val displayStatuses : MutableMap<String, Float> = mutableMapOf()

    private lateinit var scrollPane : ScrollPane

    fun statusColorTexture(overrideColor : ColorPalette? = null) : TextureRegion {
        return sdcHandler.get("statusColor", overrideColor ?: backgroundColor).textureRegion().apply {
            this.setRegion(0, 0, ViewType.padWidth(screenWidth).roundToInt(), ViewType.padHeight(screenHeight).roundToInt())
        }
    }

    fun textScrollPane() : ScrollPane {

        val innerTable = Table().padLeft(ViewType.padWidth(width)).padRight(ViewType.padWidth(width)).padTop(
            ViewType.padHeight(
                height
            )
        ).padBottom(
            ViewType.padHeight(height)
        )

        displayStatuses.forEach {
            val barStack = Stack()

            barStack.add(
                ProgressBar(0f, 1f, .01f, false, skin()).apply { this.value = it.value } )
            barStack.add(
                Label(it.key, kcopSkin.labelStyle(FontSize.TEXT, backgroundColor.incr(2)))
            )

            innerTable.add(barStack)
            innerTable.row()
        }

        innerTable.top()
//        innerTable.debug()

            val scrollPane = ScrollPane(innerTable, skin()).apply {
            // https://github.com/raeleus/skin-composer/wiki/ScrollPane
            this.fadeScrollBars = false
            this.setFlickScroll(false)
            this.validate()
            //https://gamedev.stackexchange.com/questions/96096/libgdx-scrollpane-wont-scroll-to-bottom-after-adding-children-to-contained-tab
            this.layout()
        }

        this.scrollPane = scrollPane

        return scrollPane
    }

    override fun buildCtrl() {
        this.add(Stack().apply {
            this.add(backgroundColorImg())
            this.add(textScrollPane())
        } ).size(this.tableWidth(), this.tableHeight())

        this.clip()
    }

    @Suppress("NewApi")
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
                    build()
                    return true
                }
                (MessageChannelHandler.isType(StatusViewBridge, msg.message) ) -> {
                    val statusViewMessage: StatusViewMessage = MessageChannelHandler.receiveMessage(StatusViewBridge, msg.extraInfo)

                    when (statusViewMessage.messageType) {
                        StatusViewMessage.StatusViewMessageType.AddStatus -> {
                            if (statusViewMessage.statusKey != null) {
                                displayStatuses[statusViewMessage.statusKey] = statusViewMessage.statusValue ?: 0f
                            }
                        }
                        StatusViewMessage.StatusViewMessageType.ClearStatuses -> {
                            displayStatuses.clear()
                        }
                        StatusViewMessage.StatusViewMessageType.RemoveStatus -> {
                            if (statusViewMessage.statusKey != null) {
                                displayStatuses.remove(statusViewMessage.statusKey)
                            }
                        }
                        StatusViewMessage.StatusViewMessageType.UpdateStatus -> {
                            if (statusViewMessage.statusKey != null && displayStatuses.containsKey(statusViewMessage.statusKey)) {
                                displayStatuses[statusViewMessage.statusKey] = statusViewMessage.statusValue ?: 0f
                            }
                        }
                    }

                    build()
                    return true
                }
            }
        }
        return false
    }
}