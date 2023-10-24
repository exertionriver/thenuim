package river.exertion.kcop.automation.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import ktx.ashley.allOf
import ktx.graphics.takeScreenshot
import river.exertion.kcop.asset.irlTime.IrlTime
import river.exertion.kcop.automation.AutoUserTest
import river.exertion.kcop.automation.AutoUserTestHandler
import river.exertion.kcop.automation.component.AutoUserComponent
import river.exertion.kcop.view.layout.LogView

class AutoUserSystem : IteratingSystem(allOf(AutoUserComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime : Float) {

        AutoUserTestHandler.execTestBehaviors()

        var testableBehaviorEntry = AutoUserTestHandler.currentTest.logBehaviors.entries.firstOrNull { it.value == AutoUserTest.TestResult.READY }
        var testableTask = testableBehaviorEntry?.key?.readyTasks?.firstOrNull()

        while ( (testableBehaviorEntry != null) && (testableTask == null) ) {
            AutoUserTestHandler.currentTest.logBehaviors[testableBehaviorEntry.key] = testableBehaviorEntry.key.getResults()

            testableBehaviorEntry = AutoUserTestHandler.currentTest.logBehaviors.entries.firstOrNull { it.value == AutoUserTest.TestResult.READY }
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

            testableBehaviorEntry.key.completedTasks[testableTask] = AutoUserTest.TestResult.COMPLETED
            testableBehaviorEntry.key.readyTasks.removeFirst()

            if (testableBehaviorEntry.key.readyTasks.isEmpty()) {
                AutoUserTestHandler.currentTest.logBehaviors[testableBehaviorEntry.key] = testableBehaviorEntry.key.getResults()
                LogView.addLog("ending ${testableBehaviorEntry.key.name}")
                LogView.addLog("result: ${testableBehaviorEntry.value.name}")
            }
        }
    }
}