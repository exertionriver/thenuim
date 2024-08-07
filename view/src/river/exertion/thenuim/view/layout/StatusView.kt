package river.exertion.thenuim.view.layout

import com.badlogic.gdx.scenes.scene2d.ui.*
import river.exertion.thenuim.view.TnmSkin
import river.exertion.thenuim.view.TnmFont

object StatusView : ViewBase {

    override var viewType = ViewType.STATUS
    override var viewTable = Table()

    val displayStatuses : MutableList<DisplayStatus> = mutableListOf()

    private lateinit var scrollPane : ScrollPane

    fun textScrollPane() : ScrollPane {

        val innerTable = Table().padLeft(ViewType.padWidth(TnmSkin.screenWidth)).padRight(ViewType.padWidth(TnmSkin.screenWidth)).padTop(
            ViewType.padHeight(
                TnmSkin.screenHeight
            )
        ).padBottom(
            ViewType.padHeight(TnmSkin.screenHeight)
        )

        displayStatuses.filter { it.display }.forEach {
            val barStack = Stack()

            barStack.add(
                ProgressBar(0f, 1f, .01f, false, TnmSkin.skin).apply { this.value = it.value } )
            barStack.add(
                Label(it.label, TnmSkin.labelStyle(TnmFont.TEXT, backgroundColor().label().incr(2)))
            )

            innerTable.add(barStack)
            innerTable.row()
        }

        innerTable.top()
        if (TnmSkin.displayMode) innerTable.debug()

            val scrollPane = ScrollPane(innerTable, TnmSkin.skin).apply {
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
        viewTable.add(Stack().apply {
            this.add(backgroundColorImg())
            this.add(textScrollPane())
        } ).size(this.tableWidth(), this.tableHeight())

        viewTable.clip()
    }

    fun addOrUpdateStatus(key : String, label : String, value : Float) {
        if (displayStatuses.map { it.key }.contains(key)) {
            displayStatuses.first { it.key == key }.value = value
            displayStatuses.first { it.key == key }.label = label
        } else {
            displayStatuses.add(DisplayStatus(key, label, value))
        }
    }

    fun clearStatuses() {
        displayStatuses.clear()
        build()
    }

    @Suppress("NewApi")
    fun removeStatus(key : String) {
        displayStatuses.removeIf { key == it.label }
    }

    fun showStatusByFilter(keyFilter : String) {
        displayStatuses.filter { it.key.contains(keyFilter) }.forEach { it.display = true }
        build()
    }

    fun hideStatusByFilter(keyFilter : String) {
        displayStatuses.filter { it.key.contains(keyFilter) }.forEach { it.display = false }
        build()
    }
}