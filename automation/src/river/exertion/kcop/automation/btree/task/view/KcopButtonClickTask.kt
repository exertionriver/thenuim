package river.exertion.kcop.automation.btree.task.view

import river.exertion.kcop.automation.btree.task.core.ClickTask
import river.exertion.kcop.base.Log
import river.exertion.kcop.view.layout.ViewLayout
import river.exertion.kcop.view.screenLocation
import java.util.*

class KcopButtonClickTask : ClickTask(ViewLayout.kcopButton.screenLocation()) {

    override val id = UUID.fromString("398677bf-b74c-42d6-8bfe-d89b31134630")

    override fun executeTask() {
        Log.debug("click kcop button")

        super.executeTask()
    }
}
