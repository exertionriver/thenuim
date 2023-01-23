import org.junit.jupiter.api.Test
import river.exertion.kcop.narrative.character.NameTypes

class TestNameCreation {

    @Test
    fun testCelestialNameCreation() {

        (0..36).forEach { _ ->
            println("celestial name (Osh): ${NameTypes.CELESTIAL_OSH.capName()}")
        }
        (0..36).forEach { _ ->
            println("celestial name (Ur): ${NameTypes.CELESTIAL_UR.capName()}")
        }
        (0..36).forEach { _ ->
            println("celestial name (La): ${NameTypes.CELESTIAL_LA.capName()}")
        }
        (0..36).forEach { _ ->
            println("celestial name (Di): ${NameTypes.CELESTIAL_DI.capName()}")
        }
        (0..36).forEach { _ ->
            println("below god name (Se): ${NameTypes.CELESTIAL_SE.capName()}")
        }
    }

    @Test
    fun testNameCreation() {

        (0..36).forEach { idx ->

            println("$idx")
            println("continent name: ${NameTypes.CONTINENT.capName()}")
            println("title name: ${NameTypes.TITLE.capName()}")
            println("lower name: ${NameTypes.LOWER.capName()}")
            println("common name: ${NameTypes.COMMON.capName()}")
            println("land name: ${NameTypes.LAND.capName()}")
            println("priest name: ${NameTypes.PRIEST.capName()}")
            println("royal name: ${NameTypes.ROYAL.capName()}")

        }
    }
}