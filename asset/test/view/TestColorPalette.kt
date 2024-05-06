package view

import com.badlogic.gdx.graphics.Color
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import river.exertion.thenuim.asset.view.ColorPalette
import river.exertion.thenuim.base.Log
import river.exertion.thenuim.base.TestBase
import river.exertion.thenuim.base.str
import kotlin.math.pow

class TestColorPalette : TestBase() {

    @Test
    fun testColorPaletteSize() {
        val cpCalcSize = ColorPalette.colorThresholdsInt.size.toFloat().pow(3).toInt()
        val cpMeasureSize = ColorPalette.values().size
        Log.test("calc cp size", cpCalcSize.toString())
        Log.test("measure cp size", cpMeasureSize.toString())

        assertEquals(cpCalcSize, cpMeasureSize)
    }

    @Test
    fun testColorPaletteIdentity() {
        val testCp = ColorPalette.Color665
        Log.test("${testCp.name} tags", testCp.tags().str("\t"))
        assertEquals(4, testCp.tags().size)

        val testColor = Color(
                ColorPalette.colorThresholdsFloat[testCp.rSetting()],
                ColorPalette.colorThresholdsFloat[testCp.gSetting()],
                ColorPalette.colorThresholdsFloat[testCp.bSetting()],
                ColorPalette.defaultAlpha)
        assertEquals(testCp.color(), testColor)
    }

    @Test
    fun testColorPaletteTransform() {
        val testCpMid = ColorPalette.Color333
        Log.test("incr tests")
        Log.test("${testCpMid.name} tags", testCpMid.tags().str("\t"))

        Log.test("incr()", testCpMid.incr().tags().str("\t"))
        assertEquals(ColorPalette.Color444, testCpMid.incr())

        Log.test("incrR(1)", testCpMid.incrR(1).tags().str("\t"))
        assertEquals(ColorPalette.Color433, testCpMid.incrR(1))

        Log.test("incrG(2)", testCpMid.incrG(2).tags().str("\t"))
        assertEquals(ColorPalette.Color353, testCpMid.incrG(2))

        Log.test("incrB(3)", testCpMid.incrB(3).tags().str("\t"))
        assertEquals(ColorPalette.Color336, testCpMid.incrB(3))

        Log.test("incrB(20)", testCpMid.incrB(20).tags().str("\t"))
        assertEquals(ColorPalette.Color336, testCpMid.incrB(20))

        val testCpTop = ColorPalette.Color666
        Log.test("decr tests")
        Log.test("${testCpTop.name} tags", testCpTop.tags().str("\t"))

        Log.test("decr(3)", testCpTop.decr(3).tags().str("\t"))
        assertEquals(ColorPalette.Color333, testCpTop.decr(3))

        Log.test("decrR(4)", testCpTop.decrR(4).tags().str("\t"))
        assertEquals(ColorPalette.Color266, testCpTop.decrR(4))

        Log.test("decrG(5)", testCpTop.decrG(5).tags().str("\t"))
        assertEquals(ColorPalette.Color616, testCpTop.decrG(5))

        Log.test("decrB(6)", testCpTop.decrB(6).tags().str("\t"))
        assertEquals(ColorPalette.Color660, testCpTop.decrB(6))

        Log.test("decrB(30)", testCpTop.decrB(30).tags().str("\t"))
        assertEquals(ColorPalette.Color660, testCpTop.decrB(30))

        val testCpTrans = ColorPalette.Color246
        Log.test("label transform tests")
        Log.test("${testCpTrans.name} tags", testCpTrans.tags().str("\t"))

        Log.test("inv()", testCpTrans.inv().tags().str("\t"))
        assertEquals(ColorPalette.Color420, testCpTrans.inv())

        Log.test("cmp()", testCpTrans.comp().tags().str("\t"))
        assertEquals(ColorPalette.Color642, testCpTrans.comp())

        Log.test("triad()", "${testCpTrans.triad().first.tags().str("\t")}, ${testCpTrans.triad().second.tags().str("\t")}")
        assertEquals(ColorPalette.Color462, testCpTrans.triad().first)
        assertEquals(ColorPalette.Color624, testCpTrans.triad().second)

        Log.test("label()", testCpTrans.label().tags().str("\t"))
        assertEquals(ColorPalette.Color420, testCpTrans.label())
    }

