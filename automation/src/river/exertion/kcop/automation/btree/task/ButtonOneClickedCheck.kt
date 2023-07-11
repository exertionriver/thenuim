package river.exertion.kcop.automation.btree.task

import river.exertion.kcop.automation.btree.AutoUserTask
import river.exertion.kcop.base.Log
import river.exertion.kcop.view.layout.ButtonView

class ButtonOneClickedCheck : AutoUserTask() {

    fun eval() = ButtonView.isChecked[1] == true

    override fun executeTask() {
        Log.debug("is button one clicked? ${eval()}")
    }

    override fun returnStatus() = if (eval()) Status.SUCCEEDED else Status.FAILED

}
