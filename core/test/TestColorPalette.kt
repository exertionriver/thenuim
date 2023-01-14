import org.junit.jupiter.api.Test
import river.exertion.kcop.ColorPalette
import river.exertion.kcop.NarrativeSequence
import river.exertion.kcop.Util

class TestColorPalette {

    @Test
    fun testColorPalette() {
        println(ColorPalette.approxSettings(199, 21, 133)) //MediumVioletRed
        println(ColorPalette.approxSettings(250, 128, 114)) //Salmon
        println(ColorPalette.approxSettings(72, 61, 139)) //DarkSlateBlue
    }

}