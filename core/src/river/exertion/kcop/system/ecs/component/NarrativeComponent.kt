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
    fun narrativeId() = if (narrative != null) narrative?.id!! else throw Exception("NarrativeComponent::narrativeId() narrative is null")

    val timerPair = ImmersionTimerPair(ImmersionTimer(), ImmersionTimer())
    var blockImmersionTimers : MutableMap<String, ImmersionTimerPair> = mutableMapOf()

    var flags : MutableList<ImmersionStatus> = mutableListOf()
    fun location() = ImmersionLocation(narrativeCurrBlockId(), cumlImmersionTime())

    var changed = false

    fun narrativeName() = narrative?.name ?: throw Exception("narrative name:$this narrative.name not set")
    fun cumlImmersionTime() = timerPair.cumlImmersionTimer.immersionTime()

    fun narrativeCurrBlockId() = narrative?.currentBlockId ?: throw Exception("narrativeCurrBlockId:$this narrative.currentBlockId not set")
    fun narrativePrevBlockId() = narrative?.previousBlockId ?: throw Exception("narrativePrevBlockId:$this narrative.previousBlockId not set")

//    fun seqNarrativeProgress() : Float = ((narrative?.currentIdx()?.plus(1))?.toFloat() ?: 0f) / (narrative?.narrativeBlocks?.size ?: 1)
//    fun sequentialStatusKey() : String = "progress(${narrativeName().subSequence(0, 3)})"

    override fun initialize(initData: Any?) {

        if (initData != null) {
            val narrativeComponentInit = IComponent.checkInitType<NarrativeComponentInit>(initData)

            if (narrativeComponentInit != null) {

                narrative = narrativeComponentInit.narrative()

                if (narrative != null) {

                    if (narrativeComponentInit.narrativeImmersionAsset != null) {
                        narrativeImmersion = narrativeComponentInit.narrativeImmersion()
                        narrative!!.init(narrativeImmersion!!.immersionBlockId())
                    } else {
                        narrative!!.init()
                    }

                    //set the narrative layout
                    MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.send(null, DisplayViewTextMessage(narrative!!.layoutTag))
                } else {
                    MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(TextViewCtrl.NoNarrativeLoaded))
                }

                //set the narrative cumulative timer
                timerPair.cumlImmersionTimer.setPastStartTime(ImmersionTimer.inMilliseconds(narrativeComponentInit.currentCumlTime()))

                //update current profile and statuses
                flags = narrativeComponentInit.flags().toMutableList()

                //add timers for each block
                narrative!!.narrativeBlocks.forEach { narrativeBlock ->
                    blockImmersionTimers[narrativeBlock.id] = ImmersionTimerPair(ImmersionTimer(), ImmersionTimer())
                }

                //when this timer component is added to the entity, it becomes the display timer for logCtrl
                MessageChannel.NARRATIVE_BRIDGE.enableReceive(this)

                super.initialize(initData)

                //start timers
                activate(narrativeCurrBlockId())

                MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(EngineComponentMessageType.ADD_COMPONENT, ProfileEntity.entityName, ImmersionTimerComponent::class.java, timerPair))

            }
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean {

        if (msg != null) {
            if (MessageChannel.NARRATIVE_BRIDGE.isType(msg.message) && isInitialized ) {
                val narrativeMessage: NarrativeMessage = MessageChannel.NARRATIVE_BRIDGE.receiveMessage(msg.extraInfo)

                when (narrativeMessage.narrativeMessageType) {
                    NarrativeMessage.NarrativeMessageType.PAUSE -> pause()
                    NarrativeMessage.NarrativeMessageType.UNPAUSE -> unpause()
                    NarrativeMessage.NarrativeMessageType.INACTIVATE -> inactivate()
                    NarrativeMessage.NarrativeMessageType.NEXT -> if (narrativeMessage.promptNext != null) next(narrativeMessage.promptNext)
                }
                return true
            }
        }
        return false
    }

    fun currentPrompts() = if (isInitialized) narrative!!.currentPrompts() else listOf()
    fun layoutTag() = if (isInitialized) narrative!!.layoutTag else DisplayViewCtrl.defaultLayoutTag
    fun currentDisplayText() = if (isInitialized) narrative!!.currentDisplayText() else "<no display text>"
    fun currentFontSize() = if (isInitialized) narrative!!.currentFontSize() else FontSize.TEXT

    companion object {
        fun has(entity : Entity) : Boolean = entity.components.firstOrNull{ it is NarrativeComponent } != null
        fun getFor(entity : Entity) : NarrativeComponent? = if (has(entity)) entity.components.first { it is NarrativeComponent } as NarrativeComponent else null
    }

    data class NarrativeComponentInit(val narrativeAsset: NarrativeAsset? = null, val narrativeImmersionAsset: NarrativeImmersionAsset? = null) {

        fun narrative() = narrativeAsset?.narrative
        fun narrativeImmersion() = narrativeImmersionAsset?.narrativeImmersion
        fun flags() = narrativeImmersionAsset?.flags() ?: listOf()
        fun currentCumlTime() = narrativeImmersionAsset?.narrativeImmersion?.cumlImmersionTime() ?: ImmersionTimer.zero()
    }

    // only need to replace in case of settings change
    //            MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(EngineComponentMessageType.REPLACE_COMPONENT, narrativeComponent.narrativeImmersionTimer.entityName, ImmersionTimerComponent::class.java, narrativeComponent.narrativeImmersionTimer))

}