    @Test
    fun testColorSpectrum() {
        val testCp = ColorPalette.Color135

        Log.test("spectrum tests")
        Log.test("${testCp.name} tags", testCp.tags().str("\t"))

        testCp.spectrum().forEachIndexed { idx, cp ->
            Log.test("$idx: ${cp.name} tags", cp.tags().str("\t"))
        }

        testCp.spectrum().also {
            assertEquals(ColorPalette.Color000, it[0])
            assertEquals(ColorPalette.Color001, it[1])
            assertEquals(ColorPalette.Color002, it[2])
            assertEquals(ColorPalette.Color013, it[3])
            assertEquals(ColorPalette.Color024, it[4])
            assertEquals(ColorPalette.Color135, it[5])
            assertEquals(ColorPalette.Color246, it[6])
            assertEquals(ColorPalette.Color356, it[7])
            assertEquals(ColorPalette.Color466, it[8])
            assertEquals(ColorPalette.Color566, it[9])
            assertEquals(ColorPalette.Color666, it[10])
        }

        val testCpLabelled = ColorPalette.Color356

        Log.test("labelled spectrum tests")
        Log.test(testCpLabelled.name, "tags: ${testCpLabelled.tags().str("\t")}")

        testCpLabelled.labelledSpectrum().entries.forEachIndexed { idx, cp ->
            Log.test("$idx: ${cp.value.name}", "label: ${cp.key} tags: ${cp.value.tags().str("\t")}")
        }

        assertEquals("skyBlue", testCpLabelled.labelledSpectrum().keys.toList()[6])
        assertEquals("lightSkyBlue", testCpLabelled.labelledSpectrum("lightSkyBlue").keys.toList()[6])
        assertEquals("skyBlue", testCpLabelled.labelledSpectrum("gibberish").keys.toList()[6])
        assertEquals("skyBlue", testCpLabelled.labelledSpectrum("paleTurquoise").keys.toList()[6])
    }

    @Test
    fun testOfApprox() {
        Log.test("approx ColorPalettes")
        Log.test("MediumVioletRed", "${ColorPalette.ofApprox(199, 21, 133).also { assertEquals(ColorPalette.Color503, it)}}")
        Log.test("DarkRed", "${ColorPalette.ofApprox(139, 0, 0).also { assertEquals(ColorPalette.Color300, it) }}")
        Log.test("OrangeRed", "${ColorPalette.ofApprox(255, 69, 0).also { assertEquals(ColorPalette.Color620, it) }}")
        Log.test("DarkKhaki", "${ColorPalette.ofApprox(189, 183, 107).also { assertEquals(ColorPalette.Color542, it) }}")
        Log.test("Maroon", "${ColorPalette.ofApprox(128, 0, 0).also { assertEquals(ColorPalette.Color300, it) }}")
        Log.test("Indigo", "${ColorPalette.ofApprox(75, 0, 130).also { assertEquals(ColorPalette.Color203, it) }}")
        Log.test("MidnightBlue", "${ColorPalette.ofApprox(25, 25, 112).also { assertEquals(ColorPalette.Color003, it) }}")
        Log.test("Teal", "${ColorPalette.ofApprox(0, 128, 128).also { assertEquals(ColorPalette.Color033, it) }}")
        Log.test("DarkGreen", "${ColorPalette.ofApprox(0, 100, 0).also { assertEquals(ColorPalette.Color020, it) }}")
        Log.test("MistyRose", "${ColorPalette.ofApprox(255, 228, 225).also { assertEquals(ColorPalette.Color655, it) }}")
        Log.test("Black", "${ColorPalette.ofApprox(0, 0, 0).also { assertEquals(ColorPalette.Color000, it) }}")
    }

