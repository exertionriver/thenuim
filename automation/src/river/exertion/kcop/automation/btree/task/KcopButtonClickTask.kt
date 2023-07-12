package river.exertion.kcop.automation.btree.task

import river.exertion.kcop.base.Log
import river.exertion.kcop.view.layout.ButtonView
import river.exertion.kcop.view.layout.DisplayView
import river.exertion.kcop.view.layout.ViewLayout

class KcopButtonClickTask() : ClickTask(ViewLayout.kcopButtonLocation()) {

    override fun executeTask() {
        Log.debug("click kcop button")

        super.executeTask()
    }
}
