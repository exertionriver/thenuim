package river.exertion.kcop.automation.btree.task.view

import river.exertion.kcop.automation.btree.AutoUserTask
import river.exertion.kcop.base.Log
import river.exertion.kcop.view.layout.ButtonView
import java.util.*

class ButtonViewClickedCheckTask(val idx : Int? = 0) : AutoUserTask() {

    override val id = UUID.fromString("77f5308d-1972-418a-be0b-48580ec8792e")

    fun eval() = ButtonView.isChecked[idx!!] == true

    override fun executeTask() {
        Log.debug("is button $idx clicked? ${eval()}")
    }

    override fun returnStatus() = if (eval()) Status.SUCCEEDED else Status.FAILED

}
