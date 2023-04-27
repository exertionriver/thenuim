package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.assets.AssetManagerHandler
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.TextViewMessage


class TextViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewCtrl(ViewType.TEXT, screenWidth, screenHeight) {

    init {
        MessageChannel.TEXT_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.DISPLAY_MODE_BRIDGE.enableReceive(this)

        MessageChannel.SDC_BRIDGE.enableReceive(this)
        MessageChannel.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    var currentText : String = AssetManagerHandler.NoNarrativeLoaded
    var currentHintText : String = ""
    var currentPrompts : List<String>? = null

    private lateinit var scrollPane : ScrollPane

    fun isPrompts() = !currentPrompts.isNullOrEmpty()

    fun textScrollPane() : ScrollPane {

        val innerTable = Table().padLeft(ViewType.padWidth(width)).padRight(ViewType.padWidth(width)).padTop(
            ViewType.padHeight(
                height
            )
        ).padBottom(
            ViewType.padHeight(height)
        )

        val textLabel = Label(currentText, kcopSkin.labelStyle(FontSize.TEXT, backgroundColor))
        val hintLabel = Label(currentHintText, kcopSkin.labelStyle(FontSize.TEXT, backgroundColor.triad().first))

        textLabel.wrap = true
        innerTable.add(textLabel).growX()

        if (!currentHintText.isBlank()) {
            innerTable.row()
            innerTable.add(hintLabel).growX()
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

    fun promptPane() : Table {

        val innerTable = Table().padLeft(ViewType.padWidth(width)).padRight(ViewType.padWidth(width)).padTop(
            ViewType.padHeight(
                height
            )
        ).padBottom(
            ViewType.padHeight(height)
        )

        if (isPrompts()) {
            currentPrompts!!.forEach { entry ->
                val logLabel = Label(entry, kcopSkin.labelStyle(FontSize.TEXT, backgroundColor))
                logLabel.wrap = true
                innerTable.add(logLabel).grow()
                innerTable.row()
            }
        }

        innerTable.top()
//        innerTable.debug()

        return innerTable
    }

    override fun buildCtrl() {

        this.add(Stack().apply {
            this.add(backgroundColorImg())
            this.add(Table().apply {
                this.add(textScrollPane()).grow()
                this.row()
                this.add(promptPane()).growX()
            })
        }).size(this.tableWidth(), this.tableHeight())
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
                (MessageChannel.TEXT_VIEW_BRIDGE.isType(msg.message) ) -> {
                    val textViewMessage: TextViewMessage = MessageChannel.TEXT_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

                    when (textViewMessage.textViewMessageType) {
                        TextViewMessage.TextViewMessageType.ReportText ->
                            if (textViewMessage.narrativeText != null && textViewMessage.prompts != null) {
                                currentText = textViewMessage.narrativeText
                                currentPrompts = textViewMessage.prompts
                            } else {
                                currentText = ""
                                currentPrompts = null
                            }
                        TextViewMessage.TextViewMessageType.HintText ->
                            currentHintText = textViewMessage.narrativeText ?: ""
                    }

                    build()
                    return true
                }
            }
        }
        return false
    }
}