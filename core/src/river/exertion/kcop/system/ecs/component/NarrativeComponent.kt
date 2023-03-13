package river.exertion.kcop.system.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import ktx.ashley.mapperFor
import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.messaging.messages.*

class NarrativeComponent : IComponent, Component, Telegraph {

    var narrative : Narrative? = null

    val narrativeImmersionTimer = ImmersionTimerComponent()
    var blockImmersionTimers : MutableMap<String, ImmersionTimerComponent> = mutableMapOf()
    var flags : MutableList<String> = mutableListOf()

    var isActive = false //ie., is the current simulation

    lateinit var sequentialStatusKey : String

    fun isPaused() = narrativeImmersionTimer.cumlImmersionTimer.isPaused()

    var changed = false

    fun narrativeId() = narrative?.id ?: ""
    fun narrativeCurrBlockId() = narrative?.currentId ?: ""
    fun narrativePrevBlockId() = narrative?.previousId ?: ""
    fun instNarrativeTimerId() = narrativeImmersionTimer.instImmersionTimer.id
    fun cumlNarrativeTimerId() = narrativeImmersionTimer.cumlImmersionTimer.id
    fun instBlockTimerId() = blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.id ?: ""
    fun cumlBlockTimerId() = blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.id ?: ""

    init {
        MessageChannel.NARRATIVE_PROMPT_BRIDGE.enableReceive(this)
    }

    override var isInitialized = false
    override fun initialize(initData: Any?) {
        super.initialize(initData)

        if (initData != null) {
            val initDataEntry = initData as Pair<NarrativeAsset, String>

            narrative = initDataEntry.first.narrative
            narrative!!.currentId = initDataEntry.second
        }

        initTimers()
        activate()

        MessageChannel.NARRATIVE_COMPONENT_BRIDGE.send(null, NarrativeComponentMessage(this) )

    }

    fun seqNarrativeProgress() : Float = ((narrative?.currentIdx()?.plus(1))?.toFloat() ?: 0f) / (narrative?.narrativeBlocks?.size ?: 1)

    fun initTimers() {
        if (narrative != null) {
            narrative!!.narrativeBlocks.forEach { narrativeBlock ->
                blockImmersionTimers[narrativeBlock.id] = ImmersionTimerComponent()
            }

            sequentialStatusKey = "progress(${narrativeId().subSequence(0, 3)})"
            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.ADD_STATUS, sequentialStatusKey, 0f))

            isInitialized = true
            changed = true
        }
    }

    fun begin() {
        if (isInitialized) {
            isActive = true

            narrativeImmersionTimer.cumlImmersionTimer.beginTimer()
            narrativeImmersionTimer.instImmersionTimer.beginTimer()

            blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.beginTimer()
            blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.beginTimer()

            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.UPDATE_STATUS, sequentialStatusKey, seqNarrativeProgress()))
            changed = true
        }
    }

    fun activate() {
        if (isInitialized) {
            isActive = true

            narrativeImmersionTimer.cumlImmersionTimer.resumeTimer()
            narrativeImmersionTimer.instImmersionTimer.resetTimer()

            blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.resumeTimer()
            blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resetTimer()

            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.UPDATE_STATUS, sequentialStatusKey, seqNarrativeProgress()))
            changed = true
        }
    }

    fun inactivate() {
        if (isInitialized) {
            isActive = false
            narrativeImmersionTimer.cumlImmersionTimer.pauseTimer()
            narrativeImmersionTimer.instImmersionTimer.pauseTimer()

            blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.pauseTimer()
            blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.pauseTimer()

            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.UPDATE_STATUS, sequentialStatusKey, seqNarrativeProgress()))
            changed = true
        }
    }

    //active is assumed
    fun next(keypress : String) {
        if (isInitialized) {
            val possiblePrevBlockId = narrativeCurrBlockId()
            narrative!!.next(keypress)

            //switch timers to new block
            if (possiblePrevBlockId != narrativeCurrBlockId()) {
                blockImmersionTimers[narrativePrevBlockId()]?.cumlImmersionTimer?.pauseTimer()
                blockImmersionTimers[narrativePrevBlockId()]?.instImmersionTimer?.pauseTimer()

                if (blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.isNotStarted() == true) {
                    blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.beginTimer()
                } else {
                    blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.resumeTimer()
                }

                if (blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.isNotStarted() == true) {
                    blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.beginTimer()
                } else {
                    blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resetTimer()
                }
            }
            MessageChannel.LAYOUT_BRIDGE.send(null, ViewMessage(ViewType.LOG, ViewMessage.BlockInstTimer, instBlockTimerId()))
            MessageChannel.LAYOUT_BRIDGE.send(null, ViewMessage(ViewType.LOG, ViewMessage.BlockCumlTimer, cumlBlockTimerId()))

            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.UPDATE_STATUS, sequentialStatusKey, seqNarrativeProgress()))

            changed = true
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