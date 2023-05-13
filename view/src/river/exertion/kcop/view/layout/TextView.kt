package river.exertion.kcop.view.layout

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewPackage.TextViewBridge
import river.exertion.kcop.view.asset.FontSize
import river.exertion.kcop.view.messaging.TextViewMessage

class TextView : Telegraph, ViewBase(ViewType.TEXT) {

    init {
        MessageChannelHandler.enableReceive(TextViewBridge, this)
    }

    var currentText : String = "noLoad"
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

        val textLabel = Label(currentText, KcopSkin.labelStyle(FontSize.TEXT, backgroundColor))
        val hintLabel = Label(currentHintText, KcopSkin.labelStyle(FontSize.TEXT, backgroundColor.triad().first))

        textLabel.wrap = true
        innerTable.add(textLabel).growX()

        if (!currentHintText.isBlank()) {
            innerTable.row()
            innerTable.add(hintLabel).growX()
        }

        innerTable.top()
//        innerTable.debug()

        val scrollPane = ScrollPane(innerTable, KcopSkin.skin).apply {
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
                val logLabel = Label(entry, KcopSkin.labelStyle(FontSize.TEXT, backgroundColor))
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
                (MessageChannelHandler.isType(TextViewBridge, msg.message) ) -> {
                    val textViewMessage: TextViewMessage = MessageChannelHandler.receiveMessage(TextViewBridge, msg.extraInfo)

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