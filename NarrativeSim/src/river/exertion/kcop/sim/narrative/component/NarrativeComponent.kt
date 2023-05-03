package river.exertion.kcop.sim.narrative.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.ecs.component.IComponent
import river.exertion.kcop.ecs.entity.SubjectEntity
import river.exertion.kcop.ecs.immersionTimer.ImmersionTimer
import river.exertion.kcop.ecs.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.ecs.messaging.EngineComponentMessage
import river.exertion.kcop.ecs.messaging.EngineComponentMessage.Companion.EngineComponentBridge
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.component.NarrativeComponentMessageHandler.messageHandler
import river.exertion.kcop.sim.narrative.component.NarrativeComponentNavStatusHandler.activate
import river.exertion.kcop.sim.narrative.messaging.NarrativeMessage
import river.exertion.kcop.sim.narrative.messaging.NarrativeMessage.Companion.NarrativeBridge
import river.exertion.kcop.sim.narrative.structure.ImmersionLocation
import river.exertion.kcop.sim.narrative.structure.ImmersionStatus
import river.exertion.kcop.sim.narrative.structure.Narrative
import river.exertion.kcop.sim.narrative.structure.NarrativeImmersion
import river.exertion.kcop.sim.narrative.view.DVLayout
import river.exertion.kcop.view.FontSize
import river.exertion.kcop.view.messaging.DisplayViewTextMessage
import river.exertion.kcop.view.messaging.DisplayViewTextMessage.Companion.DisplayViewTextBridge
import river.exertion.kcop.view.messaging.StatusViewMessage
import river.exertion.kcop.view.messaging.StatusViewMessage.Companion.StatusViewBridge

class NarrativeComponent : IComponent, Telegraph {

    override var isInitialized = false

    var narrative : Narrative? = null

    var narrativeImmersion : NarrativeImmersion? = null

    override fun componentId() = if (narrative != null) narrative?.id!! else throw Exception("NarrativeComponent::component() narrative is null")

    fun narrativeName() = narrative?.name ?: throw Exception("narrative name:$this narrative.name not set")

    fun narrativeCurrBlockId() = narrative?.currentBlockId ?: throw Exception("narrativeCurrBlockId:$this narrative.currentBlockId not set")

    fun narrativePrevBlockId() = narrative?.previousBlockId ?: throw Exception("narrativePrevBlockId:$this narrative.previousBlockId not set")

    var flags : MutableList<ImmersionStatus>
        get() = narrativeImmersion?.flags ?: mutableListOf()
        set(value) { narrativeImmersion?.flags = value }

    var location : ImmersionLocation
        get() = narrativeImmersion?.location ?: ImmersionLocation(narrativeCurrBlockId(), cumlImmersionTime())
        set(value) { narrativeImmersion?.location = value }

    var blockFlags : MutableList<ImmersionStatus> = mutableListOf()
    fun eventFired(id : String) = blockFlags.any { it.key == id && it.value == NarrativeImmersion.EventFiredValue }

    val timerPair = ImmersionTimerPair(ImmersionTimer(), ImmersionTimer())

    var blockImmersionTimers : MutableMap<String, ImmersionTimerPair> = mutableMapOf()

    fun blockImmersionTimersStr() = blockImmersionTimers.mapValues { it.value.cumlImmersionTimer.immersionTime() }.toMutableMap()

    fun cumlImmersionTime() = timerPair.cumlImmersionTimer.immersionTime()

    fun cumlBlockImmersionTimer() = blockImmersionTimers[narrativeCurrBlockId()]!!.cumlImmersionTimer

    var changed = false

    fun currentPrompts() = if (isInitialized) narrative!!.currentPrompts() else listOf()

    fun layoutTag() = if (isInitialized) narrative!!.layoutTag else DVLayout.DvLayoutTag

    fun currentDisplayText() = if (isInitialized) narrative!!.currentDisplayText() else "<no display text>"

    fun currentFontSize() = if (isInitialized) narrative!!.currentFontSize() else FontSize.TEXT

    fun seqNarrativeProgress() : Float = ((narrative?.currentIdx()?.plus(1))?.toFloat() ?: 0f) / (narrative?.narrativeBlocks?.size ?: 1)
    fun sequentialStatusKey() : String = "progress(${narrativeName().subSequence(0, 3)})"

