package immersionTimer

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import river.exertion.thenuim.asset.immersionTimer.ImmersionTimer
import river.exertion.thenuim.base.Log
import river.exertion.thenuim.base.TestBase

class TestImmersionTimer : TestBase() {

    @Test
    fun testTimerInit() {

        val testTimer = ImmersionTimer()

        Log.test("timer initialized", "${testTimer.stateMachine.currentState}, @ ${testTimer.immersionTime()}")
        assertEquals(0L, testTimer.immersionTimeInSeconds())

        runBlocking { delay(2000L) }

        assertEquals(0L, testTimer.immersionTimeInSeconds())

        testTimer.setPastStartTime("00:12:34")
        Log.test("timer set", "${testTimer.stateMachine.currentState}, @ ${testTimer.immersionTime()}")

        runBlocking { delay(2000L) }

        assertEquals(754L, testTimer.immersionTimeInSeconds())
    }

    @Test
    fun testTimerRun() {

        val testTimer = ImmersionTimer()

        Log.test("timer initialized", "${testTimer.stateMachine.currentState}, @ ${testTimer.immersionTime()}")
        testTimer.startOrResumeTimer()

        runBlocking { delay(2000L) }

        Log.test("timer running", "${testTimer.stateMachine.currentState}, @ ${testTimer.immersionTime()}")
        assertEquals(2L, testTimer.immersionTimeInSeconds())

        runBlocking { delay(5000L) }

        Log.test("timer running", "${testTimer.stateMachine.currentState}, @ ${testTimer.immersionTime()}")
        assertEquals(7L, testTimer.immersionTimeInSeconds())
    }

    @Test
    fun testTimerPause() {

        val testTimer = ImmersionTimer()

        testTimer.setPastStartTime("00:01:23")

        Log.test("timer initialized at", "${testTimer.stateMachine.currentState}, @ ${testTimer.immersionTime()}")
        testTimer.startOrResumeTimer()

        runBlocking { delay(2000L) }

        testTimer.pauseTimer()
        Log.test("timer paused", "${testTimer.stateMachine.currentState}, @ ${testTimer.immersionTime()}")
        assertEquals(85L, testTimer.immersionTimeInSeconds())

        runBlocking { delay(5000L) }

        Log.test("timer still paused", "${testTimer.stateMachine.currentState}, @ ${testTimer.immersionTime()}")
        assertEquals(85L, testTimer.immersionTimeInSeconds())

        testTimer.startOrResumeTimer()

        runBlocking { delay(2000L) }

        Log.test("timer resumed", "${testTimer.stateMachine.currentState}, @ ${testTimer.immersionTime()}")
        assertEquals(87L, testTimer.immersionTimeInSeconds())
    }


    @Test
    fun testTimerReset() {

        val testTimer = ImmersionTimer()

        testTimer.setPastStartTime("00:03:45")

        Log.test("timer initialized at", "${testTimer.stateMachine.currentState}, @ ${testTimer.immersionTime()}")
        assertEquals(225L, testTimer.immersionTimeInSeconds())
        testTimer.startOrResumeTimer()

        runBlocking { delay(7000L) }

        Log.test("timer running, reset", "${testTimer.stateMachine.currentState}, @ ${testTimer.immersionTime()}")
        assertEquals(232L, testTimer.immersionTimeInSeconds())

        testTimer.resetTimer()

        runBlocking { delay(5000L) }

        Log.test("timer running", "${testTimer.stateMachine.currentState}, @ ${testTimer.immersionTime()}")
        assertEquals(5L, testTimer.immersionTimeInSeconds())
    }
}