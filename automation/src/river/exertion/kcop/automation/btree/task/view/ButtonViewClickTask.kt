package river.exertion.kcop.automation.btree.task.view

import river.exertion.kcop.automation.btree.task.core.ClickTask
import river.exertion.kcop.base.Log
import river.exertion.kcop.view.layout.ButtonView
import river.exertion.kcop.view.screenLocation
import java.util.*

class ButtonViewClickTask(val idx : Int? = 0) : ClickTask(ButtonView.buttonList[idx!!].screenLocation()) {

    override val id = UUID.fromString("0928cdeb-0240-4393-b56e-3844fd3e0828")

    override fun executeTask() {
        Log.debug("click button $idx")

        super.executeTask()
    }
}
