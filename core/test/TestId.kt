import org.junit.jupiter.api.Test
import river.exertion.kcop.Id

class TestId {

    @Test
    fun testIds() {

        (0..9).forEach {
            println("id:${Id.randomId()}")
        }

    }
}