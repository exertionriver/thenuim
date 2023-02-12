import org.junit.jupiter.api.Test
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.Util

class TestBlockLoad {

    @Test
    fun testBlockLoad() {
        val narrative = Util.loader<Narrative>(Util.internalFile("kcop/nsb_kcop.json"))

        if (narrative != null) {
            println(narrative.id)
            println(narrative.narrativeBlocks[0].narrativeText)
            println(narrative.narrativeBlocks[1].narrativeText)

            narrative.init()
            println(narrative.currentText())
            narrative.next()
            println(narrative.currentText())
            narrative.next()
            println(narrative.currentText())
            narrative.prev()
            println(narrative.currentText())
            narrative.prev()
            println(narrative.currentText())

        }
    }

}