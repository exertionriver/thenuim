package river.exertion.kcop.system.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.allOf
import river.exertion.kcop.narrative.structure.Event
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.NarrativeComponentEventHandler.currentBlockTimer
import river.exertion.kcop.system.ecs.component.NarrativeComponentEventHandler.currentText
import river.exertion.kcop.system.ecs.component.NarrativeComponentEventHandler.executeReadyBlockEvents
import river.exertion.kcop.system.ecs.component.NarrativeComponentEventHandler.executeReadyTimelineEvents
import river.exertion.kcop.system.messaging.messages.*

class NarrativeTextSystem : IntervalIteratingSystem(allOf(NarrativeComponent::class).get(), 1/10f) {

    override fun processEntity(entity: Entity) {

        val narrativeComponent = NarrativeComponent.getFor(entity)!!

        if ( (narrativeComponent.narrative != null) && narrativeComponent.isActive) {

            var currentText = narrativeComponent.currentText()
            currentText += narrativeComponent.executeReadyTimelineEvents()
            currentText += narrativeComponent.executeReadyBlockEvents()
            currentText += narrativeComponent.currentBlockTimer()

            if (narrativeComponent.changed) {
                MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.send(null, DisplayViewTextMessage(narrativeComponent.narrative!!.layoutTag, narrativeComponent.narrative!!.currentDisplayText(), narrativeComponent.narrative!!.currentFontSize()))
                MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(currentText, narrativeComponent.narrative!!.currentPrompts(), narrativeComponent.narrative!!.id))
                NarrativeComponent.getFor(entity)!!.changed = false
            }
        }
    }
}