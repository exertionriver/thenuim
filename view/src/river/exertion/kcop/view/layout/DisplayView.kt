package river.exertion.kcop.view.layout

import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.view.klop.IDisplayViewLayoutHandler
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.DisplayViewMenuHandler.currentMenuTag

object DisplayView : ViewBase {

    override var viewType = ViewType.DISPLAY
    override var viewTable = Table()

    var menuOpen = false

    override fun buildCtrl() {
        viewTable.add(
            Stack().apply {
                this.add(Table().apply {
                    this.add(backgroundColorImg()).grow()
                })
                if (currentDisplayViewLayoutHandler != null) {
                    this.add(Table().apply {
                        this.add(currentDisplayViewLayoutHandler!!.build()).grow()
                    })
                }
                if (menuOpen) {
                    this.add(Table().apply {
                        this.add(DisplayViewMenuHandler.buildByTag(currentMenuTag)).grow()
                    })
                }
           }).size(this.tableWidth(), this.tableHeight())

        viewTable.clip()
    }

    var currentDisplayViewLayoutHandler : IDisplayViewLayoutHandler? = null
}