package river.exertion.kcop.sim.narrative.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.allOf
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.component.NarrativeComponent
import river.exertion.kcop.sim.narrative.component.NarrativeComponentEventHandler.currentBlockTimer
import river.exertion.kcop.sim.narrative.component.NarrativeComponentEventHandler.currentText
import river.exertion.kcop.sim.narrative.component.NarrativeComponentEventHandler.executeReadyBlockEvents
import river.exertion.kcop.sim.narrative.component.NarrativeComponentEventHandler.executeReadyTimelineEvents
import river.exertion.kcop.view.ViewPackage.DisplayViewTextBridge
import river.exertion.kcop.view.ViewPackage.TextViewBridge
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
                MessageChannelHandler.send(DisplayViewTextBridge, DisplayViewTextMessage(narrativeComponent.layoutTag(), narrativeComponent.currentDisplayText(), narrativeComponent.currentFontSize()))
                NarrativeComponent.getFor(entity)!!.changed = false
            }

            MessageChannelHandler.send(TextViewBridge, TextViewMessage(TextViewMessage.TextViewMessageType.ReportText, currentText, narrativeComponent.currentPrompts()))
        }
    }
}