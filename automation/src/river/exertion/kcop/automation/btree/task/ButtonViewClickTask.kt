package river.exertion.kcop.automation.btree.task

import river.exertion.kcop.base.Log
import river.exertion.kcop.view.layout.ButtonView

class ButtonViewClickTask(val idx : Int? = 0) : ClickTask(ButtonView.buttonLocation(idx)) {

    override fun executeTask() {
        Log.debug("click button $idx")

        super.executeTask()
    }
}
