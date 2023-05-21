package river.exertion.kcop.sim.narrative.component

import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.NarrativePackage
import river.exertion.kcop.sim.narrative.messaging.NarrativeComponentMessage.Companion.NarrativeBridge
import river.exertion.kcop.view.layout.AiView
import river.exertion.kcop.view.layout.StatusView

object NarrativeComponentNavStatusHandler {

    fun NarrativeComponent.unpause() {
        if (isInitialized) {
            cumlImmersionTimer.resumeTimer()
            instImmersionTimer.resumeTimer()

            blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.resumeTimer()
            blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resumeTimer()
        }
    }

    fun NarrativeComponent.activate(setBlockId : String) {
        if (isInitialized) {

            narrative.currentBlockId = setBlockId

            instImmersionTimer.resetTimer()
            blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resetTimer()

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

            blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.pauseTimer()
            blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.pauseTimer()
        }
    }

    fun NarrativeComponent.inactivate() {
        if (isInitialized) {

            pause()

            NarrativePackage.clearContent()

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
                blockImmersionTimers[narrativePrevBlockId()]?.cumlImmersionTimer?.pauseTimer()
                blockImmersionTimers[narrativePrevBlockId()]?.instImmersionTimer?.pauseTimer()

                blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.resumeTimer()

                blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resetTimer()
                blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resumeTimer()

                StatusView.addOrUpdateStatus(sequentialStatusKey(), seqNarrativeProgress())
                AiView.clearHints()

                narrativeState.blockFlags.clear()
                changed = true
            }

        }
    }
}