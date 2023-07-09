import org.junit.jupiter.api.Test
import river.exertion.kcop.automation.btree.AutoUser
import river.exertion.kcop.automation.btree.behavior.NoopBehavior

class TestNoop {

    @Test
    fun testNoopRun() {
        val autoUser = AutoUser().apply {
            this.behaviorSequenceList.add(NoopBehavior(1))
            this.behaviorSequenceList.add(NoopBehavior(2))
            this.behaviorSequenceList.add(NoopBehavior(3))
        }

        repeat(4) { autoUser.exec() }
    }

}