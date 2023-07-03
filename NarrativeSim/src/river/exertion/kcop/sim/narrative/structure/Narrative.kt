package river.exertion.kcop.sim.narrative.structure

import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Texture
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import river.exertion.kcop.base.Id
import river.exertion.kcop.sim.narrative.structure.events.Event
import river.exertion.kcop.sim.narrative.view.DVLayout
import river.exertion.kcop.view.KcopFont

@Serializable
data class Narrative(
    var id: String = Id.randomId(),
    val name : String = "",
    val description : String = "",
    val layoutTag : String = DVLayout.DvLayoutTag,
    val narrativeBlocks : MutableList<NarrativeBlock> = mutableListOf(),
    val promptBlocks : MutableList<PromptBlock> = mutableListOf(),
    val eventBlocks : MutableList<EventBlock> = mutableListOf(),
    val timelineEvents : MutableList<Event> = mutableListOf(),
    ) {

    @Transient
    var currentBlockId = ""

    @Transient
    var previousBlockId = ""

    @Transient
    var textures : MutableMap<String, Texture> = mutableMapOf()

    @Transient
    var sounds : MutableMap<String, Music> = mutableMapOf()

    @Transient
    var music : MutableMap<String, Music> = mutableMapOf()


    fun init(blockIdStart : String? = null) {
        currentBlockId = narrativeBlocks.firstOrNull { it.id == blockIdStart }?.id ?: firstBlock().id
        previousBlockId = currentBlockId
    }

    fun firstBlock() = narrativeBlocks[0]

    fun currentBlock() = narrativeBlocks.firstOrNull { it.id == currentBlockId }

    fun previousBlock() = narrativeBlocks.firstOrNull { it.id == previousBlockId }

    fun currentIdx() = narrativeBlocks.indexOf(currentBlock() ?: narrativeBlocks[0])

    fun previousIdx() = narrativeBlocks.indexOf(previousBlock() ?: narrativeBlocks[0])

    fun seqPrevIdx() = (currentIdx() - 1).coerceAtLeast(0)

    fun seqNextIdx() = (currentIdx() + 1).coerceAtMost(narrativeBlocks.size - 1)

    fun isSequential() : Boolean = promptBlocks.isEmpty()

    fun currentText() = currentBlock()?.toTextString() ?: ""

    fun currentDisplayText() = currentBlock()?.toDisplayString() ?: ""

    fun currentFontSize() = KcopFont.byTag(currentBlock()?.fontSize)

    fun currentPrompts() : List<String> =
        if (isSequential()) {
            listOf("(↑) Prev", "(↓) Next")
        } else {
            promptBlocks.firstOrNull { it.narrativeBlockId == currentBlockId }?.prompts?.map { it.promptText } ?: listOf("<narrativePrompts not found for '$name' at '$currentBlockId')>")
        }

    fun previousEventBlock() = eventBlocks.firstOrNull { it.narrativeBlockId == previousBlockId }
    fun currentEventBlock() = eventBlocks.firstOrNull { it.narrativeBlockId == currentBlockId }

    fun next(promptKey : String) {
        val possiblePreviousId = currentBlockId

        currentBlockId = if (isSequential()) {
            when (promptKey) {
                Input.Keys.toString(Input.Keys.UP) -> narrativeBlocks[seqPrevIdx()].id
                Input.Keys.toString(Input.Keys.DOWN) -> narrativeBlocks[seqNextIdx()].id
                else -> currentBlockId
            }
        } else {
            val currentPrompts = promptBlocks.firstOrNull { it.narrativeBlockId == currentBlockId }?.prompts?.firstOrNull { it.promptKey.toString() == promptKey }
            val nextId = currentPrompts?.promptNextId
            val randNextId = currentPrompts?.promptRandomId?.random()

            nextId ?: randNextId ?: currentBlockId
        }

        if (currentBlockId != possiblePreviousId) previousBlockId = possiblePreviousId
    }

    fun prev() {
        val possiblePreviousId = currentBlockId
        currentBlockId = narrativeBlocks[seqPrevIdx()].id
        if (currentBlockId != possiblePreviousId) previousBlockId = possiblePreviousId
    }

    fun next() {
        val possiblePreviousId = currentBlockId
        currentBlockId = narrativeBlocks[seqNextIdx()].id
        if (currentBlockId != possiblePreviousId) previousBlockId = possiblePreviousId
    }


    fun narrativeInfo() : List<String> {
        val returnList = mutableListOf<String>()

        returnList.add("name: $name")
        returnList.add("description: $description")
        returnList.add("blocks: ${narrativeBlocks.size}")

        return returnList.toList()
    }
}