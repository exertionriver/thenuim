package river.exertion.kcop.sim.narrative.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.ecs.component.IComponent
import river.exertion.kcop.ecs.component.ImmersionTimerComponent
import river.exertion.kcop.ecs.entity.SubjectEntity
import river.exertion.kcop.ecs.messaging.EngineComponentMessage
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimer
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.sim.narrative.NarrativePackage
import river.exertion.kcop.sim.narrative.NarrativePackage.NarrativeBridge
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAsset
import river.exertion.kcop.sim.narrative.component.NarrativeComponentNavStatusHandler.activate
import river.exertion.kcop.sim.narrative.component.NarrativeComponentNavStatusHandler.inactivate
import river.exertion.kcop.sim.narrative.component.NarrativeComponentNavStatusHandler.next
import river.exertion.kcop.sim.narrative.component.NarrativeComponentNavStatusHandler.pause
import river.exertion.kcop.sim.narrative.component.NarrativeComponentNavStatusHandler.unpause
import river.exertion.kcop.sim.narrative.messaging.NarrativeComponentMessage
import river.exertion.kcop.sim.narrative.structure.ImmersionLocation
import river.exertion.kcop.sim.narrative.structure.ImmersionStatus
import river.exertion.kcop.sim.narrative.structure.Narrative
import river.exertion.kcop.sim.narrative.structure.NarrativeState
import river.exertion.kcop.sim.narrative.view.DVLayout
import river.exertion.kcop.sim.narrative.view.DVLayoutHandler
import river.exertion.kcop.view.asset.FontSize
import river.exertion.kcop.view.layout.DisplayView
import river.exertion.kcop.view.layout.StatusView

class NarrativeComponent : IComponent, Telegraph {

    var narrative : Narrative
        get() = NarrativeAsset.currentNarrativeAsset.narrative
        set(value) { NarrativeAsset.currentNarrativeAsset.narrative = value }

    var narrativeState : NarrativeState
        get() = NarrativeStateAsset.currentNarrativeStateAsset.narrativeState
        set(value) { NarrativeStateAsset.currentNarrativeStateAsset.narrativeState = value }

    override fun componentId() = narrative.id

    override var isInitialized = false

    val instImmersionTimer = ImmersionTimer()

    fun narrativeName() = narrative.name
    fun narrativeCurrBlockId() = narrative.currentBlockId
    fun narrativePrevBlockId() = narrative.previousBlockId

    var cumlImmersionTimer : ImmersionTimer
        get() = narrativeState.cumlImmersionTimer
        set(value) { narrativeState.cumlImmersionTimer = value }

    var flags : MutableList<ImmersionStatus>
        get() = narrativeState.flags
        set(value) { narrativeState.flags = value }

    var location : ImmersionLocation
        get() = narrativeState.location
        set(value) { narrativeState.location = value }

    var blockInstImmersionTimers : MutableMap<String, ImmersionTimer> = mutableMapOf()

    fun blockInstImmersionTimer() = blockInstImmersionTimers[narrativeCurrBlockId()]!!

    var blockCumlImmersionTimers : MutableMap<String, ImmersionTimer>
        get() = narrativeState.blockCumlImmersionTimers
        set(value) { narrativeState.blockCumlImmersionTimers = value }

    fun blockCumlImmersionTimer() = blockCumlImmersionTimers[narrativeCurrBlockId()]!!

    var changed = false

    fun currentPrompts() = if (isInitialized) narrative.currentPrompts() else listOf()

    fun layoutTag() = if (isInitialized) narrative.layoutTag else DVLayout.DvLayoutTag

    fun currentDisplayText() = if (isInitialized) narrative.currentDisplayText() else "<no display text>"

    fun currentFontSize() = if (isInitialized) narrative.currentFontSize() else FontSize.TEXT

    fun seqNarrativeProgress() : Float = ((narrative.currentIdx().plus(1)).toFloat()) / (narrative.narrativeBlocks.size)
    fun sequentialStatusKey() : String = "progress(${narrativeName().subSequence(0, 3)})"

    override fun initialize(initData: Any?) {

        super.initialize(initData)

        narrative.init(narrativeState.immersionBlockId())

        narrative.narrativeBlocks.forEach {
            blockInstImmersionTimers[it.id] = ImmersionTimer()
        }

        if (narrativeState.blockCumlImmersionTimers.isEmpty()) {
            narrative.narrativeBlocks.forEach {
                blockCumlImmersionTimers[it.id] = ImmersionTimer()
            }
        }

        DisplayView.currentDisplayViewLayoutHandler = NarrativePackage.displayViewLayoutHandler()

        narrativeState.blockFlags.clear()
        StatusView.clearStatuses()

        changed = true

        //start timers, enable message receipt
        activate(narrativeCurrBlockId())

        ProfileAsset.currentProfileAsset.profile.execSettings()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            if (MessageChannelHandler.isType(NarrativeBridge, msg.message) && isInitialized) {
                val narrativeComponentMessage: NarrativeComponentMessage = MessageChannelHandler.receiveMessage(NarrativeBridge, msg.extraInfo)

                when (narrativeComponentMessage.narrativeMessageType) {

                    NarrativeComponentMessage.NarrativeMessageType.ReplaceCumlTimer -> {
                        ImmersionTimerComponent.ecsInit(ImmersionTimerPair(instImmersionTimer, cumlImmersionTimer))
                    }
                    NarrativeComponentMessage.NarrativeMessageType.ReplaceBlockCumlTimer -> {
                        ImmersionTimerComponent.ecsInit(ImmersionTimerPair(blockInstImmersionTimer(), blockCumlImmersionTimer()))
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

        fun ecsInit() {
            //inactivate current narrative
            MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.Inactivate))

            EngineHandler.replaceComponent(componentClass = NarrativeComponent::class.java)
        }
    }
}