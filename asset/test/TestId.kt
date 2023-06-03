import org.junit.jupiter.api.Test
import river.exertion.kcop.asset.Id

class TestId {

    @Test
    fun testIds() {

        val idRegex = "[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}"

        (0..9).forEach {
            val randomId = Id.randomId()
            println("id:$randomId")

            assert(randomId.length == 36)
            assert(idRegex.toRegex().matches(randomId))
        }
    }
}