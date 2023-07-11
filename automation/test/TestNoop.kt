import org.junit.jupiter.api.Test
import river.exertion.kcop.automation.btree.AutoUserBehaviorHandler
import river.exertion.kcop.automation.btree.behavior.NoopBehavior

class TestNoop {

    @Test
    fun testNoopRun() {
            AutoUserBehaviorHandler.behaviorSequenceList.add(NoopBehavior(1))
            AutoUserBehaviorHandler.behaviorSequenceList.add(NoopBehavior(2))
            AutoUserBehaviorHandler.behaviorSequenceList.add(NoopBehavior(3))

        repeat(4) { AutoUserBehaviorHandler.execBehavior() }
    }

}