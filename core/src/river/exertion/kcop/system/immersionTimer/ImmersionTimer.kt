package river.exertion.kcop.system.immersionTimer

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.utils.TimeUtils
import river.exertion.kcop.Id
import river.exertion.kcop.system.messaging.MessageChannel

class ImmersionTimer(var startTime : Long = TimeUtils.millis(), startState : ImmersionTimerState = ImmersionTimerState.PAUSED) : Id(), Telegraph {

    val stateMachine = DefaultStateMachine(this, startState)

    var timePausedAt : Long = 0
    var pausedTime : Long = 0

    init {
        MessageChannel.IMMERSION_TIME_BRIDGE.enableReceive(this)
        if (startState == ImmersionTimerState.PAUSED) timePausedAt = startTime //init timer in paused mode
    }

    private fun pausedTime() = if (timePausedAt > 0) TimeUtils.timeSinceMillis(timePausedAt) + pausedTime else pausedTime

    private fun activeTime() = TimeUtils.timeSinceMillis(startTime) - pausedTime()

    private fun immersionTimeHours() = activeTime() / (1000f * 60f * 60f)
    private fun immersionTimeMinutes() = activeTime() / (1000f * 60f)
    private fun immersionTimeSeconds() = activeTime() / 1000f

    private fun immersionTimeHoursStr() = immersionTimeHours().toInt().toString().padStart(2, '0')
    private fun immersionTimeMinutesStr() = (immersionTimeMinutes().toInt() % 60).toString().padStart(2, '0')
    private fun immersionTimeSecondsStr() = (immersionTimeSeconds().toInt() % 60).toString().padStart(2, '0')

    fun immersionTime() = "${immersionTimeHoursStr()}:${immersionTimeMinutesStr()}:${immersionTimeSecondsStr()}"

    fun isNotStarted() = ( stateMachine.currentState == ImmersionTimerState.PAUSED ) && ( activeTime().toInt() == 0 )

    fun isPaused() = ( stateMachine.currentState == ImmersionTimerState.PAUSED ) && ( activeTime() > 0 )

    fun beginTimer() {
        stateMachine.changeState(ImmersionTimerState.RUNNING)
        resetTimer()
    }

    fun resetTimer() {
        if (stateMachine.currentState != ImmersionTimerState.RUNNING) stateMachine.changeState(ImmersionTimerState.RUNNING)
        startTime = TimeUtils.millis()
        timePausedAt = 0
        pausedTime = 0
    }

    fun pauseTimer() {
        if (stateMachine.currentState != ImmersionTimerState.PAUSED) stateMachine.changeState(ImmersionTimerState.PAUSED)
        timePausedAt = TimeUtils.millis()
    }

    fun resumeTimer() {
        if (stateMachine.currentState != ImmersionTimerState.RUNNING) stateMachine.changeState(ImmersionTimerState.RUNNING)
        pausedTime += TimeUtils.timeSinceMillis(timePausedAt)
        timePausedAt = 0
    }

    fun onOrPast(timeString : String) : Boolean {
        return immersionTimeSeconds() >= inSeconds(timeString)
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        return this.stateMachine.currentState.onMessage(this, msg)
    }

    companion object {

        fun inSeconds(timeString : String) : Int {
            val timeSplit = timeString.split(":")
            return timeSplit[0].toInt() * 3600 + timeSplit[1].toInt() * 60 + timeSplit[2].toInt()
        }
    }
}