package river.exertion.kcop.narrative.structure

import com.badlogic.gdx.Input
import kotlinx.serialization.Serializable
import river.exertion.kcop.Id

@Serializable
data class Narrative(
    override var id: String,
    val narrativeBlocks : MutableList<NarrativeBlock> = mutableListOf(),
    val promptBlocks : MutableList<PromptBlock> = mutableListOf(),
    val eventBlocks : MutableList<EventBlock> = mutableListOf(),
    val timelineEvents : MutableList<Event> = mutableListOf()
) : Id() {
    var currentId = ""

    fun init() {
        currentId = narrativeBlocks.firstOrNull()?.id ?: currentId
    }

    fun currentIdx() = narrativeBlocks.indexOf(narrativeBlocks.firstOrNull { it.id == currentId } ?: narrativeBlocks[0])

    fun seqPrevIdx() = (currentIdx() - 1).coerceAtLeast(0)

    fun seqNextIdx() = (currentIdx() + 1).coerceAtMost(narrativeBlocks.size - 1)

    fun isSequential() : Boolean = promptBlocks.isEmpty()

    fun currentText() = narrativeBlocks.firstOrNull { it.id == currentId }?.toTextString() ?: "<narrativeText not found for '$id' at '$currentId'>"

    fun currentPrompts() : List<String> =
        if (isSequential()) {
            listOf("(↑) Prev", "(↓) Next")
        } else {
            promptBlocks.firstOrNull { it.narrativeBlockId == currentId }?.prompts?.map { it.promptText } ?: listOf("<narrativePrompts not found for '$id' at '$currentId')>")
        }

    fun currentEventBlock() = eventBlocks.firstOrNull { it.narrativeBlockId == currentId }

    fun next(promptKey : String) {
        currentId = if (isSequential()) {
            when (promptKey) {
                Input.Keys.toString(Input.Keys.UP) -> narrativeBlocks[seqPrevIdx()].id
                Input.Keys.toString(Input.Keys.DOWN) -> narrativeBlocks[seqNextIdx()].id
                else -> currentId
            }
        } else {
            promptBlocks.firstOrNull { it.narrativeBlockId == currentId }?.prompts?.firstOrNull { it.promptKey.toString() == promptKey }?.promptNextId ?: currentId
        }
    }

    fun prev() { currentId = narrativeBlocks[seqPrevIdx()].id }

    fun next() { currentId = narrativeBlocks[seqNextIdx()].id }
}