package river.exertion.kcop.automation.btree.task

import river.exertion.kcop.base.Log

class ButtonOneClickTask : ClickTask(800, 450) {

    override fun executeTask() {
        Log.debug("click button one")

        super.executeTask()
    }
}
