package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.TextViewMessage


class TextViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewCtrl(ViewType.TEXT, screenWidth, screenHeight) {

    init {
        MessageChannel.TEXT_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.TWO_BATCH_BRIDGE.enableReceive(this)
        MessageChannel.FONT_BRIDGE.enableReceive(this)
    }

    var currentText : String? = null
    var currentPrompts : List<String>? = null

    var vScrollKnobTexture : Texture? = null

    private lateinit var scrollPane : ScrollPane

    fun isText() = !currentText.isNullOrEmpty()
    fun isPrompts() = !currentPrompts.isNullOrEmpty()

    fun textScrollPane() : ScrollPane {

        val innerTable = Table().padLeft(ViewType.padWidth(width)).padRight(ViewType.padWidth(width)).padTop(
            ViewType.padHeight(
                height
            )
        ).padBottom(
            ViewType.padHeight(height)
        )

        if (isText()) {
            val textLabel = Label(currentText, Label.LabelStyle(fontPackage.font(FontSize.TEXT), backgroundColor.label().color()))
            textLabel.wrap = true
            innerTable.add(textLabel).growX()
        }

        innerTable.top()
//        innerTable.debug()

        val scrollNine = NinePatch(TextureRegion(vScrollKnobTexture, 20, 20, 20, 20))
        val scrollPaneStyle = ScrollPane.ScrollPaneStyle(TextureRegionDrawable(backgroundColorTexture()), null, null, null, NinePatchDrawable(scrollNine))

        val scrollPane = ScrollPane(innerTable, scrollPaneStyle).apply {
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
                val logLabel = Label(entry, Label.LabelStyle(fontPackage.font(FontSize.TEXT), backgroundColor.label().color()))
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
                this.add(textScrollPane())
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
                (MessageChannel.TEXT_VIEW_BRIDGE.isType(msg.message) ) -> {
                    val textViewMessage: TextViewMessage = MessageChannel.TEXT_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

                    currentText = textViewMessage.narrativeText
                    currentPrompts = textViewMessage.prompts

                    build()
                    return true
                }
            }
        }
        return false
    }
}