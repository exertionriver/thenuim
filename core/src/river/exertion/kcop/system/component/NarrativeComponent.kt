package river.exertion.kcop.system.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import ktx.ashley.mapperFor
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.immersionTimer.ImmersionTimerState
import river.exertion.kcop.system.view.ImmersionTimerMessage
import river.exertion.kcop.system.view.ViewMessage

class NarrativeComponent : Component, Telegraph {

    var narrative : Narrative? = null
    var instImmersionTimers : MutableMap<String, ImmersionTimer> = mutableMapOf()
    var cumlImmersionTimers : MutableMap<String, ImmersionTimer> = mutableMapOf()

    var isActive = false

    init {
        MessageChannel.NARRATIVE_PROMPT_BRIDGE.enableReceive(this)
    }

    fun initTimers() {
        if (narrative != null) {
            narrative!!.narrativeBlocks.forEach { narrativeBlock ->
                instImmersionTimers[narrativeBlock.id] = ImmersionTimer()
                cumlImmersionTimers[narrativeBlock.id] = ImmersionTimer()
            }
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean {

        if ( (msg != null) && (MessageChannel.NARRATIVE_PROMPT_BRIDGE.isType(msg.message))) {
            val promptMessage : ViewMessage = MessageChannel.NARRATIVE_PROMPT_BRIDGE.receiveMessage(msg.extraInfo)

            if (narrative != null && isActive) {
                narrative!!.next(promptMessage.messageContent)
            }
        }

        return false
    }

    companion object {
        val mapper = mapperFor<NarrativeComponent>()

        fun has(entity : Entity) : Boolean = entity.components.firstOrNull{ it is NarrativeComponent } != null
        fun getFor(entity : Entity) : NarrativeComponent? = if (has(entity)) entity.components.first { it is NarrativeComponent } as NarrativeComponent else null

    }

}