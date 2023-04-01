package river.exertion.kcop.system.ecs.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.activate
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.inactivate
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.next
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.pause
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.unpause
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

    val narrativeImmersionTimer = ImmersionTimerComponent().apply { this.isInitialized = true }
    var blockImmersionTimers : MutableMap<String, ImmersionTimerComponent> = mutableMapOf()
    var flags : MutableList<String> = mutableListOf()

    var isActive = false //ie., is the current simulation
    var changed = false

    fun narrativeName() = narrative?.name ?: throw Exception("narrative name:$this narrative.name not set")

    fun narrativeCurrBlockId() = narrative?.currentBlockId ?: throw Exception("narrativeCurrBlockId:$this narrative.currentBlockId not set")
    fun narrativePrevBlockId() = narrative?.previousBlockId ?: throw Exception("narrativePrevBlockId:$this narrative.previousBlockId not set")

    fun seqNarrativeProgress() : Float = ((narrative?.currentIdx()?.plus(1))?.toFloat() ?: 0f) / (narrative?.narrativeBlocks?.size ?: 1)
    fun sequentialStatusKey() : String = "progress(${narrativeName().subSequence(0, 3)})"

    override fun initialize(entityName: String, initData: Any?) {

        if (initData != null) {
            val narrativeComponentInit = IComponent.checkInitType<NarrativeComponentInit>(initData)

            if (narrativeComponentInit != null) {
                super.initialize(entityName, initData)

                narrative = narrativeComponentInit.narrativeAsset.narrative
                narrative!!.init(narrativeComponentInit.currentBlockId)

                //set the narrative layout
                MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.send(null, DisplayViewTextMessage(narrative!!.layoutTag))

                //set the narrative cumulative timer
                narrativeImmersionTimer.cumlImmersionTimer.setPastStartTime(ImmersionTimer.inMilliseconds(narrativeComponentInit.currentCumlTimer))

                //add timers for each block
                narrative!!.narrativeBlocks.forEach { narrativeBlock ->
                    blockImmersionTimers[narrativeBlock.id] = ImmersionTimerComponent().apply { this.isInitialized = true }
                }

                //load current profile
                MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UPDATE_IMMERSION, narrativeComponentInit.narrativeAsset.narrativeAssetName(), narrativeComponentInit.currentBlockId))
                MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UPDATE_STATUS, narrativeComponentInit.narrativeAsset.narrativeAssetName(), "progress", seqNarrativeProgress().toString()))

                //start timers
                activate(narrativeCurrBlockId())

                //when this timer component is added to the entity, it becomes the display timer for logCtrl
                MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(EngineComponentMessageType.ADD_COMPONENT, entityName, ImmersionTimerComponent::class.java, narrativeImmersionTimer))
            }
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean {

        if (msg != null) {
            if (MessageChannel.NARRATIVE_BRIDGE.isType(msg.message) && isActive) {
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

    companion object {
        fun has(entity : Entity) : Boolean = entity.components.firstOrNull{ it is NarrativeComponent } != null
        fun getFor(entity : Entity) : NarrativeComponent? = if (has(entity)) entity.components.first { it is NarrativeComponent } as NarrativeComponent else null
    }

    data class NarrativeComponentInit(val narrativeAsset: NarrativeAsset, val currentBlockId: String? = null, private val nullableCurrentCumlTimer: String? = null) {
        val currentCumlTimer: String
            get() = nullableCurrentCumlTimer ?: "00:00:00"
    }

    // only need to replace in case of settings change
    //            MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(EngineComponentMessageType.REPLACE_COMPONENT, narrativeComponent.narrativeImmersionTimer.entityName, ImmersionTimerComponent::class.java, narrativeComponent.narrativeImmersionTimer))

}