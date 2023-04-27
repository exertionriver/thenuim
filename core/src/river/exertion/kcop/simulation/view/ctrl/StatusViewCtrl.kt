package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.*
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.StatusViewMessage
import kotlin.math.roundToInt


class StatusViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewCtrl(ViewType.STATUS, screenWidth, screenHeight) {

    init {
        MessageChannel.STATUS_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.DISPLAY_MODE_BRIDGE.enableReceive(this)

        MessageChannel.SDC_BRIDGE.enableReceive(this)
        MessageChannel.KCOP_SKIN_BRIDGE.enableReceive(this)
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
                (MessageChannel.SDC_BRIDGE.isType(msg.message) ) -> {
                    super.sdcHandler = MessageChannel.SDC_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannel.KCOP_SKIN_BRIDGE.isType(msg.message) ) -> {
                    super.kcopSkin = MessageChannel.KCOP_SKIN_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannel.DISPLAY_MODE_BRIDGE.isType(msg.message) ) -> {
                    this.currentLayoutMode = MessageChannel.DISPLAY_MODE_BRIDGE.receiveMessage(msg.extraInfo)
                    build()
                    return true
                }
                (MessageChannel.STATUS_VIEW_BRIDGE.isType(msg.message) ) -> {
                    val statusViewMessage: StatusViewMessage = MessageChannel.STATUS_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

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