package river.exertion.kcop.system.ecs.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.*

class NarrativeComponent : IComponent, Telegraph {

    init {
        MessageChannel.NARRATIVE_BRIDGE.enableReceive(this)
    }

    override var entityName = ""
    override var isInitialized = false

    var narrative : Narrative? = null
    fun narrative() = narrative

    val narrativeImmersionTimer = ImmersionTimerComponent()
    var blockImmersionTimers : MutableMap<String, ImmersionTimerComponent> = mutableMapOf()
    var flags : MutableList<String> = mutableListOf()

    var isActive = false //ie., is the current simulation
    var changed = false

    fun narrativeId() = narrative()?.id ?: ""

    fun narrativeCurrBlockId() = narrative()?.currentId ?: narrative()?.firstBlock()?.id ?: ""
    fun narrativePrevBlockId() = narrative()?.previousId ?: ""

    fun seqNarrativeProgress() : Float = ((narrative?.currentIdx()?.plus(1))?.toFloat() ?: 0f) / (narrative?.narrativeBlocks?.size ?: 1)
    fun sequentialStatusKey() : String = "progress(${narrativeId().subSequence(0, 3)})"

    override fun initialize(entityName: String, initData: Any?) {
        super.initialize(entityName, initData)

        var currentBlockId = narrativeCurrBlockId()
        var currentCumlTimeAgo = 0L

        if (initData != null) {
            val narrativeComponentInit = IComponent.checkInitType<NarrativeComponentInit>(initData)

            if (narrativeComponentInit != null) {
                narrative = narrativeComponentInit.narrativeAsset.narrative
                currentBlockId = narrativeComponentInit.currentBlockId
                currentCumlTimeAgo = ImmersionTimer.inMilliseconds(narrativeComponentInit.currentCumlTimer)
            }
        }

        //set the narrative layout
        MessageChannel.DISPLAY_VIEW_CONFIG_BRIDGE.send(null, ViewMessage(narrative!!.layoutTag))

        //TODO: store / load block cuml timers
        initTimers(currentCumlTimeAgo)
        activate(currentBlockId)
    }

    fun initTimers(currentCumlTimeAgo : Long = 0L) {
        if (narrative != null) {
            narrative!!.narrativeBlocks.forEach { narrativeBlock ->
                blockImmersionTimers[narrativeBlock.id] = ImmersionTimerComponent()
            }

            narrativeImmersionTimer.cumlImmersionTimer.setCumlTimeAgo(currentCumlTimeAgo)

            MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(EngineComponentMessageType.ADD_COMPONENT, entityName, ImmersionTimerComponent::class.java, narrativeImmersionTimer))

            changed = true
        }
    }

    fun unpause() {
        narrativeImmersionTimer.cumlImmersionTimer.resumeTimer()
        narrativeImmersionTimer.instImmersionTimer.resumeTimer()

        blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.resumeTimer()
        blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resumeTimer()

        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(EngineComponentMessageType.REPLACE_COMPONENT, entityName, ImmersionTimerComponent::class.java, narrativeImmersionTimer))
    }

    fun activate(currentBlockId: String) {
        if (isInitialized) {
            isActive = true

            narrative!!.currentId = currentBlockId

            unpause()

            narrativeImmersionTimer.instImmersionTimer.resetTimer()
            blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resetTimer()

            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.ADD_STATUS, sequentialStatusKey(), seqNarrativeProgress()))
            MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(EngineComponentMessageType.REPLACE_COMPONENT, entityName, ImmersionTimerComponent::class.java, narrativeImmersionTimer))

            changed = true
        }
    }

    fun pause() {
        narrativeImmersionTimer.cumlImmersionTimer.pauseTimer()
        narrativeImmersionTimer.instImmersionTimer.pauseTimer()

        blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.pauseTimer()
        blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.pauseTimer()

        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(EngineComponentMessageType.REPLACE_COMPONENT, entityName, ImmersionTimerComponent::class.java, narrativeImmersionTimer))
    }

    fun inactivate() {
        if (isInitialized) {
            isActive = false

            pause()

            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.REMOVE_STATUS, sequentialStatusKey()))
            MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(EngineComponentMessageType.REPLACE_COMPONENT, entityName, ImmersionTimerComponent::class.java, narrativeImmersionTimer))

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
                    blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.resetTimer()
                } else {
                    blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.resumeTimer()
                }

                blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resetTimer()
            }

            MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(EngineComponentMessageType.REPLACE_COMPONENT, entityName, ImmersionTimerComponent::class.java, narrativeImmersionTimer))
            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.UPDATE_STATUS, sequentialStatusKey(), seqNarrativeProgress()))
            changed = true
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean {

        if (msg != null) {
            if (MessageChannel.NARRATIVE_BRIDGE.isType(msg.message) && isActive) {
                val narrativeMessage: NarrativeMessage = MessageChannel.NARRATIVE_BRIDGE.receiveMessage(msg.extraInfo)

                when (narrativeMessage.narrativeMessageType) {
                    NarrativeMessage.NarrativeMessageType.PAUSE -> this.pause()
                    NarrativeMessage.NarrativeMessageType.UNPAUSE -> this.unpause()
                    NarrativeMessage.NarrativeMessageType.NEXT -> if (narrativeMessage.promptNext != null) this.next(narrativeMessage.promptNext)
                }
                return true
            }
        }
        return false
    }

    companion object {
        fun has(entity : Entity) : Boolean = entity.components.firstOrNull{ it is NarrativeComponent } != null
        fun getFor(entity : Entity) : NarrativeComponent? = if (has(entity)) entity.components.first { it is NarrativeComponent } as NarrativeComponent else null
    }

    data class NarrativeComponentInit(val narrativeAsset: NarrativeAsset, val currentBlockId: String, private val nullableCurrentCumlTimer: String?) {
        val currentCumlTimer: String
            get() = nullableCurrentCumlTimer ?: "00:00:00"
    }

}