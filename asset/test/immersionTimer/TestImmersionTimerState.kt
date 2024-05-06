package immersionTimer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import river.exertion.thenuim.asset.immersionTimer.ImmersionTimer
import river.exertion.thenuim.asset.immersionTimer.ImmersionTimerState
import river.exertion.thenuim.base.Log
import river.exertion.thenuim.base.TestBase

class TestImmersionTimerState : TestBase() {

    @Test
    fun testTimerStateTransition() {

        val testTimer = ImmersionTimer()

        Log.test("current state", testTimer.stateMachine.currentState.toString())
        assertEquals(testTimer.stateMachine.currentState, ImmersionTimerState.PAUSED)

        testTimer.stateMachine.changeState(ImmersionTimerState.RUNNING)

        Log.test("update(), current state", testTimer.stateMachine.currentState.toString())
        assertEquals(testTimer.stateMachine.currentState, ImmersionTimerState.RUNNING)

        testTimer.stateMachine.changeState(ImmersionTimerState.PAUSED)

        Log.test("update(), current state", testTimer.stateMachine.currentState.toString())
        assertEquals(testTimer.stateMachine.currentState, ImmersionTimerState.PAUSED)
    }
}