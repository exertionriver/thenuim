package river.exertion.kcop.system.ecs.component

import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.system.messaging.messages.*
import river.exertion.kcop.view.messaging.AiHintMessage
import river.exertion.kcop.view.messaging.DisplayViewTextureMessage
import river.exertion.kcop.view.messaging.StatusViewMessage

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

            MessageChannelEnum.NARRATIVE_BRIDGE.enableReceive(this)
            MessageChannelEnum.NARRATIVE_STATUS_BRIDGE.enableReceive(this)
            MessageChannelEnum.NARRATIVE_FLAGS_BRIDGE.enableReceive(this)
            MessageChannelEnum.NARRATIVE_MEDIA_BRIDGE.enableReceive(this)

            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.RefreshCurrentImmersion, null, this))

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

            MessageChannelEnum.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                DisplayViewTextureMessage.DisplayViewTextureMessageType.ClearAll)
            )

            MessageChannelEnum.NARRATIVE_BRIDGE.disableReceive(this)
            MessageChannelEnum.NARRATIVE_STATUS_BRIDGE.disableReceive(this)
            MessageChannelEnum.NARRATIVE_FLAGS_BRIDGE.disableReceive(this)
            MessageChannelEnum.NARRATIVE_MEDIA_BRIDGE.disableReceive(this)

            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.RemoveCurrentImmersion))
            MessageChannelEnum.AI_VIEW_BRIDGE.send(null, AiHintMessage(AiHintMessage.AiHintMessageType.ClearHints))
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

                MessageChannelEnum.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessage.StatusViewMessageType.UpdateStatus, sequentialStatusKey(), seqNarrativeProgress()))
                MessageChannelEnum.AI_VIEW_BRIDGE.send(null, AiHintMessage(AiHintMessage.AiHintMessageType.ClearHints))

                blockFlags.clear()
                changed = true
            }

        }
    }
}