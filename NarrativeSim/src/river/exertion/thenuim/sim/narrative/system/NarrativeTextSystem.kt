package river.exertion.thenuim.sim.narrative.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.allOf
import river.exertion.thenuim.sim.narrative.component.NarrativeComponent
import river.exertion.thenuim.sim.narrative.component.NarrativeComponentEventHandler.currentBlockTimer
import river.exertion.thenuim.sim.narrative.component.NarrativeComponentEventHandler.currentText
import river.exertion.thenuim.sim.narrative.component.NarrativeComponentEventHandler.executeReadyBlockEvents
import river.exertion.thenuim.sim.narrative.component.NarrativeComponentEventHandler.executeReadyTimelineEvents
import river.exertion.thenuim.view.layout.displayViewLayout.DVLayoutHandler
import river.exertion.thenuim.view.layout.TextView
import river.exertion.thenuim.view.layout.ViewLayout

class NarrativeTextSystem : IntervalIteratingSystem(allOf(NarrativeComponent::class).get(), 1/10f) {

    override fun processEntity(entity: Entity) {

        val narrativeComponent = NarrativeComponent.getFor(entity)!!

        if (narrativeComponent.isInitialized) {

            narrativeComponent.executeReadyTimelineEvents()
            narrativeComponent.executeReadyBlockEvents()

            val currentText = narrativeComponent.currentText() + narrativeComponent.currentBlockTimer()

            if (narrativeComponent.changed) {
                DVLayoutHandler.currentText = narrativeComponent.currentDisplayText()
                DVLayoutHandler.currentFontSize = narrativeComponent.currentFontSize()
                ViewLayout.rebuild()

                NarrativeComponent.getFor(entity)!!.changed = false
            }

            TextView.currentText = currentText
            TextView.currentPrompts = narrativeComponent.currentPrompts()
            TextView.build()
        }
    }
}