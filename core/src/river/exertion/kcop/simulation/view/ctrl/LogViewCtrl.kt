package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.LogViewMessage
import river.exertion.kcop.system.messaging.messages.LogViewMessageType
import river.exertion.kcop.system.view.ShapeDrawerConfig


class LogViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewCtrl(ViewType.LOG, screenWidth, screenHeight) {

    init {
        MessageChannel.LOG_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.TWO_BATCH_BRIDGE.enableReceive(this)
        MessageChannel.FONT_BRIDGE.enableReceive(this)
    }

    var currentLog : MutableList<String>? = null
    var textTimeSdc : ShapeDrawerConfig? = null


    val initTimeStr = "00:00:00"

    var instImmersionTimeStr = initTimeStr
    var cumlImmersionTimeStr = initTimeStr
    var localTimeStr = initTimeStr

    var vScrollTexture : Texture? = null
    var vScrollKnobTexture : Texture? = null

    var textTimePaneDimensions = Vector2(this.tableWidth() / 4, this.tableWidth() / 13)

    private lateinit var scrollPane : ScrollPane

    fun textTimebackgroundColorTexture() : TextureRegion {
        if (this.textTimeSdc == null) this.textTimeSdc = ShapeDrawerConfig(batch, backgroundColor.triad().second.color())

        return textTimeSdc!!.textureRegion.apply {this.setRegion(0, 0, textTimePaneDimensions.x.toInt() - 1, textTimePaneDimensions.y.toInt() - 1) }
    }

    fun isLog() = !currentLog.isNullOrEmpty()

    fun addLog(logEntry : String) {
        if (currentLog == null) currentLog = mutableListOf()
        currentLog!!.add(logEntry)
    }

    fun updateInstImmersionTime(newImmersionTimeStr : String) {
        instImmersionTimeStr = newImmersionTimeStr
    }

    fun updateCumlImmersionTime(newImmersionTimeStr : String) {
        cumlImmersionTimeStr = newImmersionTimeStr
    }

    fun updateLocalTime(newLocalTimeStr : String) {
        localTimeStr = newLocalTimeStr
    }

    fun textTimeReadout() : Table {

        val innerTable = Table().padLeft(ViewType.padWidth(width)).padRight(ViewType.padWidth(width)).padTop(ViewType.padHeight(height)).padBottom(ViewType.padHeight(height))

        innerTable.add(
            Stack().apply {
                this.add(Image(textTimebackgroundColorTexture()))
                this.add(Table().apply {this.add(
                    Label(instImmersionTimeStr, Label.LabelStyle(fontPackage.font(FontSize.TEXT), backgroundColor.label().color())).apply { this.setAlignment(Align.center)})
                    .padRight(ViewType.padWidth(width))
                })
            }).size(this.textTimePaneDimensions.x, this.textTimePaneDimensions.y)

        innerTable.add(
            Stack().apply {
                this.add(Image(textTimebackgroundColorTexture()))
                this.add(Table().apply {this.add(
                    Label(cumlImmersionTimeStr, Label.LabelStyle(fontPackage.font(FontSize.TEXT), backgroundColor.label().color())).apply { this.setAlignment(Align.center)})
                    .padLeft(this@LogViewCtrl.textTimePaneDimensions.y).padRight(this@LogViewCtrl.textTimePaneDimensions.y)
                    .size(this@LogViewCtrl.textTimePaneDimensions.x, this@LogViewCtrl.textTimePaneDimensions.y)
                })
            }).size(this.textTimePaneDimensions.x, this.textTimePaneDimensions.y)

        innerTable.add(
            Stack().apply {
                this.add(Image(textTimebackgroundColorTexture()))
                this.add(Table().apply {this.add(
                    Label(localTimeStr, Label.LabelStyle(fontPackage.font(FontSize.TEXT), backgroundColor.label().color())).apply { this.setAlignment(Align.center)})
                    .padRight(ViewType.padWidth(width))
                })
            }).size(this.textTimePaneDimensions.x, this.textTimePaneDimensions.y)

//        innerTable.debug()

        return innerTable
    }

    fun rebuildTextTimeReadout() {
        this.clearChildren()

        this.add(Stack().apply {
            this.add(backgroundColorImg())
            this.add(Table().apply {
                this.add(textTimeReadout())
                this.row()
                this.add(scrollPane)
            })
        })
    }

    fun textScrollPane() : ScrollPane {

        val innerTable = Table().padLeft(ViewType.padWidth(width)).padRight(ViewType.padWidth(width)).padTop(
            ViewType.padHeight(
                height
            )
        ).padBottom(
            ViewType.padHeight(height)
        )

        if (isLog()) {
            (currentLog!!.size - 1 downTo 0).forEach { revEntryIdx ->
                val logLabel = Label(currentLog!![revEntryIdx], Label.LabelStyle(fontPackage.font(FontSize.TEXT), backgroundColor.label().color()))
                logLabel.wrap = true
                innerTable.add(logLabel).growX()
                innerTable.row()
            }
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

    override fun buildCtrl() {

        this.add(Stack().apply {
            this.add(backgroundColorImg())
            this.add(Table().apply {
                this.add(textTimeReadout())
                this.row()
                this.add(textScrollPane())
            })
        }).size(this.tableWidth(), this.tableHeight())
        this.clip()
    }

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
                (MessageChannel.LOG_VIEW_BRIDGE.isType(msg.message) ) -> {
                    val logMessage : LogViewMessage = MessageChannel.LOG_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

                    when {
                        (logMessage.messageType == LogViewMessageType.ResetTime) -> {
                            updateInstImmersionTime(initTimeStr)
                            updateCumlImmersionTime(initTimeStr)
                            rebuildTextTimeReadout()
                        }
                        else -> if (logMessage.message != null) {
                            if (logMessage.messageType == LogViewMessageType.LogEntry) {
                                addLog(logMessage.message)
                                build()
                            } else {
                                when (logMessage.messageType) {
                                    LogViewMessageType.InstImmersionTime -> updateInstImmersionTime(logMessage.message)
                                    LogViewMessageType.CumlImmersionTime -> updateCumlImmersionTime(logMessage.message)
                                    LogViewMessageType.LocalTime -> updateLocalTime(logMessage.message)
                                    else -> {}
                                }
                                rebuildTextTimeReadout()
                            }

                        }
                    }

                    return true
                }
            }
        }
        return false
    }

    companion object {
        const val NoProfileLoaded = "No Profile Loaded"
    }
}