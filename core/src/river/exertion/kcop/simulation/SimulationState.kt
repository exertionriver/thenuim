package river.exertion.kcop.simulation

import com.badlogic.gdx.Input
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import river.exertion.kcop.simulation.view.ViewLayout
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.view.*

enum class SimulationState : State<ViewLayout> {

    RUNNING {
        override fun update(viewLayout : ViewLayout) {
            viewLayout.stateMachine.changeState(PAUSED)
            MessageChannel.IMMERSION_TIME_BRIDGE.send(null, ImmersionTimeMessage(PAUSED))
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "immersion state: paused"))
        }
    },
    PAUSED {
        override fun update(viewLayout : ViewLayout) {
            viewLayout.stateMachine.changeState(RUNNING)
            MessageChannel.IMMERSION_TIME_BRIDGE.send(null, ImmersionTimeMessage(RUNNING))
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "immersion state: running"))
        }
    }
    ;

    fun byName(name : String) : SimulationState? = values().firstOrNull { it.name == name }

    abstract override fun update(viewLayout: ViewLayout)

    override fun enter(viewLayout: ViewLayout?) {
    }

    override fun exit(viewLayout: ViewLayout?) {
    }

    override fun onMessage(viewLayout: ViewLayout?, telegram: Telegram?): Boolean {

        if (telegram != null && MessageChannel.LAYOUT_BRIDGE.isType(telegram.message) ) {
            val viewMessage : ViewMessage = MessageChannel.LAYOUT_BRIDGE.receiveMessage(telegram.extraInfo)

            if (viewLayout != null) {
                this.update(viewLayout)
            }

            return true
        }
        return false
    }

}
