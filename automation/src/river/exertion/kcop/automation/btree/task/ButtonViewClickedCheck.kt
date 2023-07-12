package river.exertion.kcop.automation.btree.task

import river.exertion.kcop.automation.btree.AutoUserTask
import river.exertion.kcop.base.Log
import river.exertion.kcop.view.layout.ButtonView

class ButtonViewClickedCheck(val idx : Int? = 0) : AutoUserTask() {

    fun eval() = ButtonView.isChecked[idx!!] == true

    override fun executeTask() {
        Log.debug("is button $idx clicked? ${eval()}")
    }

    override fun returnStatus() = if (eval()) Status.SUCCEEDED else Status.FAILED

}
