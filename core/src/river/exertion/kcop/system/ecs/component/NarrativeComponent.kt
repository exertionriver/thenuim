package river.exertion.kcop.system.ecs.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.assets.NarrativeImmersionAsset
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.narrative.structure.NarrativeImmersion
import river.exertion.kcop.simulation.view.ctrl.DisplayViewCtrl
import river.exertion.kcop.simulation.view.ctrl.TextViewCtrl
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.activate
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.inactivate
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.next
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.pause
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.unpause
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.*

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

//    fun seqNarrativeProgress() : Float = ((narrative?.currentIdx()?.plus(1))?.toFloat() ?: 0f) / (narrative?.narrativeBlocks?.size ?: 1)
//    fun sequentialStatusKey() : String = "progress(${narrativeName().subSequence(0, 3)})"

    override fun initialize(initData: Any?) {

        if (initData != null) {
            val narrativeComponentInit = IComponent.checkInitType<NarrativeComponentInit>(initData)

            if (narrativeComponentInit != null) {

                narrative = narrativeComponentInit.narrative()

                if (narrative != null) {

                    //add timers for each block
                    narrative!!.narrativeBlocks.forEach { narrativeBlock ->
                        blockImmersionTimers[narrativeBlock.id] = ImmersionTimerPair(ImmersionTimer(), ImmersionTimer())
                    }

                    // set current profile narrative id
                    MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UpdateImmersionId,
                        null, componentId()
                    ))

                    if (narrativeComponentInit.narrativeImmersionAsset != null) {
                        narrativeImmersion = narrativeComponentInit.narrativeImmersion()

                        narrative!!.init(narrativeImmersion!!.immersionBlockId())

                        //set the narrative cumulative timer
                        timerPair.cumlImmersionTimer.setPastStartTime(ImmersionTimer.inMilliseconds(narrativeComponentInit.cumlTime()))

                        //set block cumulative timers
                        narrativeComponentInit.immersionTimers().entries.forEach { timerEntry ->
                            blockImmersionTimers[timerEntry.key]?.cumlImmersionTimer?.setPastStartTime(ImmersionTimer.inMilliseconds(timerEntry.value))
                        }
                    } else {
                        narrativeImmersion = null
                        narrative!!.init()
                    }

                    //set the narrative layout
                    MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.send(null, DisplayViewTextMessage(narrative!!.layoutTag))
                } else {
                    narrativeImmersion = null
                    MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(TextViewCtrl.NoNarrativeLoaded))
                }

                super.initialize(initData)

                //start timers
                activate(narrativeCurrBlockId())

                //when this timer component is added to the entity, it becomes the display timer for logCtrl
                MessageChannel.NARRATIVE_BRIDGE.enableReceive(this)
                MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.RefreshCurrentImmersion, null, this))

                MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(EngineComponentMessageType.ADD_COMPONENT, ProfileEntity.entityName, ImmersionTimerComponent::class.java, timerPair))
            }
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean {

        if (msg != null) {
            if (MessageChannel.NARRATIVE_BRIDGE.isType(msg.message) && isInitialized ) {
                val narrativeMessage: NarrativeMessage = MessageChannel.NARRATIVE_BRIDGE.receiveMessage(msg.extraInfo)

                when (narrativeMessage.narrativeMessageType) {
                    NarrativeMessage.NarrativeMessageType.UpdateNarrativeImmersion -> {
                        if (narrativeMessage.narrativeImmersion != null) {
                            narrativeImmersion = narrativeMessage.narrativeImmersion
                        }
                    }
                    NarrativeMessage.NarrativeMessageType.ReplaceCumlTimer -> {
                        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                                EngineComponentMessageType.REPLACE_COMPONENT,
                                ProfileEntity.entityName, ImmersionTimerComponent::class.java, this.timerPair))
                    }
                    NarrativeMessage.NarrativeMessageType.Pause -> pause()
                    NarrativeMessage.NarrativeMessageType.Unpause -> unpause()
                    NarrativeMessage.NarrativeMessageType.Inactivate -> inactivate()
                    NarrativeMessage.NarrativeMessageType.Next -> if (narrativeMessage.promptNext != null) next(narrativeMessage.promptNext)
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

    data class NarrativeComponentInit(val narrativeAsset: NarrativeAsset? = null, val narrativeImmersionAsset: NarrativeImmersionAsset? = null) {

        fun narrative() = narrativeAsset?.narrative
        fun narrativeImmersion() = narrativeImmersionAsset?.narrativeImmersion
        fun immersionTimers() = narrativeImmersionAsset?.timers() ?: mapOf()
        fun cumlTime() = narrativeImmersionAsset?.narrativeImmersion?.cumlImmersionTime() ?: ImmersionTimer.CumlTimeZero
    }

    // only need to replace in case of settings change
    //            MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(EngineComponentMessageType.REPLACE_COMPONENT, narrativeComponent.narrativeImmersionTimer.entityName, ImmersionTimerComponent::class.java, narrativeComponent.narrativeImmersionTimer))

}