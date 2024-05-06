package river.exertion.thenuim.automation.btree.task.view

import river.exertion.thenuim.automation.btree.AutoUserTask
import river.exertion.thenuim.base.Log
import river.exertion.thenuim.view.layout.ButtonView
import java.util.*

class ButtonViewClickedCheckTask(val idx : Int? = 0) : AutoUserTask() {

    override val id = UUID.fromString("77f5308d-1972-418a-be0b-48580ec8792e")

    fun eval() = ButtonView.isChecked[idx!!] == true

    override fun executeTask() {
        Log.debug("is button $idx clicked? ${eval()}")
    }

    override fun returnStatus() = if (eval()) Status.SUCCEEDED else Status.FAILED

}
