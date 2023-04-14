package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.DisplayStatus
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.StatusViewMessage
import river.exertion.kcop.system.view.ShapeDrawerConfig
import kotlin.math.roundToInt


class StatusViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewCtrl(ViewType.STATUS, screenWidth, screenHeight) {

    init {
        MessageChannel.STATUS_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.TWO_BATCH_BRIDGE.enableReceive(this)
        MessageChannel.FONT_BRIDGE.enableReceive(this)
    }

    val displayStatuses : MutableList<DisplayStatus> = mutableListOf()

    var vScrollKnobTexture : Texture? = null

    private lateinit var scrollPane : ScrollPane

    fun statusColorTexture(overrideColor : ColorPalette? = null) : TextureRegion {

        if (overrideColor == null)
            this.sdc = ShapeDrawerConfig(batch, backgroundColor.color())
        else
            this.sdc = ShapeDrawerConfig(batch, overrideColor.color())

        return sdc!!.textureRegion.apply {this.setRegion(0, 0, ViewType.padWidth(screenWidth)
            .roundToInt(), ViewType.padHeight(screenHeight).roundToInt()) }
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
                ProgressBar(0f, 1f, .01f, false, ProgressBar.ProgressBarStyle(
                    NinePatchDrawable(NinePatch(statusColorTexture())),
                    null
                ).apply { this.knobBefore = NinePatchDrawable(NinePatch(statusColorTexture(backgroundColor.triad().first)))}
            ).apply { this.value = it.value }
            )
            barStack.add(
                Label(it.key, Label.LabelStyle(fontPackage.font(FontSize.TEXT), backgroundColor.label().incr(2).color()))
            )

            innerTable.add(barStack)
            innerTable.row()
        }

        innerTable.top()
//        innerTable.debug()

        val scrollNine = NinePatch(statusColorTexture(backgroundColor.triad().second.incr(2)))
        val scrollPaneStyle = ScrollPane.ScrollPaneStyle(TextureRegionDrawable(statusColorTexture()), null, null, null, NinePatchDrawable(scrollNine))

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

    override fun buildCtrl() {
        this.add(textScrollPane()).size(this.tableWidth(), this.tableHeight())
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
                (MessageChannel.STATUS_VIEW_BRIDGE.isType(msg.message) ) -> {
                    val statusViewMessage: StatusViewMessage = MessageChannel.STATUS_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

                    when (statusViewMessage.messageType) {
                        StatusViewMessage.StatusViewMessageType.AddStatus -> {
                            if (statusViewMessage.statusKey != null) {
                                displayStatuses.add(DisplayStatus(statusViewMessage.statusKey, (statusViewMessage.statusValue ?: 0f)))
                            }
                        }
                        StatusViewMessage.StatusViewMessageType.ClearStatuses -> {
                            displayStatuses.clear()
                        }
                        StatusViewMessage.StatusViewMessageType.RemoveStatus -> {
                            displayStatuses.removeIf { it.key == statusViewMessage.statusKey }
                        }
                        StatusViewMessage.StatusViewMessageType.UpdateStatus -> {
                            displayStatuses.firstOrNull { it.key == statusViewMessage.statusKey }?.value = statusViewMessage.statusValue ?: 0f
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