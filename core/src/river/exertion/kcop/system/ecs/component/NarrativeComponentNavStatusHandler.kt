package river.exertion.kcop.system.ecs.component

import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.next
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.*

object NarrativeComponentNavStatusHandler {

    fun NarrativeComponent.unpause() {
        if (isInitialized) {
            narrativeImmersionTimer.cumlImmersionTimer.resumeTimer()
            narrativeImmersionTimer.instImmersionTimer.resumeTimer()

            blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.resumeTimer()
            blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resumeTimer()
        }
    }

    fun NarrativeComponent.activate(setBlockId : String) {
        if (isInitialized) {
            isActive = true

            narrative!!.currentBlockId = setBlockId

            narrativeImmersionTimer.instImmersionTimer.resetTimer()
            blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resetTimer()

            unpause()

            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.ADD_STATUS, sequentialStatusKey(), seqNarrativeProgress()))
            Switchboard.updateProfile(this)

            changed = true
        }
    }

    fun NarrativeComponent.pause() {
        if (isInitialized) {
            narrativeImmersionTimer.cumlImmersionTimer.pauseTimer()
            narrativeImmersionTimer.instImmersionTimer.pauseTimer()

            blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.pauseTimer()
            blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.pauseTimer()
        }
    }

    fun NarrativeComponent.inactivate() {
        if (isInitialized) {
            isActive = false

            pause()

            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.REMOVE_STATUS, sequentialStatusKey()))
            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(DisplayViewTextureMessageType.CLEAR_ALL))

            changed = true
        }
    }

    //active is assumed
    fun NarrativeComponent.next(keypress : String) {
        if (isInitialized) {
            val possiblePrevBlockId = narrativeCurrBlockId()
            narrative!!.next(keypress)

            //switch timers to new block
            if (possiblePrevBlockId != narrativeCurrBlockId()) {
                blockImmersionTimers[narrativePrevBlockId()]?.cumlImmersionTimer?.pauseTimer()
                blockImmersionTimers[narrativePrevBlockId()]?.instImmersionTimer?.pauseTimer()

                blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.resumeTimer()

                blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resetTimer()
                blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resumeTimer()

                MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.UPDATE_STATUS, sequentialStatusKey(), seqNarrativeProgress()))
                Switchboard.updateProfile(this)

                changed = true
            }

        }
    }
}