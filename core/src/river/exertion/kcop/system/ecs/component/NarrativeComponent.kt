package river.exertion.kcop.system.ecs.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.narrative.structure.ImmersionLocation
import river.exertion.kcop.narrative.structure.ImmersionStatus
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.narrative.structure.NarrativeImmersion
import river.exertion.kcop.simulation.view.ctrl.DisplayViewCtrl
import river.exertion.kcop.system.ecs.component.NarrativeComponentMessageHandler.messageHandler
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.activate
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.*
import river.exertion.kcop.system.profile.Profile

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

    val timerPair = ImmersionTimerPair(ImmersionTimer(), ImmersionTimer())

    var blockImmersionTimers : MutableMap<String, ImmersionTimerPair> = mutableMapOf()

    fun blockImmersionTimersStr() = blockImmersionTimers.mapValues { it.value.cumlImmersionTimer.immersionTime() }.toMutableMap()

    fun cumlImmersionTime() = timerPair.cumlImmersionTimer.immersionTime()

    var changed = false

    fun currentPrompts() = if (isInitialized) narrative!!.currentPrompts() else listOf()

    fun layoutTag() = if (isInitialized) narrative!!.layoutTag else DisplayViewCtrl.defaultLayoutTag

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
                MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.send(null, DisplayViewTextMessage(narrative!!.layoutTag))

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
                MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UpdateImmersionId,
                    null, componentId()
                ))

                // clear statuses
                MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessage.StatusViewMessageType.ClearStatuses))

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
            MessageChannel.NARRATIVE_BRIDGE.send(null, NarrativeMessage(NarrativeMessage.NarrativeMessageType.Inactivate))

            MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.ReplaceComponent,
                ProfileEntity.entityName, NarrativeComponent::class.java,
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