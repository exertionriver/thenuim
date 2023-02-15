package river.exertion.kcop.system.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import ktx.ashley.mapperFor
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.view.LogViewMessage
import river.exertion.kcop.system.view.LogViewMessageType
import river.exertion.kcop.system.view.ViewMessage
import river.exertion.kcop.system.view.ViewType

class NarrativeComponent : Component, Telegraph {

    var narrative : Narrative? = null

    val narrativeImmersionTimer = ImmersionTimerComponent()
    var blockImmersionTimers : MutableMap<String, ImmersionTimerComponent> = mutableMapOf()

    var isActive = false //ie., is the current simulation
    var isInitialized = false

    fun isPaused() = narrativeImmersionTimer.cumlImmersionTimer.isPaused()

    fun narrativeId() = narrative?.id ?: ""
    fun narrativeBlockId() = narrative?.currentId ?: ""
    fun instNarrativeTimerId() = narrativeImmersionTimer.instImmersionTimer.id
    fun cumlNarrativeTimerId() = narrativeImmersionTimer.cumlImmersionTimer.id
    fun instBlockTimerId() = blockImmersionTimers[narrativeBlockId()]?.instImmersionTimer?.id ?: ""
    fun cumlBlockTimerId() = blockImmersionTimers[narrativeBlockId()]?.cumlImmersionTimer?.id ?: ""

    init {
        MessageChannel.NARRATIVE_PROMPT_BRIDGE.enableReceive(this)
    }

    fun initTimers() {
        if (narrative != null) {
            narrative!!.narrativeBlocks.forEach { narrativeBlock ->
                blockImmersionTimers[narrativeBlock.id] = ImmersionTimerComponent()
            }
            isInitialized = true
        }
    }

    fun begin() {
        if (isInitialized) {
            isActive = true

            narrativeImmersionTimer.cumlImmersionTimer.beginTimer()
            narrativeImmersionTimer.instImmersionTimer.beginTimer()

            blockImmersionTimers[narrativeBlockId()]?.cumlImmersionTimer?.beginTimer()
            blockImmersionTimers[narrativeBlockId()]?.instImmersionTimer?.beginTimer()
        }
    }

    fun activate() {
        if (isInitialized) {
            isActive = true

            narrativeImmersionTimer.cumlImmersionTimer.resumeTimer()
            narrativeImmersionTimer.instImmersionTimer.resetTimer()

            blockImmersionTimers[narrativeBlockId()]?.cumlImmersionTimer?.resumeTimer()
            blockImmersionTimers[narrativeBlockId()]?.instImmersionTimer?.resetTimer()
        }
    }

    fun inactivate() {
        if (isInitialized) {
            isActive = false
            narrativeImmersionTimer.cumlImmersionTimer.pauseTimer()
            narrativeImmersionTimer.instImmersionTimer.pauseTimer()

            blockImmersionTimers[narrativeBlockId()]?.cumlImmersionTimer?.pauseTimer()
            blockImmersionTimers[narrativeBlockId()]?.instImmersionTimer?.pauseTimer()
        }
    }

    //active is assumed
    fun next(keypress : String) {
        if (isInitialized) {
            val prevBlockId = narrativeBlockId()
            narrative!!.next(keypress)

            //switch timers to new block
            if (prevBlockId != narrativeBlockId()) {
                blockImmersionTimers[prevBlockId]?.cumlImmersionTimer?.pauseTimer()
                blockImmersionTimers[prevBlockId]?.instImmersionTimer?.pauseTimer()

                if (blockImmersionTimers[narrativeBlockId()]?.cumlImmersionTimer?.isNotStarted() == true) {
                    blockImmersionTimers[narrativeBlockId()]?.cumlImmersionTimer?.beginTimer()
                } else {
                    blockImmersionTimers[narrativeBlockId()]?.cumlImmersionTimer?.resumeTimer()
                }

                if (blockImmersionTimers[narrativeBlockId()]?.instImmersionTimer?.isNotStarted() == true) {
                    blockImmersionTimers[narrativeBlockId()]?.instImmersionTimer?.beginTimer()
                } else {
                    blockImmersionTimers[narrativeBlockId()]?.instImmersionTimer?.resetTimer()
                }
            }
            MessageChannel.LAYOUT_BRIDGE.send(null, ViewMessage(ViewType.LOG, ViewMessage.BlockInstTimer, instBlockTimerId()))
            MessageChannel.LAYOUT_BRIDGE.send(null, ViewMessage(ViewType.LOG, ViewMessage.BlockCumlTimer, cumlBlockTimerId()))
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean {

        if ( (msg != null) && (MessageChannel.NARRATIVE_PROMPT_BRIDGE.isType(msg.message))) {
            val promptMessage : ViewMessage = MessageChannel.NARRATIVE_PROMPT_BRIDGE.receiveMessage(msg.extraInfo)

            if (isInitialized && isActive) {
                this.next(promptMessage.messageContent)
                return true
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