package river.exertion.thenuim.sim.narrative.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.thenuim.ecs.EngineHandler
import river.exertion.thenuim.ecs.component.IComponent
import river.exertion.thenuim.ecs.component.ImmersionTimerComponent
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.asset.immersionTimer.ImmersionTimer
import river.exertion.thenuim.asset.immersionTimer.ImmersionTimerPair
import river.exertion.thenuim.profile.asset.ProfileAsset
import river.exertion.thenuim.profile.settings.PSCompStatus
import river.exertion.thenuim.sim.narrative.NarrativeLoPa
import river.exertion.thenuim.sim.narrative.NarrativeLoPa.NarrativeBridge
import river.exertion.thenuim.sim.narrative.asset.NarrativeAsset
import river.exertion.thenuim.sim.narrative.asset.NarrativeStateAsset
import river.exertion.thenuim.sim.narrative.component.NarrativeComponentNavStatusHandler.activate
import river.exertion.thenuim.sim.narrative.component.NarrativeComponentNavStatusHandler.inactivate
import river.exertion.thenuim.sim.narrative.component.NarrativeComponentNavStatusHandler.next
import river.exertion.thenuim.sim.narrative.component.NarrativeComponentNavStatusHandler.pause
import river.exertion.thenuim.sim.narrative.component.NarrativeComponentNavStatusHandler.unpause
import river.exertion.thenuim.sim.narrative.messaging.NarrativeComponentMessage
import river.exertion.thenuim.sim.narrative.structure.ImmersionLocation
import river.exertion.thenuim.sim.narrative.structure.ImmersionStatus
import river.exertion.thenuim.sim.narrative.structure.Narrative
import river.exertion.thenuim.sim.narrative.structure.NarrativeState
import river.exertion.thenuim.view.layout.displayViewLayout.DVLayout
import river.exertion.thenuim.view.layout.displayViewLayout.DVLayoutHandler
import river.exertion.thenuim.view.TnmFont
import river.exertion.thenuim.view.layout.StatusView
import river.exertion.thenuim.view.layout.ViewLayout

class NarrativeComponent : IComponent, Telegraph {

    var narrative : Narrative
        get() = NarrativeAsset.currentNarrativeAsset.narrative
        set(value) { NarrativeAsset.currentNarrativeAsset.narrative = value }

    var narrativeState : NarrativeState
        get() = NarrativeStateAsset.currentNarrativeStateAsset.narrativeState
        set(value) { NarrativeStateAsset.currentNarrativeStateAsset.narrativeState = value }

    override var componentId = narrative.id
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

    var blockCumlTimer = false

    fun currentPrompts() = if (isInitialized) narrative.currentPrompts() else listOf()

    fun layoutTag() = if (isInitialized) narrative.layoutTag else DVLayout.DvLayoutEmptyTag

    fun currentDisplayText() = if (isInitialized) narrative.currentDisplayText() else "<no display text>"

    fun currentFontSize() = if (isInitialized) narrative.currentFontSize() else TnmFont.TEXT

    fun shortNarrativeName() = narrativeName().subSequence(0, 3)

    fun seqNarrativeProgress() : Float = ((narrative.currentIdx().plus(1)).toFloat()) / (narrative.narrativeBlocks.size)
    fun sequentialStatusLabel() : String = "progress(${shortNarrativeName()})"

    fun addOrUpdateCompletionStatus() {
        StatusView.addOrUpdateStatus("${PSCompStatus.selectionKey}_${shortNarrativeName()}", sequentialStatusLabel(), seqNarrativeProgress())
    }

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

        ViewLayout.currentDisplayViewLayoutHandler = NarrativeLoPa.displayViewLayoutHandler()
        DVLayoutHandler.currentDvLayout = NarrativeLoPa.dvLayoutByTag(layoutTag())

        narrativeState.blockFlags.clear()

        StatusView.clearStatuses()
        addOrUpdateCompletionStatus()

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

                    NarrativeComponentMessage.NarrativeMessageType.RemoveBlockCumlTimer -> {
                        blockCumlTimer = false
                    }
                    NarrativeComponentMessage.NarrativeMessageType.ReplaceCumlTimer -> {
                        blockCumlTimer = false
                        IComponent.ecsInit<ImmersionTimerComponent>(initData = ImmersionTimerPair(instImmersionTimer, cumlImmersionTimer))
                    }
                    NarrativeComponentMessage.NarrativeMessageType.ReplaceBlockCumlTimer -> {
                        blockCumlTimer = true
                        IComponent.ecsInit<ImmersionTimerComponent>(initData = ImmersionTimerPair(blockInstImmersionTimer(), blockCumlImmersionTimer()))
                    }
                    NarrativeComponentMessage.NarrativeMessageType.Pause -> pause()
                    NarrativeComponentMessage.NarrativeMessageType.Unpause -> unpause()
                    NarrativeComponentMessage.NarrativeMessageType.Inactivate -> inactivate()
                    NarrativeComponentMessage.NarrativeMessageType.Next -> if (narrativeComponentMessage.promptNext != null) next(narrativeComponentMessage.promptNext)
                    NarrativeComponentMessage.NarrativeMessageType.Refresh -> {
                        this.narrativeState.blockFlags.clear()
                        this.changed = true
                    }
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

            EngineHandler.replaceComponent<NarrativeComponent>()
        }
    }
}