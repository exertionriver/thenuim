package river.exertion.kcop.sim.narrative.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.ecs.ECSPackage.EngineComponentBridge
import river.exertion.kcop.ecs.component.IComponent
import river.exertion.kcop.ecs.component.ImmersionTimerComponent
import river.exertion.kcop.ecs.entity.SubjectEntity
import river.exertion.kcop.ecs.messaging.EngineComponentMessage
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimer
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.sim.narrative.NarrativePackage
import river.exertion.kcop.sim.narrative.component.NarrativeComponentNavStatusHandler.activate
import river.exertion.kcop.sim.narrative.component.NarrativeComponentNavStatusHandler.inactivate
import river.exertion.kcop.sim.narrative.component.NarrativeComponentNavStatusHandler.next
import river.exertion.kcop.sim.narrative.component.NarrativeComponentNavStatusHandler.pause
import river.exertion.kcop.sim.narrative.component.NarrativeComponentNavStatusHandler.unpause
import river.exertion.kcop.sim.narrative.messaging.NarrativeComponentMessage
import river.exertion.kcop.sim.narrative.messaging.NarrativeComponentMessage.Companion.NarrativeBridge
import river.exertion.kcop.sim.narrative.structure.ImmersionLocation
import river.exertion.kcop.sim.narrative.structure.ImmersionStatus
import river.exertion.kcop.sim.narrative.structure.Narrative
import river.exertion.kcop.sim.narrative.structure.NarrativeState
import river.exertion.kcop.sim.narrative.view.DVLayout
import river.exertion.kcop.view.asset.FontSize
import river.exertion.kcop.view.layout.StatusView

class NarrativeComponent : IComponent, Telegraph {

    var narrative : Narrative
        get() = NarrativePackage.currentNarrativeAsset.narrative
        set(value) { NarrativePackage.currentNarrativeAsset.narrative = value }

    var narrativeState : NarrativeState
        get() = NarrativePackage.currentNarrativeStateAsset.narrativeState
        set(value) { NarrativePackage.currentNarrativeStateAsset.narrativeState = value }

    override fun componentId() = narrative.id

    override var isInitialized = false

    val timerPair = ImmersionTimerPair(ImmersionTimer(), ImmersionTimer())

    fun narrativeName() = narrative.name

    fun narrativeCurrBlockId() = narrative.currentBlockId

    fun narrativePrevBlockId() = narrative.previousBlockId

    var flags : MutableList<ImmersionStatus>
        get() = narrativeState.flags
        set(value) { narrativeState.flags = value }

    var location : ImmersionLocation
        get() = narrativeState.location ?: ImmersionLocation(narrativeCurrBlockId(), cumlImmersionTime())
        set(value) { narrativeState.location = value }

    var blockImmersionTimers : MutableMap<String, ImmersionTimerPair> = mutableMapOf()

    fun blockImmersionTimersStr() = blockImmersionTimers.mapValues { it.value.cumlImmersionTimer.immersionTime() }.toMutableMap()

    fun cumlImmersionTime() = timerPair.cumlImmersionTimer.immersionTime()

    fun cumlBlockImmersionTimer() = blockImmersionTimers[narrativeCurrBlockId()]!!.cumlImmersionTimer

    var changed = false

    fun currentPrompts() = if (isInitialized) narrative.currentPrompts() else listOf()

    fun layoutTag() = if (isInitialized) narrative.layoutTag else DVLayout.DvLayoutTag

    fun currentDisplayText() = if (isInitialized) narrative.currentDisplayText() else "<no display text>"

    fun currentFontSize() = if (isInitialized) narrative.currentFontSize() else FontSize.TEXT

    fun seqNarrativeProgress() : Float = ((narrative.currentIdx().plus(1)).toFloat()) / (narrative.narrativeBlocks.size)
    fun sequentialStatusKey() : String = "progress(${narrativeName().subSequence(0, 3)})"

