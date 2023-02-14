package river.exertion.kcop.system.immersionTimer

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.utils.TimeUtils
import river.exertion.kcop.Id
import river.exertion.kcop.system.MessageChannel

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

    fun isPaused() = stateMachine.currentState == ImmersionTimerState.PAUSED

    fun restart() {
        startTime = TimeUtils.millis()
        timePausedAt = 0
        pausedTime = 0
    }

    fun pauserTimer() {
        timePausedAt = TimeUtils.millis()
    }

    fun restartTimer() {
        pausedTime += TimeUtils.timeSinceMillis(timePausedAt)
        timePausedAt = 0
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        return this.stateMachine.currentState.onMessage(this, msg)
    }
}