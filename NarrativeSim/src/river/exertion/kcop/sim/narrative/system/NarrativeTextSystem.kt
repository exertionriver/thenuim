package river.exertion.kcop.sim.narrative.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.allOf
import river.exertion.kcop.sim.narrative.NarrativePackage
import river.exertion.kcop.sim.narrative.component.NarrativeComponent
import river.exertion.kcop.sim.narrative.component.NarrativeComponentEventHandler.currentBlockTimer
import river.exertion.kcop.sim.narrative.component.NarrativeComponentEventHandler.currentText
import river.exertion.kcop.sim.narrative.component.NarrativeComponentEventHandler.executeReadyBlockEvents
import river.exertion.kcop.sim.narrative.component.NarrativeComponentEventHandler.executeReadyTimelineEvents
import river.exertion.kcop.sim.narrative.view.DVLayoutHandler
import river.exertion.kcop.view.layout.DisplayView
import river.exertion.kcop.view.layout.TextView

class NarrativeTextSystem : IntervalIteratingSystem(allOf(NarrativeComponent::class).get(), 1/10f) {

    override fun processEntity(entity: Entity) {

        val narrativeComponent = NarrativeComponent.getFor(entity)!!

        if (narrativeComponent.isInitialized) {

            if (narrativeComponent.changed) {
                //in case this was reset, e.g. by colorPalette
                DisplayView.currentDisplayViewLayoutHandler = NarrativePackage.displayViewLayoutHandler()
                DVLayoutHandler.currentDvLayout = NarrativePackage.dvLayoutByTag(narrativeComponent.layoutTag())
            }

            narrativeComponent.executeReadyTimelineEvents()
            narrativeComponent.executeReadyBlockEvents()

            val currentText = narrativeComponent.currentText() + narrativeComponent.currentBlockTimer()

            if (narrativeComponent.changed) {
                DVLayoutHandler.currentText = narrativeComponent.currentDisplayText()
                DVLayoutHandler.currentFontSize = narrativeComponent.currentFontSize()
                DisplayView.build()

                NarrativeComponent.getFor(entity)!!.changed = false
            }

            TextView.currentText = currentText
            TextView.currentPrompts = narrativeComponent.currentPrompts()
            TextView.build()
        }
    }
}