    override fun initialize(initData: Any?) {

        if (initData != null) {
            val narrativeComponentInit = IComponent.checkInitType<NarrativeComponentInit>(initData)

            if (narrativeComponentInit != null) {

                //set up narrative
                narrative = narrativeComponentInit.narrative

                //add timers for each block
                narrative.narrativeBlocks.forEach { narrativeBlock ->
                    blockImmersionTimers[narrativeBlock.id] = ImmersionTimerPair(ImmersionTimer(), ImmersionTimer())
                }

                //set the narrative layout
//                DVLayoutHandler.currentDvLayout = NarrativePackage.dvLayoutByTag(narrative!!.layoutTag)

                if (narrativeComponentInit.narrativeState != null) {
                    narrativeState = narrativeComponentInit.narrativeState

                    narrative.init(narrativeState.immersionBlockId())

                    //set the narrative cumulative timer
                    timerPair.cumlImmersionTimer.setPastStartTime(ImmersionTimer.inMilliseconds(narrativeComponentInit.cumlTime()))

                    //set block cumulative timers
                    narrativeComponentInit.blockImmersionTimers().entries.forEach { timerEntry ->
                        blockImmersionTimers[timerEntry.key]?.cumlImmersionTimer?.setPastStartTime(ImmersionTimer.inMilliseconds(timerEntry.value))
                    }
                } else {
     /*               narrativeImmersion = NarrativeImmersion(NarrativeImmersion.genId(narrativeComponentInit.profile.id, narrativeComponentInit.narrative.id)).apply {
                        this.location = ImmersionLocation(narrativeCurrBlockId(), cumlImmersionTime())
                        this.flags = mutableListOf()
                        this.blockImmersionTimers = blockImmersionTimersStr()
                    }
       */             narrative.init()
                }

                // set current profile narrative id
//                MessageChannelEnum.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UpdateImmersionId,
//                    null, componentId()
//                ))

                StatusView.clearStatuses()

                super.initialize(initData)

                //start timers, enable message receipt
                activate(narrativeCurrBlockId())

                //update kcop with current settings, including setting log timers
            }
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            if (MessageChannelHandler.isType(NarrativeBridge, msg.message) && isInitialized) {
                val narrativeComponentMessage: NarrativeComponentMessage = MessageChannelHandler.receiveMessage(NarrativeBridge, msg.extraInfo)

                when (narrativeComponentMessage.narrativeMessageType) {

                    NarrativeComponentMessage.NarrativeMessageType.ReplaceCumlTimer -> {
                        MessageChannelHandler.send(EngineComponentBridge, EngineComponentMessage(
                                EngineComponentMessage.EngineComponentMessageType.ReplaceComponent,
                                SubjectEntity.entityName, ImmersionTimerComponent::class.java, this.timerPair)
                        )
                    }

                    NarrativeComponentMessage.NarrativeMessageType.Pause -> pause()
                    NarrativeComponentMessage.NarrativeMessageType.Unpause -> unpause()
                    NarrativeComponentMessage.NarrativeMessageType.Inactivate -> inactivate()
                    NarrativeComponentMessage.NarrativeMessageType.Next -> if (narrativeComponentMessage.promptNext != null) next(narrativeComponentMessage.promptNext)
                }
                return true
            }
        }
        return false
    }

    companion object {
        fun has(entity : Entity) : Boolean = entity.components.firstOrNull{ it is NarrativeComponent } != null
        fun getFor(entity : Entity) : NarrativeComponent? = if (has(entity)) entity.components.first { it is NarrativeComponent } as NarrativeComponent else null

        fun isValid(narrativeComponent: NarrativeComponent?) : Boolean {
            return (narrativeComponent?.narrative != null && narrativeComponent.isInitialized)
        }

        fun ecsInit(narrative: Narrative, narrativeState: NarrativeState? = null) {
            //inactivate current narrative
            MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.Inactivate))

            MessageChannelHandler.send(EngineComponentBridge, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.ReplaceComponent,
                SubjectEntity.entityName, NarrativeComponent::class.java,
                NarrativeComponentInit(narrative, narrativeState)
            ) )
        }
    }

    data class NarrativeComponentInit(val narrative: Narrative, val narrativeState: NarrativeState? = null) {
        fun blockImmersionTimers() = narrativeState?.blockImmersionTimers ?: mapOf()
        fun cumlTime() = narrativeState?.cumlImmersionTime() ?: ImmersionTimer.CumlTimeZero
    }

    // only need to replace in case of settings change
    //            MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(EngineComponentMessageType.REPLACE_COMPONENT, narrativeComponent.narrativeImmersionTimer.entityName, ImmersionTimerComponent::class.java, narrativeComponent.narrativeImmersionTimer))

}