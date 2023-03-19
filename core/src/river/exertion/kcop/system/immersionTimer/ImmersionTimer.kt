package river.exertion.kcop.system.immersionTimer

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.utils.TimeUtils
import river.exertion.kcop.Id
import river.exertion.kcop.system.messaging.MessageChannel

class ImmersionTimer(var startTime : Long = TimeUtils.millis(), startState : ImmersionTimerState = ImmersionTimerState.PAUSED) : Id() {

    val stateMachine = DefaultStateMachine(this, startState)

    var timePausedAt : Long = 0
    var pausedTime : Long = 0

    var isActive = false

    init {
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

    fun setCumlTimeAgo(cumlTimeAgo : Long = 0L) {
        timePausedAt = startTime
        startTime -= cumlTimeAgo
    }

    fun resetTimer() {
        if (stateMachine.currentState != ImmersionTimerState.RUNNING) stateMachine.changeState(ImmersionTimerState.RUNNING)
        startTime = TimeUtils.millis()
        timePausedAt = 0
        pausedTime = 0

        isActive = true
    }

    fun pauseTimer() {
        if (stateMachine.currentState != ImmersionTimerState.PAUSED) stateMachine.changeState(ImmersionTimerState.PAUSED)
        timePausedAt = TimeUtils.millis()

        isActive = false
    }

    fun resumeTimer() {
        if (stateMachine.currentState != ImmersionTimerState.RUNNING) stateMachine.changeState(ImmersionTimerState.RUNNING)
        pausedTime += TimeUtils.timeSinceMillis(timePausedAt)
        timePausedAt = 0

        isActive = true
    }

    fun onOrPast(timeString : String) : Boolean {
        return immersionTimeSeconds() >= inSeconds(timeString)
    }

    companion object {

        fun inSeconds(timeString : String) : Long {
            val timeSplit = timeString.split(":")
            return timeSplit[0].toInt() * 3600L + timeSplit[1].toInt() * 60L + timeSplit[2].toInt()
        }

        fun inMilliseconds(timeString : String) : Long = inSeconds(timeString) * 1000L
    }
}