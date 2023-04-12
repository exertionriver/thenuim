package river.exertion.kcop.system.ecs.component

import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.*

object NarrativeComponentNavStatusHandler {

    fun NarrativeComponent.unpause() {
        if (isInitialized) {
            timerPair.cumlImmersionTimer.resumeTimer()
            timerPair.instImmersionTimer!!.resumeTimer()

            blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.resumeTimer()
            blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resumeTimer()
        }
    }

    fun NarrativeComponent.activate(setBlockId : String) {
        if (isInitialized) {

            narrative!!.currentBlockId = setBlockId

            timerPair.instImmersionTimer!!.resetTimer()
            blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.resetTimer()

            unpause()

            MessageChannel.NARRATIVE_BRIDGE.enableReceive(this)

            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.RefreshCurrentImmersion, null, this))

//            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.ADD_STATUS, sequentialStatusKey(), seqNarrativeProgress()))

            changed = true
        }
    }

    fun NarrativeComponent.pause() {
        if (isInitialized) {
            timerPair.cumlImmersionTimer.pauseTimer()
            timerPair.instImmersionTimer!!.pauseTimer()

            blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.pauseTimer()
            blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.pauseTimer()
        }
    }

    fun NarrativeComponent.inactivate() {
        if (isInitialized) {

            pause()

//            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.REMOVE_STATUS, sequentialStatusKey()))
            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(DisplayViewTextureMessageType.CLEAR_ALL))

            MessageChannel.NARRATIVE_BRIDGE.disableReceive(this)

            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.RemoveCurrentImmersion))

            isInitialized = false

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

//                MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.UPDATE_STATUS, sequentialStatusKey(), seqNarrativeProgress()))

                changed = true
            }

        }
    }
}