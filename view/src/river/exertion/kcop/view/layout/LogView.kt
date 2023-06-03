package river.exertion.kcop.view.layout

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import river.exertion.kcop.asset.immersionTimer.ImmersionTimer
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.SdcHandler
import river.exertion.kcop.view.asset.FontSize

object LogView : ViewBase {

    override var viewType = ViewType.LOG
    override var viewTable = Table()

    var currentLog : MutableList<String>? = null

    val initTimeStr = ImmersionTimer.CumlTimeZero

    var instImmersionTimeStr = initTimeStr
    var cumlImmersionTimeStr = initTimeStr
    var localTimeStr = initTimeStr

    var textTimePaneDimensions = Vector2(this.tableWidth() / 4, this.tableWidth() / 13)

    private var scrollPane : ScrollPane = textScrollPane()

    fun textTimeBackgroundColorTexture() : TextureRegion {
        return SdcHandler.get("textTime", backgroundColor().triad().second).textureRegion().apply {
                this.setRegion(0, 0, textTimePaneDimensions.x.toInt() - 1, textTimePaneDimensions.y.toInt() - 1)
            }
    }

    fun isLog() = !currentLog.isNullOrEmpty()

    fun addLog(logEntry : String) {
        if (currentLog != null) {
            currentLog!!.add(logEntry)
        } else {
            currentLog = mutableListOf(logEntry)
        }

        build()
    }

    fun updateInstImmersionTime(newImmersionTimeStr : String) {
        instImmersionTimeStr = newImmersionTimeStr
        rebuildTextTimeReadout()
    }

    fun updateCumlImmersionTime(newImmersionTimeStr : String) {
        cumlImmersionTimeStr = newImmersionTimeStr
        rebuildTextTimeReadout()
    }

    fun updateLocalTime(newLocalTimeStr : String) {
        localTimeStr = newLocalTimeStr
        rebuildTextTimeReadout()
    }

    fun textTimeReadout() : Table {

        val innerTable = Table().padLeft(ViewType.padWidth(tableWidth())).padRight(ViewType.padWidth(tableWidth())).padTop(
            ViewType.padHeight(tableHeight())).padBottom(ViewType.padHeight(tableHeight()))

        innerTable.add(
            Stack().apply {
                this.add(Image(textTimeBackgroundColorTexture()))
                this.add(Table().apply {
                    this.add(Label(instImmersionTimeStr, KcopSkin.labelStyle(FontSize.TEXT, backgroundColor().label()))
                    .apply {
                        this.setAlignment(Align.center)
                    })
                    .padRight(ViewType.padWidth(tableWidth()))
                })
            }).size(this.textTimePaneDimensions.x, this.textTimePaneDimensions.y)

        innerTable.add(
            Stack().apply {
                this.add(Image(textTimeBackgroundColorTexture()))
                this.add(Table().apply {
                    this.add(Label(cumlImmersionTimeStr, KcopSkin.labelStyle(FontSize.TEXT, backgroundColor().label()))
                    .apply {
                        this.setAlignment(Align.center)
                    })
                    .padLeft(this@LogView.textTimePaneDimensions.y).padRight(this@LogView.textTimePaneDimensions.y)
                    .size(this@LogView.textTimePaneDimensions.x, this@LogView.textTimePaneDimensions.y)
                })
            }).size(this.textTimePaneDimensions.x, this.textTimePaneDimensions.y)

        innerTable.add(
            Stack().apply {
                this.add(Image(textTimeBackgroundColorTexture()))
                this.add(Table().apply {
                    this.add(Label(localTimeStr, KcopSkin.labelStyle(FontSize.TEXT, backgroundColor().label()))
                    .apply {
                        this.setAlignment(Align.center)
                    })
                    .padRight(ViewType.padWidth(tableWidth()))
                })
            }).size(this.textTimePaneDimensions.x, this.textTimePaneDimensions.y)

       // innerTable.debug()

        return innerTable
    }

    fun rebuildTextTimeReadout() {
        viewTable.clearChildren()

        viewTable.add(Stack().apply {
            this.add(Table().apply {
                this.add(backgroundColorImg()).grow()
            })
            this.add(Table().apply {
                this.add(textTimeReadout())
                this.row()
                this.add(scrollPane).grow()
            })
        }).size(this.tableWidth(), this.tableHeight())
        viewTable.clip()
    }

    fun textScrollPane() : ScrollPane {

        val innerTable = Table()
            .padLeft(ViewType.padWidth(tableWidth()))
            .padRight(ViewType.padWidth(tableWidth()))
            .padTop(ViewType.padHeight(tableHeight()))
            .padBottom(
                ViewType.padHeight(tableHeight())
        )

        if (isLog()) {
            (currentLog!!.size - 1 downTo 0).forEach { revEntryIdx ->
                val logLabel = Label(currentLog!![revEntryIdx], KcopSkin.labelStyle(FontSize.TEXT, backgroundColor().label()))
                logLabel.wrap = true
                innerTable.add(logLabel).growX()
                innerTable.row()
            }
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
//            this.debug()
        }

        this.scrollPane = scrollPane

        return scrollPane
    }

    override fun buildCtrl() {

        viewTable.add(Stack().apply {
            this.add(Table().apply {
                this.add(backgroundColorImg()).grow()
            })
            this.add(Table().apply {
//                this.debug()
                this.add(textTimeReadout())
                this.row()
                this.add(textScrollPane()).grow()
            })
 //           this.debug()
        }).size(this.tableWidth(), this.tableHeight())
        viewTable.clip()
    }

    fun resetTime() {
        updateInstImmersionTime(initTimeStr)
        updateCumlImmersionTime(initTimeStr)
        updateLocalTime(initTimeStr)
        rebuildTextTimeReadout()
    }
}