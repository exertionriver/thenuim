package river.exertion.thenuim.automation.btree.task.view

import river.exertion.thenuim.automation.btree.task.core.ClickTask
import river.exertion.thenuim.base.Log
import river.exertion.thenuim.view.layout.ViewLayout
import river.exertion.thenuim.view.screenLocation
import java.util.*

class KcopButtonClickTask : ClickTask(ViewLayout.kcopButton.screenLocation()) {

    override val id = UUID.fromString("398677bf-b74c-42d6-8bfe-d89b31134630")

    override fun executeTask() {
        Log.debug("click kcop button")

        super.executeTask()
    }
}
