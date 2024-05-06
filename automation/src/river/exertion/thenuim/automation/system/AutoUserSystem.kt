package river.exertion.thenuim.automation.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import ktx.ashley.allOf
import ktx.graphics.takeScreenshot
import river.exertion.thenuim.asset.irlTime.IrlTime
import river.exertion.thenuim.automation.component.AutoUserComponent
import river.exertion.thenuim.view.layout.LogView

class AutoUserSystem : IteratingSystem(allOf(AutoUserComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime : Float) {

        river.exertion.thenuim.automation.AutoUserTestHandler.execTestBehaviors()

        var testableBehaviorEntry = river.exertion.thenuim.automation.AutoUserTestHandler.currentTest.logBehaviors.entries.firstOrNull { it.value == river.exertion.thenuim.automation.AutoUserTest.TestResult.READY }
        var testableTask = testableBehaviorEntry?.key?.readyTasks?.firstOrNull()

        while ( (testableBehaviorEntry != null) && (testableTask == null) ) {
            river.exertion.thenuim.automation.AutoUserTestHandler.currentTest.logBehaviors[testableBehaviorEntry.key] = testableBehaviorEntry.key.getResults()

            testableBehaviorEntry = river.exertion.thenuim.automation.AutoUserTestHandler.currentTest.logBehaviors.entries.firstOrNull { it.value == river.exertion.thenuim.automation.AutoUserTest.TestResult.READY }
            testableTask = testableBehaviorEntry?.key?.readyTasks?.firstOrNull()
        }

        //execTasks
        if (testableBehaviorEntry != null)  {
            if (testableBehaviorEntry.key.completedTasks.isEmpty()) LogView.addLog("beginning ${testableBehaviorEntry.key.name}")

            if ( testableTask!!.allowScreenshot() )
            {
                val screenshotFilename = "${testableBehaviorEntry.key.id}_${testableBehaviorEntry.key.completedTasks.size}_${testableTask.id}_${IrlTime.localTimeMillis("_")}.png"
                takeScreenshot(Gdx.files.local(screenshotFilename))
            }

            testableTask.executeTask()

            testableBehaviorEntry.key.completedTasks[testableTask] = river.exertion.thenuim.automation.AutoUserTest.TestResult.COMPLETED
            testableBehaviorEntry.key.readyTasks.removeFirst()

            if (testableBehaviorEntry.key.readyTasks.isEmpty()) {
                river.exertion.thenuim.automation.AutoUserTestHandler.currentTest.logBehaviors[testableBehaviorEntry.key] = testableBehaviorEntry.key.getResults()
                LogView.addLog("ending ${testableBehaviorEntry.key.name}")
                LogView.addLog("result: ${testableBehaviorEntry.value.name}")
            }
        }
    }
}