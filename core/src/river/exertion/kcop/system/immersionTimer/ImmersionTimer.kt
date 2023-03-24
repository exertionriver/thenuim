package river.exertion.kcop.system.immersionTimer

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.utils.TimeUtils
import river.exertion.kcop.Id

class ImmersionTimer(var startTime : Long = TimeUtils.millis(), startState : ImmersionTimerState = ImmersionTimerState.PAUSED) : Id() {

    val stateMachine = DefaultStateMachine(this, startState)

    var pausedAtTime : Long = 0
    var pausedDurationMillis : Long = 0

    var isActive = false

    init {
        if (startState == ImmersionTimerState.PAUSED) pausedAtTime = startTime //init timer in paused mode
    }

    private fun pausedTime() = if (pausedAtTime > 0) TimeUtils.timeSinceMillis(pausedAtTime) + pausedDurationMillis else pausedDurationMillis

    private fun activeTime() = TimeUtils.timeSinceMillis(startTime) - pausedTime()

    private fun immersionTimeHours() = activeTime() / (1000f * 60f * 60f)
    private fun immersionTimeMinutes() = activeTime() / (1000f * 60f)
    private fun immersionTimeSeconds() = activeTime() / 1000f

    private fun immersionTimeHoursStr() = immersionTimeHours().toInt().toString().padStart(2, '0')
    private fun immersionTimeMinutesStr() = (immersionTimeMinutes().toInt() % 60).toString().padStart(2, '0')
    private fun immersionTimeSecondsStr() = (immersionTimeSeconds().toInt() % 60).toString().padStart(2, '0')

    fun immersionTime() = "${immersionTimeHoursStr()}:${immersionTimeMinutesStr()}:${immersionTimeSecondsStr()}"

    fun setPastStartTime(millisAgo : Long = 0L) {
        pausedAtTime = startTime
        startTime -= millisAgo
    }

    fun resetTimer() {
        startTime = TimeUtils.millis()
        pausedAtTime = if (stateMachine.currentState == ImmersionTimerState.PAUSED) startTime else 0
    }

    fun pauseTimer() {
        stateMachine.changeState(ImmersionTimerState.PAUSED)
        pausedAtTime = TimeUtils.millis()

        isActive = false
    }

    fun resumeTimer() {
        stateMachine.changeState(ImmersionTimerState.RUNNING)
        pausedDurationMillis += TimeUtils.timeSinceMillis(pausedAtTime)
        pausedAtTime = 0

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