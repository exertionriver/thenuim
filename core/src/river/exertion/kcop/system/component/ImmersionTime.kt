package river.exertion.kcop.system.component

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.utils.TimeUtils
import river.exertion.kcop.simulation.SimulationState
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.view.ImmersionTimeMessage

class ImmersionTime(val startTime : Long = TimeUtils.millis()) : Telegraph {

    init {
        MessageChannel.IMMERSION_TIME_BRIDGE.enableReceive(this)
    }

    var timePausedAt : Long = 0
    var pausedTime : Long = 0

    private fun pausedTime() = if (timePausedAt > 0) TimeUtils.timeSinceMillis(timePausedAt) + pausedTime else pausedTime

    private fun activeTime() = TimeUtils.timeSinceMillis(startTime) - pausedTime()

    private fun immersionTimeHours() = activeTime() / (1000f * 60f * 60f)
    private fun immersionTimeMinutes() = activeTime() / (1000f * 60f)
    private fun immersionTimeSeconds() = activeTime() / 1000f

    private fun immersionTimeHoursStr() = immersionTimeHours().toInt().toString().padStart(2, '0')
    private fun immersionTimeMinutesStr() = (immersionTimeMinutes().toInt() % 60).toString().padStart(2, '0')
    private fun immersionTimeSecondsStr() = (immersionTimeSeconds().toInt() % 60).toString().padStart(2, '0')

    fun immersionTime() = "${immersionTimeHoursStr()}:${immersionTimeMinutesStr()}:${immersionTimeSecondsStr()}"

    override fun handleMessage(msg: Telegram?): Boolean {
        if ((msg != null) && (MessageChannel.IMMERSION_TIME_BRIDGE.isType(msg.message))) {
            val immersionTimeMessage : ImmersionTimeMessage = MessageChannel.IMMERSION_TIME_BRIDGE.receiveMessage(msg.extraInfo)

            if (immersionTimeMessage.toState == SimulationState.PAUSED) {
                timePausedAt = TimeUtils.millis()
            }
            if (immersionTimeMessage.toState == SimulationState.RUNNING) {
                pausedTime += TimeUtils.timeSinceMillis(timePausedAt)
                timePausedAt = 0
            }
        }

        return true
    }
}