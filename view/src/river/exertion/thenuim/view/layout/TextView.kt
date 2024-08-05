package river.exertion.thenuim.view.layout

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.thenuim.view.TnmSkin
import river.exertion.thenuim.view.TnmFont

object TextView : ViewBase {

    override var viewType = ViewType.TEXT
    override var viewTable = Table()

    var currentText : String = ""
    var currentPrompts : List<String>? = null

    private lateinit var scrollPane : ScrollPane

    fun isPrompts() = !currentPrompts.isNullOrEmpty()

    fun textScrollPane() : ScrollPane {

        val innerTable = Table().padLeft(ViewType.padWidth(TnmSkin.screenWidth)).padRight(ViewType.padWidth(TnmSkin.screenWidth)).padTop(
            ViewType.padHeight(
                TnmSkin.screenHeight
            )
        ).padBottom(
            ViewType.padHeight(TnmSkin.screenHeight)
        )

        val textLabel = Label(currentText, TnmSkin.labelStyle(TnmFont.TEXT, backgroundColor().label()))

        textLabel.wrap = true
        innerTable.add(textLabel).growX()

        if (AiView.isChecked && AiView.hintText().isNotBlank()) {
            val hintLabel = Label(AiView.hintText(), TnmSkin.labelStyle(TnmFont.TEXT, backgroundColor().label().decr(4)))

            innerTable.row()
            innerTable.add(hintLabel).growX()
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

    fun promptPane() : Table {

        val innerTable = Table().padLeft(ViewType.padWidth(TnmSkin.screenWidth)).padRight(ViewType.padWidth(TnmSkin.screenWidth)).padTop(
            ViewType.padHeight(
                    TnmSkin.screenHeight
            )
        ).padBottom(
            ViewType.padHeight(TnmSkin.screenHeight)
        )

        if (isPrompts()) {
            currentPrompts!!.forEach { entry ->
                val logLabel = Label(entry, TnmSkin.labelStyle(TnmFont.TEXT, backgroundColor().label()))
                logLabel.wrap = true
                innerTable.add(logLabel).grow()
                innerTable.row()
            }
        }

        innerTable.top()
        if (TnmSkin.displayMode) innerTable.debug()

        return innerTable
    }

    override fun buildCtrl() {

        viewTable.add(Stack().apply {
            this.add(backgroundColorImg())
            this.add(Table().apply {
                this.add(textScrollPane()).grow()
                this.row()
                this.add(promptPane()).growX()
            })
        }).size(this.tableWidth(), this.tableHeight())
        viewTable.clip()
    }

    fun clearContent() {
        currentText = ""
        currentPrompts = null
        build()
    }

}