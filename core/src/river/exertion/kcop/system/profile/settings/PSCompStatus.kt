package river.exertion.kcop.system.profile.settings

import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.system.messaging.messages.NarrativeStatusMessage
import river.exertion.kcop.system.profile.PSOption
import river.exertion.kcop.system.profile.PSSelection

object PSCompStatus : PSSelection {

    override val selectionKey = "completionStatus"

    override val selectionLabel = "Show Completion Status"

    override val options = listOf (
        PSOption("Show", PSCompStatusOptions.Show.tag()),
        PSOption("Hide", PSCompStatusOptions.Hide.tag())
    )

    enum class PSCompStatusOptions {
        Show { override fun tag() = "show"
            override fun exec() {
                MessageChannelEnum.NARRATIVE_STATUS_BRIDGE.send(null, NarrativeStatusMessage(NarrativeStatusMessage.NarrativeFlagsMessageType.AddStatus))
            }
        },
        Hide { override fun tag() = "hide"
            override fun exec() {
                MessageChannelEnum.NARRATIVE_STATUS_BRIDGE.send(null, NarrativeStatusMessage(NarrativeStatusMessage.NarrativeFlagsMessageType.RemoveStatus))
            }
        }
        ;
        abstract fun tag() : String
        abstract fun exec()

        companion object {
            fun byTag(tag : String) : PSCompStatusOptions? = values().firstOrNull { it.tag() == tag }
        }
    }
}