package river.exertion.kcop.system.ecs.component

import river.exertion.kcop.narrative.structure.events.HintTextEvent
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.inactivate
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
            MessageChannel.NARRATIVE_STATUS_BRIDGE.enableReceive(this)
            MessageChannel.NARRATIVE_FLAGS_BRIDGE.enableReceive(this)
            MessageChannel.NARRATIVE_MEDIA_BRIDGE.enableReceive(this)

            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.RefreshCurrentImmersion, null, this))

            blockFlags.clear()
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

            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(DisplayViewTextureMessage.DisplayViewTextureMessageType.ClearAll))

            MessageChannel.NARRATIVE_BRIDGE.disableReceive(this)
            MessageChannel.NARRATIVE_STATUS_BRIDGE.disableReceive(this)
            MessageChannel.NARRATIVE_FLAGS_BRIDGE.disableReceive(this)
            MessageChannel.NARRATIVE_MEDIA_BRIDGE.disableReceive(this)

            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.RemoveCurrentImmersion))
            MessageChannel.AI_VIEW_BRIDGE.send(null, AiHintMessage(AiHintMessage.AiHintMessageType.ClearHints))
//            MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessage.StatusViewMessageType.ClearStatuses))

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

                MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessage.StatusViewMessageType.UpdateStatus, sequentialStatusKey(), seqNarrativeProgress()))
                MessageChannel.AI_VIEW_BRIDGE.send(null, AiHintMessage(AiHintMessage.AiHintMessageType.ClearHints))

                blockFlags.clear()
                changed = true
            }

        }
    }
}