    @Test
    fun testApproxSettings() {
        Log.test("approx ColorPalette r, g, b")
        Log.test("MediumVioletRed", "${ColorPalette.approxSettings(199, 21, 133).also { assertEquals(listOf(5, 0, 3), it) }}" )
        Log.test("DeepPink", "${ColorPalette.approxSettings(255, 20, 147).also { assertEquals(listOf(6, 0, 3), it) }}" )
        Log.test("PaleVioletRed", "${ColorPalette.approxSettings(219, 112, 147).also { assertEquals(listOf(5, 3, 3), it) }}" )
        Log.test("HotPink", "${ColorPalette.approxSettings(255, 105, 180).also { assertEquals(listOf(6, 2, 4), it) }}" )
        Log.test("LightPink", "${ColorPalette.approxSettings(255, 182, 193).also { assertEquals(listOf(6, 4, 5), it) }}" )
        Log.test("Pink", "${ColorPalette.approxSettings(255, 192, 203).also { assertEquals(listOf(6, 5, 5), it) }}" )

        Log.test("DarkRed", "${ColorPalette.approxSettings(139, 0, 0).also { assertEquals(listOf(3, 0, 0), it) }}")
        Log.test("Red", "${ColorPalette.approxSettings(255, 0, 0).also { assertEquals(listOf(6, 0, 0), it) }}")
        Log.test("Firebrick", "${ColorPalette.approxSettings(178, 34, 34).also { assertEquals(listOf(4, 1, 1), it) }}")
        Log.test("Crimson", "${ColorPalette.approxSettings(220, 20, 60).also { assertEquals(listOf(5, 0, 1), it) }}")
        Log.test("IndianRed", "${ColorPalette.approxSettings(205, 92, 92).also { assertEquals(listOf(5, 2, 2), it) }}")
        Log.test("LightCoral", "${ColorPalette.approxSettings(240, 128, 128).also { assertEquals(listOf(6, 3, 3), it) }}")
        Log.test("Salmon", "${ColorPalette.approxSettings(250, 128, 114).also { assertEquals(listOf(6, 3, 3), it) }}")
        Log.test("DarkSalmon", "${ColorPalette.approxSettings(233, 150, 122).also { assertEquals(listOf(6, 4, 3), it) }}")
        Log.test("LightSalmon", "${ColorPalette.approxSettings(255, 160, 122).also { assertEquals(listOf(6, 4, 3), it) }}")

        Log.test("OrangeRed", "${ColorPalette.approxSettings(255, 69, 0).also { assertEquals(listOf(6, 2, 0), it) }}")
        Log.test("Tomato", "${ColorPalette.approxSettings(255, 99, 71).also { assertEquals(listOf(6, 2, 2), it) }}")
        Log.test("DarkOrange", "${ColorPalette.approxSettings(255, 140, 0).also { assertEquals(listOf(6, 3, 0), it) }}")
        Log.test("Coral", "${ColorPalette.approxSettings(255, 127, 80).also { assertEquals(listOf(6, 3, 2), it) }}")
        Log.test("Orange", "${ColorPalette.approxSettings(255, 165, 0).also { assertEquals(listOf(6, 4, 0), it) }}")

        Log.test("DarkKhaki", "${ColorPalette.approxSettings(189, 183, 107).also { assertEquals(listOf(5, 4, 2), it) }}")
        Log.test("Gold", "${ColorPalette.approxSettings(255, 215, 0).also { assertEquals(listOf(6, 5, 0), it) }}")
        Log.test("Khaki", "${ColorPalette.approxSettings(240, 230, 140).also { assertEquals(listOf(6, 6, 3), it) }}")
        Log.test("PeachPuff", "${ColorPalette.approxSettings(255, 218, 185).also { assertEquals(listOf(6, 5, 4), it) }}")
        Log.test("Yellow", "${ColorPalette.approxSettings(255, 255, 0).also { assertEquals(listOf(6, 6, 0), it) }}")
        Log.test("PaleGoldenrod", "${ColorPalette.approxSettings(238, 232, 170).also { assertEquals(listOf(6, 6, 4), it) }}")
        Log.test("Moccasin", "${ColorPalette.approxSettings(255, 228, 181).also { assertEquals(listOf(6, 5, 4), it) }}")
        Log.test("PapayaWhip", "${ColorPalette.approxSettings(255, 239, 213).also { assertEquals(listOf(6, 6, 5), it) }}")
        Log.test("LightGoldenrodYellow", "${ColorPalette.approxSettings(250, 250, 210).also { assertEquals(listOf(6, 6, 5), it) }}")
        Log.test("LemonChiffon", "${ColorPalette.approxSettings(255, 250, 205).also { assertEquals(listOf(6, 6, 5), it) }}")
        Log.test("LightYellow", "${ColorPalette.approxSettings(255, 255, 224).also { assertEquals(listOf(6, 6, 5), it) }}")

        Log.test("Maroon", "${ColorPalette.approxSettings(128, 0, 0).also { assertEquals(listOf(3, 0, 0), it) }}")
        Log.test("Brown", "${ColorPalette.approxSettings(165, 42, 42).also { assertEquals(listOf(4, 1, 1), it) }}")
        Log.test("SaddleBrown", "${ColorPalette.approxSettings(139, 69, 19).also { assertEquals(listOf(3, 2, 0), it) }}")
        Log.test("Sienna", "${ColorPalette.approxSettings(160, 82, 45).also { assertEquals(listOf(4, 2, 1), it) }}")
        Log.test("Chocolate", "${ColorPalette.approxSettings(210, 105, 30).also { assertEquals(listOf(5, 2, 1), it) }}")
        Log.test("DarkGoldenrod", "${ColorPalette.approxSettings(184, 134, 11).also { assertEquals(listOf(4, 3, 0), it) }}")
        Log.test("Peru", "${ColorPalette.approxSettings(205, 133, 63).also { assertEquals(listOf(5, 3, 1), it) }}")
        Log.test("RosyBrown", "${ColorPalette.approxSettings(188, 143, 143).also { assertEquals(listOf(4, 3, 3), it) }}")
        Log.test("Goldenrod", "${ColorPalette.approxSettings(218, 165, 32).also { assertEquals(listOf(5, 4, 1), it) }}")
        Log.test("SandyBrown", "${ColorPalette.approxSettings(244, 164, 96).also { assertEquals(listOf(6, 4, 2), it) }}")
        Log.test("Tan", "${ColorPalette.approxSettings(210, 180, 140).also { assertEquals(listOf(5, 4, 3), it) }}")
        Log.test("Burlywood", "${ColorPalette.approxSettings(222, 184, 135).also { assertEquals(listOf(5, 4, 3), it) }}")
        Log.test("Wheat", "${ColorPalette.approxSettings(245, 222, 179).also { assertEquals(listOf(6, 5, 4), it) }}")
        Log.test("NavajoWhite", "${ColorPalette.approxSettings(255, 222, 173).also { assertEquals(listOf(6, 5, 4), it) }}")
        Log.test("Bisque", "${ColorPalette.approxSettings(255, 228, 196).also { assertEquals(listOf(6, 5, 5), it) }}")
        Log.test("BlanchedAlmond", "${ColorPalette.approxSettings(255, 235, 205).also { assertEquals(listOf(6, 6, 5), it) }}")
        Log.test("Cornsilk", "${ColorPalette.approxSettings(255, 248, 220).also { assertEquals(listOf(6, 6, 5), it) }}")
        //https://www.canva.com/colors/color-meanings/taupe/
        Log.test("Taupe", "${ColorPalette.approxSettings(72, 60, 50).also { assertEquals(listOf(2, 1, 1), it) }}")

        Log.test("Indigo", "${ColorPalette.approxSettings(75, 0, 130).also { assertEquals(listOf(2, 0, 3), it) }}")
        Log.test("Purple", "${ColorPalette.approxSettings(128, 0, 128).also { assertEquals(listOf(3, 0, 3), it) }}")
        Log.test("DarkMagenta", "${ColorPalette.approxSettings(139, 0, 139).also { assertEquals(listOf(3, 0, 3), it) }}")
        Log.test("DarkViolet", "${ColorPalette.approxSettings(148, 0, 211).also { assertEquals(listOf(3, 0, 5), it) }}")
        Log.test("DarkSlateBlue", "${ColorPalette.approxSettings(72, 61, 139).also { assertEquals(listOf(2, 1, 3), it) }}")
        Log.test("BlueViolet", "${ColorPalette.approxSettings(138, 43, 226).also { assertEquals(listOf(3, 1, 5), it) }}")
        Log.test("DarkOrchid", "${ColorPalette.approxSettings(153, 50, 204).also { assertEquals(listOf(4, 1, 5), it) }}")
        Log.test("Fuchsia", "${ColorPalette.approxSettings(255, 0, 255).also { assertEquals(listOf(6, 0, 6), it) }}")
        Log.test("Magenta", "${ColorPalette.approxSettings(255, 0, 255).also { assertEquals(listOf(6, 0, 6), it) }}")
        Log.test("SlateBlue", "${ColorPalette.approxSettings(106, 90, 205).also { assertEquals(listOf(2, 2, 5), it) }}")
        Log.test("MediumSlateBlue", "${ColorPalette.approxSettings(123, 104, 238).also { assertEquals(listOf(3, 2, 6), it) }}")
        Log.test("MediumOrchid", "${ColorPalette.approxSettings(186, 85, 211).also { assertEquals(listOf(4, 2, 5), it) }}")
        Log.test("MediumPurple", "${ColorPalette.approxSettings(147, 112, 219).also { assertEquals(listOf(3, 3, 5), it) }}")
        Log.test("Orchid", "${ColorPalette.approxSettings(218, 112, 214).also { assertEquals(listOf(5, 3, 5), it) }}")
        Log.test("Violet", "${ColorPalette.approxSettings(238, 130, 238).also { assertEquals(listOf(6, 3, 6), it) }}")
        Log.test("Plum", "${ColorPalette.approxSettings(221, 160, 221).also { assertEquals(listOf(5, 4, 5), it) }}")
        Log.test("Thistle", "${ColorPalette.approxSettings(216, 191, 216).also { assertEquals(listOf(5, 5, 5), it) }}")
        Log.test("Lavender", "${ColorPalette.approxSettings(230, 230, 250).also { assertEquals(listOf(6, 6, 6), it) }}")
        //https://www.canva.com/colors/color-meanings/mauve/
        Log.test("Mauve", "${ColorPalette.approxSettings(224, 176, 255).also { assertEquals(listOf(5, 4, 6), it) }}")

        Log.test("MidnightBlue", "${ColorPalette.approxSettings(25, 25, 112).also { assertEquals(listOf(0, 0, 3), it) }}")
        Log.test("Navy", "${ColorPalette.approxSettings(0, 0, 128).also { assertEquals(listOf(0, 0, 3), it) }}")
        Log.test("DarkBlue", "${ColorPalette.approxSettings(0, 0, 139).also { assertEquals(listOf(0, 0, 3), it) }}")
        Log.test("MediumBlue", "${ColorPalette.approxSettings(0, 0, 205).also { assertEquals(listOf(0, 0, 5), it) }}")
        Log.test("Blue", "${ColorPalette.approxSettings(0, 0, 255).also { assertEquals(listOf(0, 0, 6), it) }}")
        Log.test("RoyalBlue", "${ColorPalette.approxSettings(65, 105, 225).also { assertEquals(listOf(1, 2, 5), it) }}")
        Log.test("SteelBlue", "${ColorPalette.approxSettings(70, 130, 180).also { assertEquals(listOf(2, 3, 4), it) }}")
        Log.test("DodgerBlue", "${ColorPalette.approxSettings(30, 144, 255).also { assertEquals(listOf(1, 3, 6), it) }}")
        Log.test("DeepSkyBlue", "${ColorPalette.approxSettings(0, 191, 255).also { assertEquals(listOf(0, 5, 6), it) }}")
        Log.test("CornflowerBlue", "${ColorPalette.approxSettings(100, 149, 237).also { assertEquals(listOf(2, 4, 6), it) }}")
        Log.test("SkyBlue", "${ColorPalette.approxSettings(135, 206, 235).also { assertEquals(listOf(3, 5, 6), it) }}")
        Log.test("LightSkyBlue", "${ColorPalette.approxSettings(135, 206, 250).also { assertEquals(listOf(3, 5, 6), it) }}")
        Log.test("LightSteelBlue", "${ColorPalette.approxSettings(176, 196, 222).also { assertEquals(listOf(4, 5, 5), it) }}")
        Log.test("LightBlue", "${ColorPalette.approxSettings(173, 216, 230).also { assertEquals(listOf(4, 5, 6), it) }}")
        Log.test("PowderBlue", "${ColorPalette.approxSettings(176, 224, 230).also { assertEquals(listOf(4, 5, 6), it) }}")

        Log.test("Teal", "${ColorPalette.approxSettings(0, 128, 128).also { assertEquals(listOf(0, 3, 3), it) }}")
        Log.test("DarkCyan", "${ColorPalette.approxSettings(0, 139, 139).also { assertEquals(listOf(0, 3, 3), it) }}")
        Log.test("LightSeaGreen", "${ColorPalette.approxSettings(32, 178, 170).also { assertEquals(listOf(1, 4, 4), it) }}")
        Log.test("CadetBlue", "${ColorPalette.approxSettings(95, 158, 160).also { assertEquals(listOf(2, 4, 4), it) }}")
        Log.test("DarkTurquoise", "${ColorPalette.approxSettings(0, 206, 209).also { assertEquals(listOf(0, 5, 5), it) }}")
        Log.test("MediumTurquoise", "${ColorPalette.approxSettings(72, 209, 204).also { assertEquals(listOf(2, 5, 5), it) }}")
        Log.test("Turquoise", "${ColorPalette.approxSettings(64, 224, 208).also { assertEquals(listOf(1, 5, 5), it) }}")
        Log.test("Aqua", "${ColorPalette.approxSettings(0, 255, 255).also { assertEquals(listOf(0, 6, 6), it) }}")
        Log.test("Cyan", "${ColorPalette.approxSettings(0, 255, 255).also { assertEquals(listOf(0, 6, 6), it) }}")
        Log.test("Aquamarine", "${ColorPalette.approxSettings(127, 255, 212).also { assertEquals(listOf(3, 6, 5), it) }}")
        Log.test("PaleTurquoise", "${ColorPalette.approxSettings(175, 238, 238).also { assertEquals(listOf(4, 6, 6), it) }}")
        Log.test("LightCyan", "${ColorPalette.approxSettings(224, 255, 255).also { assertEquals(listOf(5, 6, 6), it) }}")

        Log.test("DarkGreen", "${ColorPalette.approxSettings(0, 100, 0).also { assertEquals(listOf(0, 2, 0), it) }}")
        Log.test("Green", "${ColorPalette.approxSettings(0, 128, 0).also { assertEquals(listOf(0, 3, 0), it) }}")
        Log.test("DarkOliveGreen", "${ColorPalette.approxSettings(85, 107, 47).also { assertEquals(listOf(2, 2, 1), it) }}")
        Log.test("ForestGreen", "${ColorPalette.approxSettings(34, 139, 34).also { assertEquals(listOf(1, 3, 1), it) }}")
        Log.test("SeaGreen", "${ColorPalette.approxSettings(46, 139, 87).also { assertEquals(listOf(1, 3, 2), it) }}")
        Log.test("Olive", "${ColorPalette.approxSettings(128, 128, 0).also { assertEquals(listOf(3, 3, 0), it) }}")
        Log.test("OliveDrab", "${ColorPalette.approxSettings(107, 142, 35).also { assertEquals(listOf(2, 3, 1), it) }}")
        Log.test("MediumSeaGreen", "${ColorPalette.approxSettings(60, 179, 113).also { assertEquals(listOf(1, 4, 3), it) }}")
        Log.test("LimeGreen", "${ColorPalette.approxSettings(50, 205, 50).also { assertEquals(listOf(1, 5, 1), it) }}")
        Log.test("Lime", "${ColorPalette.approxSettings(0, 255, 0).also { assertEquals(listOf(0, 6, 0), it) }}")
        Log.test("SpringGreen", "${ColorPalette.approxSettings(0, 255, 127).also { assertEquals(listOf(0, 6, 3), it) }}")
        Log.test("MediumSpringGreen", "${ColorPalette.approxSettings(0, 250, 154).also { assertEquals(listOf(0, 6, 4), it) }}")
        Log.test("DarkSeaGreen", "${ColorPalette.approxSettings(143, 188, 143).also { assertEquals(listOf(3, 4, 3), it) }}")
        Log.test("MediumAquamarine", "${ColorPalette.approxSettings(102, 205, 170).also { assertEquals(listOf(2, 5, 4), it) }}")
        Log.test("YellowGreen", "${ColorPalette.approxSettings(154, 205, 50).also { assertEquals(listOf(4, 5, 1), it) }}")
        Log.test("LawnGreen", "${ColorPalette.approxSettings(124, 252, 0).also { assertEquals(listOf(3, 6, 0), it) }}")
        Log.test("Chartreuse", "${ColorPalette.approxSettings(127, 255, 0).also { assertEquals(listOf(3, 6, 0), it) }}")
        Log.test("LightGreen", "${ColorPalette.approxSettings(144, 238, 144).also { assertEquals(listOf(3, 6, 3), it) }}")
        Log.test("GreenYellow", "${ColorPalette.approxSettings(173, 255, 47).also { assertEquals(listOf(4, 6, 1), it) }}")
        Log.test("PaleGreen", "${ColorPalette.approxSettings(152, 251, 152).also { assertEquals(listOf(4, 6, 4), it) }}")

        Log.test("MistyRose", "${ColorPalette.approxSettings(255, 228, 225).also { assertEquals(listOf(6, 5, 5), it) }}")
        Log.test("AntiqueWhite", "${ColorPalette.approxSettings(250, 235, 215).also { assertEquals(listOf(6, 6, 5), it) }}")
        Log.test("Linen", "${ColorPalette.approxSettings(250, 240, 230).also { assertEquals(listOf(6, 6, 6), it) }}")
        Log.test("Beige", "${ColorPalette.approxSettings(245, 245, 220).also { assertEquals(listOf(6, 6, 5), it) }}")
        Log.test("WhiteSmoke", "${ColorPalette.approxSettings(245, 245, 245).also { assertEquals(listOf(6, 6, 6), it) }}")
        Log.test("LavenderBlush", "${ColorPalette.approxSettings(255, 240, 245).also { assertEquals(listOf(6, 6, 6), it) }}")
        Log.test("OldLace", "${ColorPalette.approxSettings(253, 245, 230).also { assertEquals(listOf(6, 6, 6), it) }}")
        Log.test("AliceBlue", "${ColorPalette.approxSettings(240, 248, 255).also { assertEquals(listOf(6, 6, 6), it) }}")
        Log.test("Seashell", "${ColorPalette.approxSettings(255, 245, 238).also { assertEquals(listOf(6, 6, 6), it) }}")
        Log.test("GhostWhite", "${ColorPalette.approxSettings(248, 248, 255).also { assertEquals(listOf(6, 6, 6), it) }}")
        Log.test("Honeydew", "${ColorPalette.approxSettings(240, 255, 240).also { assertEquals(listOf(6, 6, 6), it) }}")
        Log.test("FloralWhite", "${ColorPalette.approxSettings(255, 250, 240).also { assertEquals(listOf(6, 6, 6), it) }}")
        Log.test("Azure", "${ColorPalette.approxSettings(240, 255, 255).also { assertEquals(listOf(6, 6, 6), it) }}")
        Log.test("MintCream", "${ColorPalette.approxSettings(245, 255, 250).also { assertEquals(listOf(6, 6, 6), it) }}")
        Log.test("Snow", "${ColorPalette.approxSettings(255, 250, 250).also { assertEquals(listOf(6, 6, 6), it) }}")
        Log.test("Ivory", "${ColorPalette.approxSettings(255, 255, 240).also { assertEquals(listOf(6, 6, 6), it) }}")
        Log.test("White", "${ColorPalette.approxSettings(255, 255, 255).also { assertEquals(listOf(6, 6, 6), it) }}")

        Log.test("Black", "${ColorPalette.approxSettings(0, 0, 0).also { assertEquals(listOf(0, 0, 0), it) }}")
        Log.test("DarkSlateGray", "${ColorPalette.approxSettings(47, 79, 79).also { assertEquals(listOf(1, 2, 2), it) }}")
        Log.test("DimGray", "${ColorPalette.approxSettings(105, 105, 105).also { assertEquals(listOf(2, 2, 2), it) }}")
        Log.test("SlateGray", "${ColorPalette.approxSettings(112, 128, 144).also { assertEquals(listOf(3, 3, 3), it) }}")
        Log.test("Gray", "${ColorPalette.approxSettings(128, 128, 128).also { assertEquals(listOf(3, 3, 3), it) }}")
        Log.test("LightSlateGray", "${ColorPalette.approxSettings(119, 136, 153).also { assertEquals(listOf(3, 3, 4), it) }}")
        Log.test("DarkGray", "${ColorPalette.approxSettings(169, 169, 169).also { assertEquals(listOf(4, 4, 4), it) }}")
        Log.test("Silver", "${ColorPalette.approxSettings(192, 192, 192).also { assertEquals(listOf(5, 5, 5), it) }}")
        Log.test("LightGray", "${ColorPalette.approxSettings(211, 211, 211).also { assertEquals(listOf(5, 5, 5), it) }}")
        Log.test("Gainsboro", "${ColorPalette.approxSettings(220, 220, 220).also { assertEquals(listOf(5, 5, 5), it) }}")

    }
}