package river.exertion.kcop.asset.immersionTimer

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram

enum class ImmersionTimerState : State<ImmersionTimer> {

    RUNNING,
    PAUSED
    ;

    override fun update(immersionTimer: ImmersionTimer) { }

    override fun enter(immersionTimer: ImmersionTimer?) { }

    override fun exit(immersionTimer: ImmersionTimer?) { }

    override fun onMessage(immersionTimer: ImmersionTimer?, telegram: Telegram?): Boolean {
        return true
    }

}
