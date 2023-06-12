package river.exertion.kcop.asset.immersionTimer

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.utils.TimeUtils

class ImmersionTimer {

    val stateMachine = DefaultStateMachine(this, ImmersionTimerState.PAUSED)

    private var startTimeMillis : Long = currentTimeMillis()
    private var pausedAtTimeMillis : Long = 0
    private var pausedDurationMillis : Long = 0

    init {
        //init at paused
        pausedAtTimeMillis = startTimeMillis
    }

    //paused time aggregates over timer lifespan unless timer is reset
    private fun pausedTime() = if (pausedAtTimeMillis > 0) timeSinceMillis(pausedAtTimeMillis) + pausedDurationMillis else pausedDurationMillis
    private fun activeTime() = TimeUtils.timeSinceMillis(startTimeMillis) - pausedTime()

    private fun immersionTimeHours() = activeTime() / (1000f * 60f * 60f)
    private fun immersionTimeMinutes() = activeTime() / (1000f * 60f)
    private fun immersionTimeSeconds() = activeTime() / 1000f

    private fun immersionTimeHoursStr() = immersionTimeHours().toInt().toString().padStart(2, '0')
    private fun immersionTimeMinutesStr() = (immersionTimeMinutes().toInt() % 60).toString().padStart(2, '0')
    private fun immersionTimeSecondsStr() = (immersionTimeSeconds().toInt() % 60).toString().padStart(2, '0')

    fun immersionTime() = "${immersionTimeHoursStr()}:${immersionTimeMinutesStr()}:${immersionTimeSecondsStr()}"
    fun immersionTimeInSeconds() = inSeconds(immersionTime())

    fun setPastStartTime(timeString : String?) = setPastStartTime(inMilliseconds(timeString ?: CumlTimeZero))

    private fun setPastStartTime(millisAgo : Long = 0L) {
        pausedAtTimeMillis = startTimeMillis
        startTimeMillis -= millisAgo
    }

    fun resetTimer() {
        startTimeMillis = currentTimeMillis()
        pausedAtTimeMillis = if (stateMachine.currentState == ImmersionTimerState.PAUSED) startTimeMillis else 0
        pausedDurationMillis = 0
    }

    private fun setPausedTimeAtCurrentTime() { pausedAtTimeMillis = currentTimeMillis() }

    fun pauseTimer() {
        if (stateMachine.currentState != ImmersionTimerState.PAUSED) {
            stateMachine.changeState(ImmersionTimerState.PAUSED)
            setPausedTimeAtCurrentTime()
        }
    }

    private fun addPausedTimeToDurationTimeAndClearPausedTime() {
        pausedDurationMillis += timeSinceMillis(pausedAtTimeMillis)
        pausedAtTimeMillis = 0
    }

    fun startOrResumeTimer() {
        if (stateMachine.currentState != ImmersionTimerState.RUNNING) {
            addPausedTimeToDurationTimeAndClearPausedTime()
            stateMachine.changeState(ImmersionTimerState.RUNNING)
        }
    }

    fun onOrPast(timeString : String?) : Boolean = immersionTimeSeconds() >= inSeconds(timeString)

    companion object {

        const val CumlTimeZero = "00:00:00"

        private fun currentTimeMillis() = TimeUtils.millis()
        private fun timeSinceMillis(millisAgo : Long = 0L) = TimeUtils.timeSinceMillis(millisAgo)

        fun isValidTime(timeString : String) : Boolean = "^[0-9]{2}:[0-9]{2}:[0-9]{2}\$".toRegex().matches(timeString)

        fun inSeconds(timeString : String?) : Long {
            val timeSplit = (timeString ?: CumlTimeZero).split(":")

            return timeSplit[0].toInt() * 3600L + timeSplit[1].toInt() * 60L + timeSplit[2].toInt()
        }

        fun inMilliseconds(timeString : String?) : Long = inSeconds(timeString) * 1000L
    }
}