package river.exertion.kcop.sim.narrative.component

import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.messaging.NarrativeFlagsMessage.Companion.NarrativeFlagsBridge
import river.exertion.kcop.sim.narrative.messaging.NarrativeMediaMessage.Companion.NarrativeMediaBridge
import river.exertion.kcop.sim.narrative.messaging.NarrativeMessage.Companion.NarrativeBridge
import river.exertion.kcop.sim.narrative.messaging.NarrativeStatusMessage.Companion.NarrativeStatusBridge
import river.exertion.kcop.view.messaging.AiHintMessage
import river.exertion.kcop.view.messaging.AiHintMessage.Companion.AiHintBridge
import river.exertion.kcop.view.messaging.DisplayViewTextureMessage
import river.exertion.kcop.view.messaging.DisplayViewTextureMessage.Companion.DisplayViewTextureBridge
import river.exertion.kcop.view.messaging.StatusViewMessage
import river.exertion.kcop.view.messaging.StatusViewMessage.Companion.StatusViewBridge

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

            MessageChannelHandler.enableReceive(NarrativeBridge, this)
            MessageChannelHandler.enableReceive(NarrativeStatusBridge,this)
            MessageChannelHandler.enableReceive(NarrativeFlagsBridge,this)
            MessageChannelHandler.enableReceive(NarrativeMediaBridge,this)

//            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.RefreshCurrentImmersion, null, this))

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

            MessageChannelHandler.send(DisplayViewTextureBridge, DisplayViewTextureMessage(
                DisplayViewTextureMessage.DisplayViewTextureMessageType.ClearAll)
            )

            MessageChannelHandler.disableReceive(NarrativeBridge, this)
            MessageChannelHandler.disableReceive(NarrativeStatusBridge,this)
            MessageChannelHandler.disableReceive(NarrativeFlagsBridge,this)
            MessageChannelHandler.disableReceive(NarrativeMediaBridge,this)


//            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.RemoveCurrentImmersion))
            MessageChannelHandler.send(AiHintBridge, AiHintMessage(AiHintMessage.AiHintMessageType.ClearHints))
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

                MessageChannelHandler.send(StatusViewBridge, StatusViewMessage(StatusViewMessage.StatusViewMessageType.UpdateStatus, sequentialStatusKey(), seqNarrativeProgress()))
                MessageChannelHandler.send(AiHintBridge, AiHintMessage(AiHintMessage.AiHintMessageType.ClearHints))

                blockFlags.clear()
                changed = true
            }

        }
    }
}