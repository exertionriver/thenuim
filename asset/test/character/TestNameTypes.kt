package character

import org.junit.jupiter.api.Test
import river.exertion.thenuim.asset.character.NameTypes
import river.exertion.thenuim.base.TestBase
import river.exertion.thenuim.base.Log

class TestNameTypes : TestBase() {

    @Test
    fun testCelestialNameCreation() {

        (0..36).forEach { _ ->
            Log.test("celestial name (Osh)", NameTypes.CELESTIAL_OSH.capName())
        }
        (0..36).forEach { _ ->
            Log.test("celestial name (Ur)", NameTypes.CELESTIAL_UR.capName())
        }
        (0..36).forEach { _ ->
            Log.test("celestial name (La)", NameTypes.CELESTIAL_LA.capName())
        }
        (0..36).forEach { _ ->
            Log.test("celestial name (Di)", NameTypes.CELESTIAL_DI.capName())
        }
        (0..36).forEach { _ ->
            Log.test("below god name (Se)", NameTypes.CELESTIAL_SE.capName())
        }
    }

    @Test
    fun testNameCreation() {

        (0..36).forEach { idx ->

            Log.test("$idx")
            Log.test("continent name", NameTypes.CONTINENT.capName())
            Log.test("title name", NameTypes.TITLE.capName())
            Log.test("lower name", NameTypes.LOWER.capName())
            Log.test("common name", NameTypes.COMMON.capName())
            Log.test("land name", NameTypes.LAND.capName())
            Log.test("priest name", NameTypes.PRIEST.capName())
            Log.test("royal name", NameTypes.ROYAL.capName())

        }
    }
}