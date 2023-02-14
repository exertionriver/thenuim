package river.exertion.kcop.system.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.allOf
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.component.NarrativeComponent
import river.exertion.kcop.system.view.TextViewMessage

class NarrativeTextSystem : IntervalIteratingSystem(allOf(NarrativeComponent::class).get(), 1/10f) {

    override fun processEntity(entity: Entity) {

        val narrativeComponent = NarrativeComponent.getFor(entity)!!

        if (narrativeComponent.narrative != null) {
            MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(narrativeComponent.narrative!!.currentText(), narrativeComponent.narrative!!.currentPrompts(), narrativeComponent.narrative!!.id))
        }

//        SystemManager.logDebug(this.javaClass.name, "${this.interval} passed, timeActive: ${TimeComponent.getFor(entity)!!.timeActive()}, timeRender: ${TimeComponent.getFor(entity)!!.timeRender()}")
//        SystemManager.logDebug(this.javaClass.name, "instImmersionTime: ${immersionTimeComponent.instImmersionTime()}, cumlImmersionTime: ${immersionTimeComponent.cumlImmersionTime()}, localTime:${immersionTimeComponent.localTime()}" )
    }
}