package river.exertion.kcop.system.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.allOf
import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.NarrativeComponentEventHandler.currentBlockTimer
import river.exertion.kcop.system.ecs.component.NarrativeComponentEventHandler.currentText
import river.exertion.kcop.system.ecs.component.NarrativeComponentEventHandler.executeReadyBlockEvents
import river.exertion.kcop.system.ecs.component.NarrativeComponentEventHandler.executeReadyTimelineEvents
import river.exertion.kcop.view.messaging.DisplayViewTextMessage
import river.exertion.kcop.view.messaging.TextViewMessage

class NarrativeTextSystem : IntervalIteratingSystem(allOf(NarrativeComponent::class).get(), 1/10f) {

    override fun processEntity(entity: Entity) {

        val narrativeComponent = NarrativeComponent.getFor(entity)!!

        if (narrativeComponent.isInitialized) {

            narrativeComponent.executeReadyTimelineEvents()
            narrativeComponent.executeReadyBlockEvents()

            val currentText = narrativeComponent.currentText() + narrativeComponent.currentBlockTimer()

            if (narrativeComponent.changed) {
                MessageChannelEnum.DISPLAY_VIEW_TEXT_BRIDGE.send(null, DisplayViewTextMessage(narrativeComponent.layoutTag(), narrativeComponent.currentDisplayText(), narrativeComponent.currentFontSize()))
                NarrativeComponent.getFor(entity)!!.changed = false
            }

            MessageChannelEnum.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(TextViewMessage.TextViewMessageType.ReportText, currentText, narrativeComponent.currentPrompts()))
        }
    }
}