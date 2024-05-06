package river.exertion.thenuim.view.layout

import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table

object DisplayAuxView : ViewBase {

    override var viewType = ViewType.DISPLAY_AUX
    override var viewTable = Table()

    var displayViewTable = Table()

    override fun buildCtrl() {
        viewTable.add(
            Stack().apply {
                this.add(Table().apply {
                    this.add(backgroundColorImg()).grow()
                })
                if (!displayViewTable.cells.isEmpty) {
                    this.add(Table().apply {
                        this.add(displayViewTable).grow()
                    })
                }
           }).size(this.tableWidth(), this.tableHeight())

        viewTable.clip()
        viewTable.validate()
    }
}