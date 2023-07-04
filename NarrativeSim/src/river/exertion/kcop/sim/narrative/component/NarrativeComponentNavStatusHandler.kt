package river.exertion.kcop.sim.narrative.component

import river.exertion.kcop.ecs.component.ImmersionTimerComponent
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.asset.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.sim.narrative.NarrativeKlop
import river.exertion.kcop.sim.narrative.NarrativeKlop.NarrativeBridge
import river.exertion.kcop.view.layout.AiView

object NarrativeComponentNavStatusHandler {

    fun NarrativeComponent.unpause() {
        if (isInitialized) {
            cumlImmersionTimer.startOrResumeTimer()
            instImmersionTimer.startOrResumeTimer()

            blockCumlImmersionTimers[narrativeCurrBlockId()]?.startOrResumeTimer()
            blockInstImmersionTimers[narrativeCurrBlockId()]?.startOrResumeTimer()
        }
    }

    fun NarrativeComponent.activate(setBlockId : String) {
        if (isInitialized) {

            narrative.currentBlockId = setBlockId

            instImmersionTimer.resetTimer()
            blockInstImmersionTimers[narrativeCurrBlockId()]?.resetTimer()

            unpause()

            MessageChannelHandler.enableReceive(NarrativeBridge, this)

            narrativeState.blockFlags.clear()
            changed = true
        }
    }

    fun NarrativeComponent.pause() {
        if (isInitialized) {
            cumlImmersionTimer.pauseTimer()
            instImmersionTimer.pauseTimer()

            blockCumlImmersionTimers[narrativeCurrBlockId()]?.pauseTimer()
            blockInstImmersionTimers[narrativeCurrBlockId()]?.pauseTimer()
        }
    }

    fun NarrativeComponent.inactivate() {
        if (isInitialized) {

            pause()

            NarrativeKlop.displayViewLayoutHandler().clearContent()

            MessageChannelHandler.disableReceive(NarrativeBridge, this)

            AiView.clearHints()

            isInitialized = false

            changed = true
        }
    }

    //active is assumed
    fun NarrativeComponent.next(keypress : String) {
        if (isInitialized) {
            val possiblePrevBlockId = narrativeCurrBlockId()
            narrative.next(keypress)

            //switch timers to new block
            if (possiblePrevBlockId != narrativeCurrBlockId()) {
                blockCumlImmersionTimers[narrativePrevBlockId()]?.pauseTimer()
                blockInstImmersionTimers[narrativePrevBlockId()]?.pauseTimer()

                blockCumlImmersionTimers[narrativeCurrBlockId()]?.startOrResumeTimer()

                blockInstImmersionTimers[narrativeCurrBlockId()]?.resetTimer()
                blockInstImmersionTimers[narrativeCurrBlockId()]?.startOrResumeTimer()

                addOrUpdateCompletionStatus()
                AiView.clearHints()

                location.immersionBlockId = narrativeCurrBlockId()

                narrativeState.blockFlags.clear()
                changed = true

                if (blockCumlTimer)
                    ImmersionTimerComponent.ecsInit(initData = ImmersionTimerPair(blockInstImmersionTimer(), blockCumlImmersionTimer()))
            }

        }
    }
}