    override fun initialize(initData: Any?) {

        if (initData != null) {
            val narrativeComponentInit = IComponent.checkInitType<NarrativeComponentInit>(initData)

            if (narrativeComponentInit != null) {

                //set up narrative
                narrative = narrativeComponentInit.narrative

                //add timers for each block
                narrative!!.narrativeBlocks.forEach { narrativeBlock ->
                    blockImmersionTimers[narrativeBlock.id] = ImmersionTimerPair(ImmersionTimer(), ImmersionTimer())
                }

                //set the narrative layout
                MessageChannelHandler.send(DisplayViewTextBridge, DisplayViewTextMessage(narrative!!.layoutTag))

                if (narrativeComponentInit.narrativeImmersion != null) {
                    narrativeImmersion = narrativeComponentInit.narrativeImmersion

                    narrative!!.init(narrativeImmersion!!.immersionBlockId())

                    //set the narrative cumulative timer
                    timerPair.cumlImmersionTimer.setPastStartTime(ImmersionTimer.inMilliseconds(narrativeComponentInit.cumlTime()))

                    //set block cumulative timers
                    narrativeComponentInit.blockImmersionTimers().entries.forEach { timerEntry ->
                        blockImmersionTimers[timerEntry.key]?.cumlImmersionTimer?.setPastStartTime(ImmersionTimer.inMilliseconds(timerEntry.value))
                    }
                } else {
                    narrativeImmersion = NarrativeImmersion(NarrativeImmersion.genId(narrativeComponentInit.profile.id, narrativeComponentInit.narrative.id)).apply {
                        this.location = ImmersionLocation(narrativeCurrBlockId(), cumlImmersionTime())
                        this.flags = mutableListOf()
                        this.blockImmersionTimers = blockImmersionTimersStr()
                    }
                    narrative!!.init()
                }

                // set current profile narrative id
                MessageChannelEnum.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UpdateImmersionId,
                    null, componentId()
                ))

                // clear statuses
                MessageChannelHandler.send(StatusViewBridge, StatusViewMessage(StatusViewMessage.StatusViewMessageType.ClearStatuses))

                super.initialize(initData)

                //start timers, enable message receipt
                activate(narrativeCurrBlockId())

                //update kcop with current settings, including setting log timers
                Switchboard.updateSettings(narrativeComponentInit.profile.settings)
            }
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean = this.messageHandler(msg)

    companion object {
        fun has(entity : Entity) : Boolean = entity.components.firstOrNull{ it is NarrativeComponent } != null
        fun getFor(entity : Entity) : NarrativeComponent? = if (has(entity)) entity.components.first { it is NarrativeComponent } as NarrativeComponent else null

        fun isValid(narrativeComponent: NarrativeComponent?) : Boolean {
            return (narrativeComponent?.narrative != null && narrativeComponent.narrativeImmersion != null && narrativeComponent.isInitialized)
        }

        fun ecsInit(profile: Profile, narrative: Narrative, narrativeImmersion: NarrativeImmersion? = null) {
            //inactivate current narrative
            MessageChannelHandler.send(NarrativeBridge, NarrativeMessage(NarrativeMessage.NarrativeMessageType.Inactivate))

            MessageChannelHandler.send(EngineComponentBridge, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.ReplaceComponent,
                SubjectEntity.entityName, NarrativeComponent::class.java,
                NarrativeComponentInit(profile, narrative, narrativeImmersion)
            ) )
        }
    }

    data class NarrativeComponentInit(val profile: Profile, val narrative: Narrative, val narrativeImmersion: NarrativeImmersion? = null) {
        fun blockImmersionTimers() = narrativeImmersion?.blockImmersionTimers ?: mapOf()
        fun cumlTime() = narrativeImmersion?.cumlImmersionTime() ?: ImmersionTimer.CumlTimeZero
    }

    // only need to replace in case of settings change
    //            MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(EngineComponentMessageType.REPLACE_COMPONENT, narrativeComponent.narrativeImmersionTimer.entityName, ImmersionTimerComponent::class.java, narrativeComponent.narrativeImmersionTimer))

}