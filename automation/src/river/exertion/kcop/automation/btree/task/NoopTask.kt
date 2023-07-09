package river.exertion.kcop.automation.btree.task

import river.exertion.kcop.automation.btree.UserTask

class NoopTask(param : Int) : UserTask() {

    override val name: String = "noopTask$param"
}
