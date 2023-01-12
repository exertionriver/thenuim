import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.junit.jupiter.api.Test
import river.exertion.kcop.NarrativeSequenceBlock
import river.exertion.kcop.Util

class TestBlockLoad {

    @Test
    fun testBlockLoad() {
        val fileHandle = Util.internalFile("kcop/nsb_kcop.json")

        val rawFile = fileHandle.readString()
        println(rawFile)

        val json = Json { ignoreUnknownKeys = true }
        val narrativeSequenceBlocks : Array<NarrativeSequenceBlock> = json.decodeFromJsonElement(json.parseToJsonElement(rawFile))

        println(narrativeSequenceBlocks[0].id)
        println(narrativeSequenceBlocks[0].narrativeText)
        println(narrativeSequenceBlocks[0].sequenceId)
    }

}