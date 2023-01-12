import org.junit.jupiter.api.Test
import river.exertion.kcop.NarrativeSequence
import river.exertion.kcop.Util

class TestBlockLoad {

    @Test
    fun testBlockLoad() {
        val narrativeSequence = Util.loader<NarrativeSequence>(Util.internalFile("kcop/nsb_kcop.json"))

        if (narrativeSequence != null) {
            println(narrativeSequence.id)
            println(narrativeSequence.sequenceBlocks[0].narrativeText)
            println(narrativeSequence.sequenceBlocks[1].narrativeText)
        }
    }
}