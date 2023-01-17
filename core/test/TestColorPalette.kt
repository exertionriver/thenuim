import org.junit.jupiter.api.Test
import river.exertion.kcop.ColorPalette
import river.exertion.kcop.NarrativeSequence
import river.exertion.kcop.Util
import kotlin.math.pow
import kotlin.test.assertEquals

class TestColorPalette {

    @Test
    fun testColorPaletteSize() {
        val cpCalcSize = ColorPalette.colorThresholdsInt.size.toFloat().pow(3).toInt()
        val cpMeasureSize = ColorPalette.values().size
        println("calc cp size :$cpCalcSize")
        println("measure cp size :$cpMeasureSize")

        assertEquals(cpCalcSize, cpMeasureSize)
    }

    @Test
    fun testColorPalette() {
        println("MediumVioletRed :" + ColorPalette.approxSettings(199, 21, 133))
        println("DeepPink :" + ColorPalette.approxSettings(255, 20, 147))
        println("PaleVioletRed :" + ColorPalette.approxSettings(219, 112, 147))
        println("HotPink :" + ColorPalette.approxSettings(255, 105, 180))
        println("LightPink :" + ColorPalette.approxSettings(255, 182, 193))
        println("Pink :" + ColorPalette.approxSettings(255, 192, 203))

        println("DarkRed :" + ColorPalette.approxSettings(139, 0, 0))
        println("Red :" + ColorPalette.approxSettings(255, 0, 0))
        println("Firebrick :" + ColorPalette.approxSettings(178, 34, 34))
        println("Crimson :" + ColorPalette.approxSettings(220, 20, 60))
        println("IndianRed :" + ColorPalette.approxSettings(205, 92, 92))
        println("LightCoral :" + ColorPalette.approxSettings(240, 128, 128))
        println("Salmon :" + ColorPalette.approxSettings(250, 128, 114))
        println("DarkSalmon :" + ColorPalette.approxSettings(233, 150, 122))
        println("LightSalmon :" + ColorPalette.approxSettings(255, 160, 122))

        println("OrangeRed :" + ColorPalette.approxSettings(255, 69, 0))
        println("Tomato :" + ColorPalette.approxSettings(255, 99, 71))
        println("DarkOrange :" + ColorPalette.approxSettings(255, 140, 0))
        println("Coral :" + ColorPalette.approxSettings(255, 127, 80))
        println("Orange :" + ColorPalette.approxSettings(255, 165, 0))

        println("DarkKhaki :" + ColorPalette.approxSettings(189, 183, 107))
        println("Gold :" + ColorPalette.approxSettings(255, 215, 0))
        println("Khaki :" + ColorPalette.approxSettings(240, 230, 140))
        println("PeachPuff :" + ColorPalette.approxSettings(255, 218, 185))
        println("Yellow :" + ColorPalette.approxSettings(255, 255, 0))
        println("PaleGoldenrod :" + ColorPalette.approxSettings(238, 232, 170))
        println("Moccasin :" + ColorPalette.approxSettings(255, 228, 181))
        println("PapayaWhip :" + ColorPalette.approxSettings(255, 239, 213))
        println("LightGoldenrodYellow :" + ColorPalette.approxSettings(250, 250, 210))
        println("LemonChiffon :" + ColorPalette.approxSettings(255, 250, 205))
        println("LightYellow :" + ColorPalette.approxSettings(255, 255, 224))

        println("Maroon :" + ColorPalette.approxSettings(128, 0, 0))
        println("Brown :" + ColorPalette.approxSettings(165, 42, 42))
        println("SaddleBrown :" + ColorPalette.approxSettings(139, 69, 19))
        println("Sienna :" + ColorPalette.approxSettings(160, 82, 45))
        println("Chocolate :" + ColorPalette.approxSettings(210, 105, 30))
        println("DarkGoldenrod :" + ColorPalette.approxSettings(184, 134, 11))
        println("Peru :" + ColorPalette.approxSettings(205, 133, 63))
        println("RosyBrown :" + ColorPalette.approxSettings(188, 143, 143))
        println("Goldenrod :" + ColorPalette.approxSettings(218, 165, 32))
        println("SandyBrown :" + ColorPalette.approxSettings(244, 164, 96))
        println("Tan :" + ColorPalette.approxSettings(210, 180, 140))
        println("Burlywood :" + ColorPalette.approxSettings(222, 184, 135))
        println("Wheat :" + ColorPalette.approxSettings(245, 222, 179))
        println("NavajoWhite :" + ColorPalette.approxSettings(255, 222, 173))
        println("Bisque :" + ColorPalette.approxSettings(255, 228, 196))
        println("BlanchedAlmond :" + ColorPalette.approxSettings(255, 235, 205))
        println("Cornsilk :" + ColorPalette.approxSettings(255, 248, 220))
        println("Taupe :" + ColorPalette.approxSettings(72, 60, 50)) //https://www.canva.com/colors/color-meanings/taupe/

        println("Indigo :" + ColorPalette.approxSettings(75, 0, 130))
        println("Purple :" + ColorPalette.approxSettings(128, 0, 128))
        println("DarkMagenta :" + ColorPalette.approxSettings(139, 0, 139))
        println("DarkViolet :" + ColorPalette.approxSettings(148, 0, 211))
        println("DarkSlateBlue :" + ColorPalette.approxSettings(72, 61, 139))
        println("BlueViolet :" + ColorPalette.approxSettings(138, 43, 226))
        println("DarkOrchid :" + ColorPalette.approxSettings(153, 50, 204))
        println("Fuchsia :" + ColorPalette.approxSettings(255, 0, 255))
        println("Magenta :" + ColorPalette.approxSettings(255, 0, 255))
        println("SlateBlue :" + ColorPalette.approxSettings(106, 90, 205))
        println("MediumSlateBlue :" + ColorPalette.approxSettings(123, 104, 238))
        println("MediumOrchid :" + ColorPalette.approxSettings(186, 85, 211))
        println("MediumPurple :" + ColorPalette.approxSettings(147, 112, 219))
        println("Orchid :" + ColorPalette.approxSettings(218, 112, 214))
        println("Violet :" + ColorPalette.approxSettings(238, 130, 238))
        println("Plum :" + ColorPalette.approxSettings(221, 160, 221))
        println("Thistle :" + ColorPalette.approxSettings(216, 191, 216))
        println("Lavender :" + ColorPalette.approxSettings(230, 230, 250))
        println("Mauve :" + ColorPalette.approxSettings(224, 176, 255)) //https://www.canva.com/colors/color-meanings/mauve/

        println("MidnightBlue :" + ColorPalette.approxSettings(25, 25, 112))
        println("Navy :" + ColorPalette.approxSettings(0, 0, 128))
        println("DarkBlue :" + ColorPalette.approxSettings(0, 0, 139))
        println("MediumBlue :" + ColorPalette.approxSettings(0, 0, 205))
        println("Blue :" + ColorPalette.approxSettings(0, 0, 255))
        println("RoyalBlue :" + ColorPalette.approxSettings(65, 105, 225))
        println("SteelBlue :" + ColorPalette.approxSettings(70, 130, 180))
        println("DodgerBlue :" + ColorPalette.approxSettings(30, 144, 255))
        println("DeepSkyBlue :" + ColorPalette.approxSettings(0, 191, 255))
        println("CornflowerBlue :" + ColorPalette.approxSettings(100, 149, 237))
        println("SkyBlue :" + ColorPalette.approxSettings(135, 206, 235))
        println("LightSkyBlue :" + ColorPalette.approxSettings(135, 206, 250))
        println("LightSteelBlue :" + ColorPalette.approxSettings(176, 196, 222))
        println("LightBlue :" + ColorPalette.approxSettings(173, 216, 230))
        println("PowderBlue :" + ColorPalette.approxSettings(176, 224, 230))

        println("Teal :" + ColorPalette.approxSettings(0, 128, 128))
        println("DarkCyan :" + ColorPalette.approxSettings(0, 139, 139))
        println("LightSeaGreen :" + ColorPalette.approxSettings(32, 178, 170))
        println("CadetBlue :" + ColorPalette.approxSettings(95, 158, 160))
        println("DarkTurquoise :" + ColorPalette.approxSettings(0, 206, 209))
        println("MediumTurquoise :" + ColorPalette.approxSettings(72, 209, 204))
        println("Turquoise :" + ColorPalette.approxSettings(64, 224, 208))
        println("Aqua :" + ColorPalette.approxSettings(0, 255, 255))
        println("Cyan :" + ColorPalette.approxSettings(0, 255, 255))
        println("Aquamarine :" + ColorPalette.approxSettings(127, 255, 212))
        println("PaleTurquoise :" + ColorPalette.approxSettings(175, 238, 238))
        println("LightCyan :" + ColorPalette.approxSettings(224, 255, 255))

        println("DarkGreen :" + ColorPalette.approxSettings(0, 100, 0))
        println("Green :" + ColorPalette.approxSettings(0, 128, 0))
        println("DarkOliveGreen :" + ColorPalette.approxSettings(85, 107, 47))
        println("ForestGreen :" + ColorPalette.approxSettings(34, 139, 34))
        println("SeaGreen :" + ColorPalette.approxSettings(46, 139, 87))
        println("Olive :" + ColorPalette.approxSettings(128, 128, 0))
        println("OliveDrab :" + ColorPalette.approxSettings(107, 142, 35))
        println("MediumSeaGreen :" + ColorPalette.approxSettings(60, 179, 113))
        println("LimeGreen :" + ColorPalette.approxSettings(50, 205, 50))
        println("Lime :" + ColorPalette.approxSettings(0, 255, 0))
        println("SpringGreen :" + ColorPalette.approxSettings(0, 255, 127))
        println("MediumSpringGreen :" + ColorPalette.approxSettings(0, 250, 154))
        println("DarkSeaGreen :" + ColorPalette.approxSettings(143, 188, 143))
        println("MediumAquamarine :" + ColorPalette.approxSettings(102, 205, 170))
        println("YellowGreen :" + ColorPalette.approxSettings(154, 205, 50))
        println("LawnGreen :" + ColorPalette.approxSettings(124, 252, 0))
        println("Chartreuse :" + ColorPalette.approxSettings(127, 255, 0))
        println("LightGreen :" + ColorPalette.approxSettings(144, 238, 144))
        println("GreenYellow :" + ColorPalette.approxSettings(173, 255, 47))
        println("PaleGreen :" + ColorPalette.approxSettings(152, 251, 152))

        println("MistyRose :" + ColorPalette.approxSettings(255, 228, 225))
        println("AntiqueWhite :" + ColorPalette.approxSettings(250, 235, 215))
        println("Linen :" + ColorPalette.approxSettings(250, 240, 230))
        println("Beige :" + ColorPalette.approxSettings(245, 245, 220))
        println("WhiteSmoke :" + ColorPalette.approxSettings(245, 245, 245))
        println("LavenderBlush :" + ColorPalette.approxSettings(255, 240, 245))
        println("OldLace :" + ColorPalette.approxSettings(253, 245, 230))
        println("AliceBlue :" + ColorPalette.approxSettings(240, 248, 255))
        println("Seashell :" + ColorPalette.approxSettings(255, 245, 238))
        println("GhostWhite :" + ColorPalette.approxSettings(248, 248, 255))
        println("Honeydew :" + ColorPalette.approxSettings(240, 255, 240))
        println("FloralWhite :" + ColorPalette.approxSettings(255, 250, 240))
        println("Azure :" + ColorPalette.approxSettings(240, 255, 255))
        println("MintCream :" + ColorPalette.approxSettings(245, 255, 250))
        println("Snow :" + ColorPalette.approxSettings(255, 250, 250))
        println("Ivory :" + ColorPalette.approxSettings(255, 255, 240))
        println("White :" + ColorPalette.approxSettings(255, 255, 255))

        println("Black :" + ColorPalette.approxSettings(0, 0, 0))
        println("DarkSlateGray :" + ColorPalette.approxSettings(47, 79, 79))
        println("DimGray :" + ColorPalette.approxSettings(105, 105, 105))
        println("SlateGray :" + ColorPalette.approxSettings(112, 128, 144))
        println("Gray :" + ColorPalette.approxSettings(128, 128, 128))
        println("LightSlateGray :" + ColorPalette.approxSettings(119, 136, 153))
        println("DarkGray :" + ColorPalette.approxSettings(169, 169, 169))
        println("Silver :" + ColorPalette.approxSettings(192, 192, 192))
        println("LightGray :" + ColorPalette.approxSettings(211, 211, 211))
        println("Gainsboro :" + ColorPalette.approxSettings(220, 220, 220))

    }
}