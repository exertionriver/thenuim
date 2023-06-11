package view

import com.badlogic.gdx.graphics.Color
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import river.exertion.kcop.asset.view.ColorPalette
import kotlin.math.pow

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
    fun testColorPaletteIdentity() {
        val testCp = ColorPalette.Color665
        println("${testCp.name} tags: ${testCp.tags()}")
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
        println("incr tests")
        println("${testCpMid.name} tags: ${testCpMid.tags()}")

        println("incr() : ${testCpMid.incr().tags()}")
        assertEquals(ColorPalette.Color444, testCpMid.incr())

        println("incrR(1) : ${testCpMid.incrR(1).tags()}")
        assertEquals(ColorPalette.Color433, testCpMid.incrR(1))

        println("incrG(2) : ${testCpMid.incrG(2).tags()}")
        assertEquals(ColorPalette.Color353, testCpMid.incrG(2))

        println("incrB(3) : ${testCpMid.incrB(3).tags()}")
        assertEquals(ColorPalette.Color336, testCpMid.incrB(3))

        println("incrB(20) : ${testCpMid.incrB(20).tags()}")
        assertEquals(ColorPalette.Color336, testCpMid.incrB(20))

        val testCpTop = ColorPalette.Color666
        println("decr tests")
        println("${testCpTop.name} tags: ${testCpTop.tags()}")

        println("decr(3) : ${testCpTop.decr(3).tags()}")
        assertEquals(ColorPalette.Color333, testCpTop.decr(3))

        println("decrR(4) : ${testCpTop.decrR(4).tags()}")
        assertEquals(ColorPalette.Color266, testCpTop.decrR(4))

        println("decrG(5) : ${testCpTop.decrG(5).tags()}")
        assertEquals(ColorPalette.Color616, testCpTop.decrG(5))

        println("decrB(6) : ${testCpTop.decrB(6).tags()}")
        assertEquals(ColorPalette.Color660, testCpTop.decrB(6))

        println("decrB(30) : ${testCpTop.decrB(30).tags()}")
        assertEquals(ColorPalette.Color660, testCpTop.decrB(30))

        val testCpTrans = ColorPalette.Color246
        println("label transform tests")
        println("${testCpTrans.name} tags: ${testCpTrans.tags()}")

        println("inv() : ${testCpTrans.inv().tags()}")
        assertEquals(ColorPalette.Color420, testCpTrans.inv())

        println("cmp() : ${testCpTrans.comp().tags()}")
        assertEquals(ColorPalette.Color642, testCpTrans.comp())

        println("triad() : ${testCpTrans.triad().first.tags()}, ${testCpTrans.triad().second.tags()}")
        assertEquals(ColorPalette.Color462, testCpTrans.triad().first)
        assertEquals(ColorPalette.Color624, testCpTrans.triad().second)

        println("label() : ${testCpTrans.label().tags()}")
        assertEquals(ColorPalette.Color420, testCpTrans.label())
    }

    @Test
    fun testColorSpectrum() {
        val testCp = ColorPalette.Color135

        println("spectrum tests")
        println("${testCp.name} tags: ${testCp.tags()}")

        testCp.spectrum().forEachIndexed { idx, cp ->
            println("$idx: ${cp.name} tags: ${cp.tags()}")
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

        println("labelled spectrum tests")
        println("${testCpLabelled.name} tags: ${testCpLabelled.tags()}")

        testCpLabelled.labelledSpectrum().entries.forEachIndexed { idx, cp ->
            println("$idx: ${cp.value.name} label: ${cp.key} tags: ${cp.value.tags()}")
        }

        assertEquals("skyBlue", testCpLabelled.labelledSpectrum().keys.toList()[6])
        assertEquals("lightSkyBlue", testCpLabelled.labelledSpectrum("lightSkyBlue").keys.toList()[6])
        assertEquals("skyBlue", testCpLabelled.labelledSpectrum("gibberish").keys.toList()[6])
        assertEquals("skyBlue", testCpLabelled.labelledSpectrum("paleTurquoise").keys.toList()[6])
    }

    @Test
    fun testOfApprox() {
        println("approx ColorPalettes:")
        println("MediumVioletRed :" + ColorPalette.ofApprox(199, 21, 133).also { assertEquals(ColorPalette.Color503, it) } )
        println("DarkRed :" + ColorPalette.ofApprox(139, 0, 0).also { assertEquals(ColorPalette.Color300, it) })
        println("OrangeRed :" + ColorPalette.ofApprox(255, 69, 0).also { assertEquals(ColorPalette.Color620, it) })
        println("DarkKhaki :" + ColorPalette.ofApprox(189, 183, 107).also { assertEquals(ColorPalette.Color542, it) })
        println("Maroon :" + ColorPalette.ofApprox(128, 0, 0).also { assertEquals(ColorPalette.Color300, it) })
        println("Indigo :" + ColorPalette.ofApprox(75, 0, 130).also { assertEquals(ColorPalette.Color203, it) })
        println("MidnightBlue :" + ColorPalette.ofApprox(25, 25, 112).also { assertEquals(ColorPalette.Color003, it) })
        println("Teal :" + ColorPalette.ofApprox(0, 128, 128).also { assertEquals(ColorPalette.Color033, it) })
        println("DarkGreen :" + ColorPalette.ofApprox(0, 100, 0).also { assertEquals(ColorPalette.Color020, it) })
        println("MistyRose :" + ColorPalette.ofApprox(255, 228, 225).also { assertEquals(ColorPalette.Color655, it) })
        println("Black :" + ColorPalette.ofApprox(0, 0, 0).also { assertEquals(ColorPalette.Color000, it) })
    }

    @Test
    fun testApproxSettings() {
        println("approx ColorPalette r, g, b:")
        println("MediumVioletRed :" + ColorPalette.approxSettings(199, 21, 133).also { assertEquals(listOf(5, 0, 3), it) } )
        println("DeepPink :" + ColorPalette.approxSettings(255, 20, 147).also { assertEquals(listOf(6, 0, 3), it) } )
        println("PaleVioletRed :" + ColorPalette.approxSettings(219, 112, 147).also { assertEquals(listOf(5, 3, 3), it) } )
        println("HotPink :" + ColorPalette.approxSettings(255, 105, 180).also { assertEquals(listOf(6, 2, 4), it) } )
        println("LightPink :" + ColorPalette.approxSettings(255, 182, 193).also { assertEquals(listOf(6, 4, 5), it) } )
        println("Pink :" + ColorPalette.approxSettings(255, 192, 203).also { assertEquals(listOf(6, 5, 5), it) } )

        println("DarkRed :" + ColorPalette.approxSettings(139, 0, 0).also { assertEquals(listOf(3, 0, 0), it) })
        println("Red :" + ColorPalette.approxSettings(255, 0, 0).also { assertEquals(listOf(6, 0, 0), it) })
        println("Firebrick :" + ColorPalette.approxSettings(178, 34, 34).also { assertEquals(listOf(4, 1, 1), it) })
        println("Crimson :" + ColorPalette.approxSettings(220, 20, 60).also { assertEquals(listOf(5, 0, 1), it) })
        println("IndianRed :" + ColorPalette.approxSettings(205, 92, 92).also { assertEquals(listOf(5, 2, 2), it) })
        println("LightCoral :" + ColorPalette.approxSettings(240, 128, 128).also { assertEquals(listOf(6, 3, 3), it) })
        println("Salmon :" + ColorPalette.approxSettings(250, 128, 114).also { assertEquals(listOf(6, 3, 3), it) })
        println("DarkSalmon :" + ColorPalette.approxSettings(233, 150, 122).also { assertEquals(listOf(6, 4, 3), it) })
        println("LightSalmon :" + ColorPalette.approxSettings(255, 160, 122).also { assertEquals(listOf(6, 4, 3), it) })

        println("OrangeRed :" + ColorPalette.approxSettings(255, 69, 0).also { assertEquals(listOf(6, 2, 0), it) })
        println("Tomato :" + ColorPalette.approxSettings(255, 99, 71).also { assertEquals(listOf(6, 2, 2), it) })
        println("DarkOrange :" + ColorPalette.approxSettings(255, 140, 0).also { assertEquals(listOf(6, 3, 0), it) })
        println("Coral :" + ColorPalette.approxSettings(255, 127, 80).also { assertEquals(listOf(6, 3, 2), it) })
        println("Orange :" + ColorPalette.approxSettings(255, 165, 0).also { assertEquals(listOf(6, 4, 0), it) })

        println("DarkKhaki :" + ColorPalette.approxSettings(189, 183, 107).also { assertEquals(listOf(5, 4, 2), it) })
        println("Gold :" + ColorPalette.approxSettings(255, 215, 0).also { assertEquals(listOf(6, 5, 0), it) })
        println("Khaki :" + ColorPalette.approxSettings(240, 230, 140).also { assertEquals(listOf(6, 6, 3), it) })
        println("PeachPuff :" + ColorPalette.approxSettings(255, 218, 185).also { assertEquals(listOf(6, 5, 4), it) })
        println("Yellow :" + ColorPalette.approxSettings(255, 255, 0).also { assertEquals(listOf(6, 6, 0), it) })
        println("PaleGoldenrod :" + ColorPalette.approxSettings(238, 232, 170).also { assertEquals(listOf(6, 6, 4), it) })
        println("Moccasin :" + ColorPalette.approxSettings(255, 228, 181).also { assertEquals(listOf(6, 5, 4), it) })
        println("PapayaWhip :" + ColorPalette.approxSettings(255, 239, 213).also { assertEquals(listOf(6, 6, 5), it) })
        println("LightGoldenrodYellow :" + ColorPalette.approxSettings(250, 250, 210).also { assertEquals(listOf(6, 6, 5), it) })
        println("LemonChiffon :" + ColorPalette.approxSettings(255, 250, 205).also { assertEquals(listOf(6, 6, 5), it) })
        println("LightYellow :" + ColorPalette.approxSettings(255, 255, 224).also { assertEquals(listOf(6, 6, 5), it) })

        println("Maroon :" + ColorPalette.approxSettings(128, 0, 0).also { assertEquals(listOf(3, 0, 0), it) })
        println("Brown :" + ColorPalette.approxSettings(165, 42, 42).also { assertEquals(listOf(4, 1, 1), it) })
        println("SaddleBrown :" + ColorPalette.approxSettings(139, 69, 19).also { assertEquals(listOf(3, 2, 0), it) })
        println("Sienna :" + ColorPalette.approxSettings(160, 82, 45).also { assertEquals(listOf(4, 2, 1), it) })
        println("Chocolate :" + ColorPalette.approxSettings(210, 105, 30).also { assertEquals(listOf(5, 2, 1), it) })
        println("DarkGoldenrod :" + ColorPalette.approxSettings(184, 134, 11).also { assertEquals(listOf(4, 3, 0), it) })
        println("Peru :" + ColorPalette.approxSettings(205, 133, 63).also { assertEquals(listOf(5, 3, 1), it) })
        println("RosyBrown :" + ColorPalette.approxSettings(188, 143, 143).also { assertEquals(listOf(4, 3, 3), it) })
        println("Goldenrod :" + ColorPalette.approxSettings(218, 165, 32).also { assertEquals(listOf(5, 4, 1), it) })
        println("SandyBrown :" + ColorPalette.approxSettings(244, 164, 96).also { assertEquals(listOf(6, 4, 2), it) })
        println("Tan :" + ColorPalette.approxSettings(210, 180, 140).also { assertEquals(listOf(5, 4, 3), it) })
        println("Burlywood :" + ColorPalette.approxSettings(222, 184, 135).also { assertEquals(listOf(5, 4, 3), it) })
        println("Wheat :" + ColorPalette.approxSettings(245, 222, 179).also { assertEquals(listOf(6, 5, 4), it) })
        println("NavajoWhite :" + ColorPalette.approxSettings(255, 222, 173).also { assertEquals(listOf(6, 5, 4), it) })
        println("Bisque :" + ColorPalette.approxSettings(255, 228, 196).also { assertEquals(listOf(6, 5, 5), it) })
        println("BlanchedAlmond :" + ColorPalette.approxSettings(255, 235, 205).also { assertEquals(listOf(6, 6, 5), it) })
        println("Cornsilk :" + ColorPalette.approxSettings(255, 248, 220).also { assertEquals(listOf(6, 6, 5), it) })
        //https://www.canva.com/colors/color-meanings/taupe/
        println("Taupe :" + ColorPalette.approxSettings(72, 60, 50).also { assertEquals(listOf(2, 1, 1), it) })

        println("Indigo :" + ColorPalette.approxSettings(75, 0, 130).also { assertEquals(listOf(2, 0, 3), it) })
        println("Purple :" + ColorPalette.approxSettings(128, 0, 128).also { assertEquals(listOf(3, 0, 3), it) })
        println("DarkMagenta :" + ColorPalette.approxSettings(139, 0, 139).also { assertEquals(listOf(3, 0, 3), it) })
        println("DarkViolet :" + ColorPalette.approxSettings(148, 0, 211).also { assertEquals(listOf(3, 0, 5), it) })
        println("DarkSlateBlue :" + ColorPalette.approxSettings(72, 61, 139).also { assertEquals(listOf(2, 1, 3), it) })
        println("BlueViolet :" + ColorPalette.approxSettings(138, 43, 226).also { assertEquals(listOf(3, 1, 5), it) })
        println("DarkOrchid :" + ColorPalette.approxSettings(153, 50, 204).also { assertEquals(listOf(4, 1, 5), it) })
        println("Fuchsia :" + ColorPalette.approxSettings(255, 0, 255).also { assertEquals(listOf(6, 0, 6), it) })
        println("Magenta :" + ColorPalette.approxSettings(255, 0, 255).also { assertEquals(listOf(6, 0, 6), it) })
        println("SlateBlue :" + ColorPalette.approxSettings(106, 90, 205).also { assertEquals(listOf(2, 2, 5), it) })
        println("MediumSlateBlue :" + ColorPalette.approxSettings(123, 104, 238).also { assertEquals(listOf(3, 2, 6), it) })
        println("MediumOrchid :" + ColorPalette.approxSettings(186, 85, 211).also { assertEquals(listOf(4, 2, 5), it) })
        println("MediumPurple :" + ColorPalette.approxSettings(147, 112, 219).also { assertEquals(listOf(3, 3, 5), it) })
        println("Orchid :" + ColorPalette.approxSettings(218, 112, 214).also { assertEquals(listOf(5, 3, 5), it) })
        println("Violet :" + ColorPalette.approxSettings(238, 130, 238).also { assertEquals(listOf(6, 3, 6), it) })
        println("Plum :" + ColorPalette.approxSettings(221, 160, 221).also { assertEquals(listOf(5, 4, 5), it) })
        println("Thistle :" + ColorPalette.approxSettings(216, 191, 216).also { assertEquals(listOf(5, 5, 5), it) })
        println("Lavender :" + ColorPalette.approxSettings(230, 230, 250).also { assertEquals(listOf(6, 6, 6), it) })
        //https://www.canva.com/colors/color-meanings/mauve/
        println("Mauve :" + ColorPalette.approxSettings(224, 176, 255).also { assertEquals(listOf(5, 4, 6), it) })

        println("MidnightBlue :" + ColorPalette.approxSettings(25, 25, 112).also { assertEquals(listOf(0, 0, 3), it) })
        println("Navy :" + ColorPalette.approxSettings(0, 0, 128).also { assertEquals(listOf(0, 0, 3), it) })
        println("DarkBlue :" + ColorPalette.approxSettings(0, 0, 139).also { assertEquals(listOf(0, 0, 3), it) })
        println("MediumBlue :" + ColorPalette.approxSettings(0, 0, 205).also { assertEquals(listOf(0, 0, 5), it) })
        println("Blue :" + ColorPalette.approxSettings(0, 0, 255).also { assertEquals(listOf(0, 0, 6), it) })
        println("RoyalBlue :" + ColorPalette.approxSettings(65, 105, 225).also { assertEquals(listOf(1, 2, 5), it) })
        println("SteelBlue :" + ColorPalette.approxSettings(70, 130, 180).also { assertEquals(listOf(2, 3, 4), it) })
        println("DodgerBlue :" + ColorPalette.approxSettings(30, 144, 255).also { assertEquals(listOf(1, 3, 6), it) })
        println("DeepSkyBlue :" + ColorPalette.approxSettings(0, 191, 255).also { assertEquals(listOf(0, 5, 6), it) })
        println("CornflowerBlue :" + ColorPalette.approxSettings(100, 149, 237).also { assertEquals(listOf(2, 4, 6), it) })
        println("SkyBlue :" + ColorPalette.approxSettings(135, 206, 235).also { assertEquals(listOf(3, 5, 6), it) })
        println("LightSkyBlue :" + ColorPalette.approxSettings(135, 206, 250).also { assertEquals(listOf(3, 5, 6), it) })
        println("LightSteelBlue :" + ColorPalette.approxSettings(176, 196, 222).also { assertEquals(listOf(4, 5, 5), it) })
        println("LightBlue :" + ColorPalette.approxSettings(173, 216, 230).also { assertEquals(listOf(4, 5, 6), it) })
        println("PowderBlue :" + ColorPalette.approxSettings(176, 224, 230).also { assertEquals(listOf(4, 5, 6), it) })

        println("Teal :" + ColorPalette.approxSettings(0, 128, 128).also { assertEquals(listOf(0, 3, 3), it) })
        println("DarkCyan :" + ColorPalette.approxSettings(0, 139, 139).also { assertEquals(listOf(0, 3, 3), it) })
        println("LightSeaGreen :" + ColorPalette.approxSettings(32, 178, 170).also { assertEquals(listOf(1, 4, 4), it) })
        println("CadetBlue :" + ColorPalette.approxSettings(95, 158, 160).also { assertEquals(listOf(2, 4, 4), it) })
        println("DarkTurquoise :" + ColorPalette.approxSettings(0, 206, 209).also { assertEquals(listOf(0, 5, 5), it) })
        println("MediumTurquoise :" + ColorPalette.approxSettings(72, 209, 204).also { assertEquals(listOf(2, 5, 5), it) })
        println("Turquoise :" + ColorPalette.approxSettings(64, 224, 208).also { assertEquals(listOf(1, 5, 5), it) })
        println("Aqua :" + ColorPalette.approxSettings(0, 255, 255).also { assertEquals(listOf(0, 6, 6), it) })
        println("Cyan :" + ColorPalette.approxSettings(0, 255, 255).also { assertEquals(listOf(0, 6, 6), it) })
        println("Aquamarine :" + ColorPalette.approxSettings(127, 255, 212).also { assertEquals(listOf(3, 6, 5), it) })
        println("PaleTurquoise :" + ColorPalette.approxSettings(175, 238, 238).also { assertEquals(listOf(4, 6, 6), it) })
        println("LightCyan :" + ColorPalette.approxSettings(224, 255, 255).also { assertEquals(listOf(5, 6, 6), it) })

        println("DarkGreen :" + ColorPalette.approxSettings(0, 100, 0).also { assertEquals(listOf(0, 2, 0), it) })
        println("Green :" + ColorPalette.approxSettings(0, 128, 0).also { assertEquals(listOf(0, 3, 0), it) })
        println("DarkOliveGreen :" + ColorPalette.approxSettings(85, 107, 47).also { assertEquals(listOf(2, 2, 1), it) })
        println("ForestGreen :" + ColorPalette.approxSettings(34, 139, 34).also { assertEquals(listOf(1, 3, 1), it) })
        println("SeaGreen :" + ColorPalette.approxSettings(46, 139, 87).also { assertEquals(listOf(1, 3, 2), it) })
        println("Olive :" + ColorPalette.approxSettings(128, 128, 0).also { assertEquals(listOf(3, 3, 0), it) })
        println("OliveDrab :" + ColorPalette.approxSettings(107, 142, 35).also { assertEquals(listOf(2, 3, 1), it) })
        println("MediumSeaGreen :" + ColorPalette.approxSettings(60, 179, 113).also { assertEquals(listOf(1, 4, 3), it) })
        println("LimeGreen :" + ColorPalette.approxSettings(50, 205, 50).also { assertEquals(listOf(1, 5, 1), it) })
        println("Lime :" + ColorPalette.approxSettings(0, 255, 0).also { assertEquals(listOf(0, 6, 0), it) })
        println("SpringGreen :" + ColorPalette.approxSettings(0, 255, 127).also { assertEquals(listOf(0, 6, 3), it) })
        println("MediumSpringGreen :" + ColorPalette.approxSettings(0, 250, 154).also { assertEquals(listOf(0, 6, 4), it) })
        println("DarkSeaGreen :" + ColorPalette.approxSettings(143, 188, 143).also { assertEquals(listOf(3, 4, 3), it) })
        println("MediumAquamarine :" + ColorPalette.approxSettings(102, 205, 170).also { assertEquals(listOf(2, 5, 4), it) })
        println("YellowGreen :" + ColorPalette.approxSettings(154, 205, 50).also { assertEquals(listOf(4, 5, 1), it) })
        println("LawnGreen :" + ColorPalette.approxSettings(124, 252, 0).also { assertEquals(listOf(3, 6, 0), it) })
        println("Chartreuse :" + ColorPalette.approxSettings(127, 255, 0).also { assertEquals(listOf(3, 6, 0), it) })
        println("LightGreen :" + ColorPalette.approxSettings(144, 238, 144).also { assertEquals(listOf(3, 6, 3), it) })
        println("GreenYellow :" + ColorPalette.approxSettings(173, 255, 47).also { assertEquals(listOf(4, 6, 1), it) })
        println("PaleGreen :" + ColorPalette.approxSettings(152, 251, 152).also { assertEquals(listOf(4, 6, 4), it) })

        println("MistyRose :" + ColorPalette.approxSettings(255, 228, 225).also { assertEquals(listOf(6, 5, 5), it) })
        println("AntiqueWhite :" + ColorPalette.approxSettings(250, 235, 215).also { assertEquals(listOf(6, 6, 5), it) })
        println("Linen :" + ColorPalette.approxSettings(250, 240, 230).also { assertEquals(listOf(6, 6, 6), it) })
        println("Beige :" + ColorPalette.approxSettings(245, 245, 220).also { assertEquals(listOf(6, 6, 5), it) })
        println("WhiteSmoke :" + ColorPalette.approxSettings(245, 245, 245).also { assertEquals(listOf(6, 6, 6), it) })
        println("LavenderBlush :" + ColorPalette.approxSettings(255, 240, 245).also { assertEquals(listOf(6, 6, 6), it) })
        println("OldLace :" + ColorPalette.approxSettings(253, 245, 230).also { assertEquals(listOf(6, 6, 6), it) })
        println("AliceBlue :" + ColorPalette.approxSettings(240, 248, 255).also { assertEquals(listOf(6, 6, 6), it) })
        println("Seashell :" + ColorPalette.approxSettings(255, 245, 238).also { assertEquals(listOf(6, 6, 6), it) })
        println("GhostWhite :" + ColorPalette.approxSettings(248, 248, 255).also { assertEquals(listOf(6, 6, 6), it) })
        println("Honeydew :" + ColorPalette.approxSettings(240, 255, 240).also { assertEquals(listOf(6, 6, 6), it) })
        println("FloralWhite :" + ColorPalette.approxSettings(255, 250, 240).also { assertEquals(listOf(6, 6, 6), it) })
        println("Azure :" + ColorPalette.approxSettings(240, 255, 255).also { assertEquals(listOf(6, 6, 6), it) })
        println("MintCream :" + ColorPalette.approxSettings(245, 255, 250).also { assertEquals(listOf(6, 6, 6), it) })
        println("Snow :" + ColorPalette.approxSettings(255, 250, 250).also { assertEquals(listOf(6, 6, 6), it) })
        println("Ivory :" + ColorPalette.approxSettings(255, 255, 240).also { assertEquals(listOf(6, 6, 6), it) })
        println("White :" + ColorPalette.approxSettings(255, 255, 255).also { assertEquals(listOf(6, 6, 6), it) })

        println("Black :" + ColorPalette.approxSettings(0, 0, 0).also { assertEquals(listOf(0, 0, 0), it) })
        println("DarkSlateGray :" + ColorPalette.approxSettings(47, 79, 79).also { assertEquals(listOf(1, 2, 2), it) })
        println("DimGray :" + ColorPalette.approxSettings(105, 105, 105).also { assertEquals(listOf(2, 2, 2), it) })
        println("SlateGray :" + ColorPalette.approxSettings(112, 128, 144).also { assertEquals(listOf(3, 3, 3), it) })
        println("Gray :" + ColorPalette.approxSettings(128, 128, 128).also { assertEquals(listOf(3, 3, 3), it) })
        println("LightSlateGray :" + ColorPalette.approxSettings(119, 136, 153).also { assertEquals(listOf(3, 3, 4), it) })
        println("DarkGray :" + ColorPalette.approxSettings(169, 169, 169).also { assertEquals(listOf(4, 4, 4), it) })
        println("Silver :" + ColorPalette.approxSettings(192, 192, 192).also { assertEquals(listOf(5, 5, 5), it) })
        println("LightGray :" + ColorPalette.approxSettings(211, 211, 211).also { assertEquals(listOf(5, 5, 5), it) })
        println("Gainsboro :" + ColorPalette.approxSettings(220, 220, 220).also { assertEquals(listOf(5, 5, 5), it) })

    }
}