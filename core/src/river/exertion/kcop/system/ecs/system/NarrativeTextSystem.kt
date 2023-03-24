package river.exertion.kcop.system.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.allOf
import river.exertion.kcop.narrative.structure.Event
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.NarrativeComponentEventHandler.executeReadyBlockEvents
import river.exertion.kcop.system.ecs.component.NarrativeComponentEventHandler.executeReadyTimelineEvents
import river.exertion.kcop.system.messaging.messages.*

class NarrativeTextSystem : IntervalIteratingSystem(allOf(NarrativeComponent::class).get(), 1/10f) {

    //TO DO: split into two systems, one for changed, one for timeline
    override fun processEntity(entity: Entity) {

        val narrativeComponent = NarrativeComponent.getFor(entity)!!

        if ( (narrativeComponent.narrative != null) && narrativeComponent.isActive) {

//            var timelineChanged = false

//            val blockInstTime = narrativeComponent.blockImmersionTimers[narrativeComponent.narrativeCurrBlockId()]?.instImmersionTimer?.immersionTime()
//            val blockCuml = narrativeComponent.blockImmersionTimers[narrativeComponent.narrativeCurrBlockId()]?.cumlImmersionTimer
            var eventText = ""

            eventText += narrativeComponent.executeReadyTimelineEvents()

//            if (!eventText.isBlank()) timelineChanged = true

            eventText += narrativeComponent.executeReadyBlockEvents()

//            if (narrativeComponent.changed || timelineChanged) {


                MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.send(null, TextViewMessage("", narrativeComponent.narrative!!.currentDisplayText(), narrativeComponent.narrative!!.currentFontSize(), narrativeComponent.narrative!!.currentPrompts(), narrativeComponent.narrative!!.layoutTag))

//                NarrativeComponent.getFor(entity)!!.changed = false
 //           }


            MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(eventText, "", narrativeComponent.narrative!!.currentFontSize(), narrativeComponent.narrative!!.currentPrompts(), narrativeComponent.narrative!!.id))

        }
    }
}