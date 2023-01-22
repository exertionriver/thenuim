package river.exertion.kcop.system.colorPalette

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector3
import kotlin.math.absoluteValue

enum class ColorPalette {
    //color approximations for https://en.wikipedia.org/wiki/Web_colors
    //helpful for comp() and triad(): https://www.canva.com/colors/color-wheel/

    Color000 { override fun tags() = listOf("black") },
    Color003 { override fun tags() = listOf("navy", "midnightBlue", "darkBlue") },
    Color005 { override fun tags() = listOf("mediumBlue") },
    Color006 { override fun tags() = listOf("blue") },
    Color020 { override fun tags() = listOf("darkGreen") },
    Color030 { override fun tags() = listOf("green") },
    Color033 { override fun tags() = listOf("teal", "darkCyan") },
    Color036 { override fun tags() = listOf("azure") },
    Color055 { override fun tags() = listOf("darkTurquoise") },
    Color056 { override fun tags() = listOf("deepSkyBlue") },
    Color060 { override fun tags() = listOf("lime") },
    Color063 { override fun tags() = listOf("springGreen") },
    Color064 { override fun tags() = listOf("mediumSpringGreen") },
    Color066 { override fun tags() = listOf("cyan", "aqua") },

    Color001, Color002, Color004,
    Color010, Color011, Color012, Color013, Color014, Color015, Color016,
    Color021, Color022, Color023, Color024, Color025, Color026,
    Color031, Color032, Color034, Color035,
    Color040, Color041, Color042, Color043, Color044, Color045, Color046,
    Color050, Color051, Color052, Color053, Color054,
    Color061, Color062, Color065,

    Color122 { override fun tags() = listOf("darkSlateGray") },
    Color125 { override fun tags() = listOf("royalBlue") },
    Color131 { override fun tags() = listOf("forestGreen") },
    Color132 { override fun tags() = listOf("seaGreen") },
    Color136 { override fun tags() = listOf("dodgerBlue") },
    Color143 { override fun tags() = listOf("mediumSeaGreen") },
    Color144 { override fun tags() = listOf("lightSeaGreen") },
    Color151 { override fun tags() = listOf("limeGreen") },
    Color155 { override fun tags() = listOf("turquoise") },

    Color100, Color101, Color102, Color103, Color104, Color105, Color106,
    Color110, Color111, Color112, Color113, Color114, Color115, Color116,
    Color120, Color121, Color123, Color124, Color126,
    Color130, Color133, Color134, Color135,
    Color140, Color141, Color142, Color145, Color146,
    Color150, Color152, Color153, Color154, Color156,
    Color160, Color161, Color162, Color163, Color164, Color165, Color166,

    Color203 { override fun tags() = listOf("indigo") },
    Color211 { override fun tags() = listOf("taupe") },
    Color213 { override fun tags() = listOf("darkSlateBlue") },
    Color221 { override fun tags() = listOf("darkOliveGreen") },
    Color222 { override fun tags() = listOf("dimGray") },
    Color225 { override fun tags() = listOf("slateBlue") },
    Color231 { override fun tags() = listOf("oliveDrab") },
    Color234 { override fun tags() = listOf("steelBlue") },
    Color244 { override fun tags() = listOf("cadetBlue") },
    Color246 { override fun tags() = listOf("cornflowerBlue") },
    Color254 { override fun tags() = listOf("mediumAquamarine") },
    Color255 { override fun tags() = listOf("mediumTurquoise") },

    Color200, Color201, Color202, Color204, Color205, Color206,
    Color210, Color212, Color214, Color215, Color216,
    Color220, Color223, Color224, Color226,
    Color230, Color232, Color233, Color235, Color236,
    Color240, Color241, Color242, Color243, Color245,
    Color250, Color251, Color252, Color253, Color256,
    Color260, Color261, Color262, Color263, Color264, Color265, Color266,

    Color300 { override fun tags() = listOf("maroon", "darkRed") },
    Color303 { override fun tags() = listOf("purple", "darkMagenta") },
    Color305 { override fun tags() = listOf("darkViolet") },
    Color315 { override fun tags() = listOf("blueViolet") },
    Color320 { override fun tags() = listOf("saddleBrown") },
    Color326 { override fun tags() = listOf("mediumSlateBlue") },
    Color330 { override fun tags() = listOf("olive") },
    Color333 { override fun tags() = listOf("gray", "slateGray") },
    Color334 { override fun tags() = listOf("lightSlateGray") },
    Color335 { override fun tags() = listOf("mediumPurple") },
    Color343 { override fun tags() = listOf("darkSeaGreen") },
    Color356 { override fun tags() = listOf("skyBlue", "lightSkyBlue") },
    Color360 { override fun tags() = listOf("lawnGreen", "chartreuse") },
    Color363 { override fun tags() = listOf("lightGreen") },
    Color365 { override fun tags() = listOf("aquamarine") },

    Color301, Color302, Color304,  Color306,
    Color310, Color311, Color312, Color313, Color314,  Color316,
    Color321, Color322, Color323, Color324, Color325,
    Color331, Color332, Color336,
    Color340, Color341, Color342, Color344, Color345, Color346,
    Color350, Color351, Color352, Color353, Color354, Color355,
    Color361, Color362, Color364, Color366,

    Color411 { override fun tags() = listOf("brown", "fireBrick") },
    Color415 { override fun tags() = listOf("darkOrchid") },
    Color421 { override fun tags() = listOf("sienna") },
    Color425 { override fun tags() = listOf("mediumOrchid") },
    Color430 { override fun tags() = listOf("darkGoldenrod") },
    Color433 { override fun tags() = listOf("rosyBrown") },
    Color444 { override fun tags() = listOf("darkGray") },
    Color451 { override fun tags() = listOf("yellowGreen") },
    Color455 { override fun tags() = listOf("lightSteelBlue") },
    Color456 { override fun tags() = listOf("lightBlue", "powderBlue") },
    Color461 { override fun tags() = listOf("greenYellow") },
    Color464 { override fun tags() = listOf("paleGreen") },
    Color466 { override fun tags() = listOf("paleTurquoise") },

    Color400, Color401, Color402, Color403, Color404, Color405, Color406,
    Color410, Color412, Color413, Color414, Color416,
    Color420, Color422, Color423, Color424, Color426,
    Color431, Color432, Color434, Color435, Color436,
    Color440, Color441, Color442, Color443, Color445, Color446,
    Color450, Color452, Color453, Color454,
    Color460, Color462, Color463, Color465,

    Color501 { override fun tags() = listOf("crimson") },
    Color503 { override fun tags() = listOf("mediumVioletRed") },
    Color521 { override fun tags() = listOf("chocolate") },
    Color522 { override fun tags() = listOf("indianRed") },
    Color531 { override fun tags() = listOf("peru") },
    Color533 { override fun tags() = listOf("paleVioletRed") },
    Color535 { override fun tags() = listOf("orchid") },
    Color541 { override fun tags() = listOf("goldenrod") },
    Color542 { override fun tags() = listOf("darkKhaki") },
    Color543 { override fun tags() = listOf("tan", "burlywood") },
    Color545 { override fun tags() = listOf("plum") },
    Color546 { override fun tags() = listOf("mauve") },
    Color555 { override fun tags() = listOf("silver", "lightGray", "thistle") },
    Color556 { override fun tags() = listOf("lightCyan") },

    Color500, Color502, Color504, Color505, Color506,
    Color510, Color511, Color512, Color513, Color514, Color515, Color516,
    Color520, Color523, Color524, Color525, Color526,
    Color530, Color532, Color534, Color536,
    Color540, Color544,
    Color550, Color551, Color552, Color553, Color554,
    Color560, Color561, Color562, Color563, Color564, Color565, Color566,

    Color600 { override fun tags() = listOf("red") },
    Color603 { override fun tags() = listOf("deepPink") },
    Color606 { override fun tags() = listOf("magenta", "fuchsia") },
    Color620 { override fun tags() = listOf("orangeRed") },
    Color622 { override fun tags() = listOf("tomato") },
    Color624 { override fun tags() = listOf("hotPink") },
    Color630 { override fun tags() = listOf("darkOrange") },
    Color632 { override fun tags() = listOf("coral") },
    Color633 { override fun tags() = listOf("salmon", "lightCoral") },
    Color636 { override fun tags() = listOf("violet") },
    Color640 { override fun tags() = listOf("orange") },
    Color642 { override fun tags() = listOf("sandyBrown") },
    Color643 { override fun tags() = listOf("lightSalmon", "darkSalmon") },
    Color645 { override fun tags() = listOf("lightPink") },
    Color650 { override fun tags() = listOf("gold") },
    Color654 { override fun tags() = listOf("wheat", "peachPuff", "moccasin") },
    Color655 { override fun tags() = listOf("pink", "bisque") },
    Color660 { override fun tags() = listOf("yellow") },
    Color663 { override fun tags() = listOf("khaki") },
    Color664 { override fun tags() = listOf("paleGoldenrod") },
    Color665 { override fun tags() = listOf("beige", "lightYellow", "antiqueWhite", "blanchedAlmond") },
    Color666 { override fun tags() = listOf("white", "ivory", "seashell") },

    Color601, Color602, Color604, Color605,
    Color610, Color611, Color612, Color613, Color614, Color615, Color616,
    Color621, Color623, Color625, Color626,
    Color631, Color634, Color635,
    Color641, Color644, Color646,
    Color651, Color652, Color653, Color656,
    Color661, Color662,

    //excluded tags; see testColorPalette() for approximations
    //gainsboro     [5, 5, 5]
    //navajoWhite   [6, 5, 4]
    //mistyRose 	[6, 5, 5]
    //papayaWhip 	[6, 6, 5]
    //lightGoldenrodYellow 	[6, 6, 5]
    //lemonChiffon 	[6, 6, 5]
    //cornsilk 	    [6, 6, 5]
    //lavender 	    [6, 6, 6]
    //linen 	    [6, 6, 6]
    //whiteSmoke 	[6, 6, 6]
    //lavenderBlush [6, 6, 6]
    //oldLace 	    [6, 6, 6]
    //aliceBlue 	[6, 6, 6]
    //ghostWhite 	[6, 6, 6]
    //honeydew 	    [6, 6, 6]
    //floralWhite 	[6, 6, 6]
    //azure 	    [6, 6, 6]
    //mintCream 	[6, 6, 6]
    //snow 	        [6, 6, 6]

    ;

    open fun tags() : List<String> = listOf(this.name)
    fun color() = Color(colorThresholdsFloat[rSetting()], colorThresholdsFloat[gSetting()], colorThresholdsFloat[bSetting()], defaultAlpha)
    fun incr(by : Int = 1) = of(rSetting(by), gSetting(by), bSetting(by) )
    fun incrR(by : Int = 1) = of(rSetting(by), gSetting(), bSetting() )
    fun incrG(by : Int = 1) = of(rSetting(), gSetting(by), bSetting() )
    fun incrB(by : Int = 1) = of(rSetting(), gSetting(), bSetting(by) )
    fun decr(by : Int = 1) = incr(-by)
    fun decrR(by : Int = 1) = incrR(-by)
    fun decrG(by : Int = 1) = incrG(-by)
    fun decrB(by : Int = 1) = incrB(-by)
    fun inv() : ColorPalette {
        return of(colorThresholdsInt.size - 1 - rSetting(), colorThresholdsInt.size - 1 - gSetting(), colorThresholdsInt.size - 1 - bSetting())
    }
    fun comp() : ColorPalette {
        val max = maxOf(rSetting(), gSetting(), bSetting())
        val min = minOf(rSetting(), gSetting(), bSetting())
        val range = max + min
        return of(range - rSetting(), range - gSetting(), range - bSetting())
    }

    fun triad() = Pair(
        of(gSetting(), bSetting(), rSetting()),
        of(bSetting(), rSetting(), gSetting()),
    )

    fun spectrum() = listOf(
        this.decr(6), this.decr(5), this.decr(4), this.decr(3), this.decr(2), this.decr(1),
        this,
        this.incr(1), this.incr(2), this.incr(3), this.incr(4), this.incr(5), this.incr(6),
    ).distinct()

    fun labelledSpectrum(labelOverride : String? = null) : Map<String, ColorPalette> = spectrum().associateBy { if ((labelOverride != null) && (it == this@ColorPalette)) labelOverride else it.tags()[0] }

    fun rSetting(offset : Int = 0) = (this.name.substring(5,6).toInt() + offset).coerceIn(0, 6)
    fun gSetting(offset : Int = 0) = (this.name.substring(6,7).toInt() + offset).coerceIn(0, 6)
    fun bSetting(offset : Int = 0) = (this.name.substring(7,8).toInt() + offset).coerceIn(0, 6)

    fun label() : ColorPalette {
        val thisColorV3 = Vector3(rSetting().toFloat(), gSetting().toFloat(), bSetting().toFloat())

        val cmpColors = mapOf (
            inv() to Vector3(inv().rSetting().toFloat(), inv().gSetting().toFloat(), inv().bSetting().toFloat()),
            comp() to Vector3(comp().rSetting().toFloat(), comp().gSetting().toFloat(), comp().bSetting().toFloat()),
            incr(2) to Vector3(incr(2).rSetting().toFloat(), incr(2).gSetting().toFloat(), incr(2).bSetting().toFloat())
        )

        return cmpColors.maxBy { it.value.dst(thisColorV3) }.key
    }

    companion object {
        // r, g, b, each 0 - 255
        fun approxSettings(r : Int, g : Int, b : Int) =
            listOf(
                colorThresholdsInt.map { (r - it).absoluteValue }.withIndex().minBy { (_, it) -> it }.index,
                colorThresholdsInt.map { (g - it).absoluteValue }.withIndex().minBy { (_, it) -> it }.index,
                colorThresholdsInt.map { (b - it).absoluteValue }.withIndex().minBy { (_, it) -> it }.index,
            )

        fun ofApprox(r : Int, g : Int, b : Int) : ColorPalette {
            val approx = approxSettings(r, g, b)
            return of(approx[0], approx[1], approx[2])
        }
        fun of(rSetting : Int, gSetting : Int, bSetting : Int) = ColorPalette.values().firstOrNull { it.name == "Color$rSetting$gSetting$bSetting" } ?: Color000
        fun of(tag : String) = ColorPalette.values().firstOrNull { it.tags().contains(tag) } ?: Color000
        fun ofKind(tag : String) = ColorPalette.values().filter { it.tags().contains(tag) }

        val colorThresholdsInt : List<Int> = listOf(8, 48, 88, 128, 168, 208, 248)
        val colorThresholdsFloat : List<Float> = colorThresholdsInt.map { it / 255f }
        const val defaultAlpha = 1f

        //w3c standard color lists
        fun w3cBasic() = listOf(
            "black", "blue", "lime", "aqua", "navy", "green", "teal", "maroon",
            "purple", "olive", "gray", "silver", "red", "fuchsia", "yellow", "white",
        ).associateWith { of(it) }

        fun w3cExtPink() = listOf(
            "mediumVioletRed", "deepPink", "paleVioletRed", "hotPink", "lightPink", "pink",
        ).associateWith { of(it) }

        fun w3cExtRed() = listOf(
            "darkRed", "red", "fireBrick", "crimson", "indianRed", "lightCoral", "salmon", "darkSalmon",
            "lightSalmon"
        ).associateWith { of(it) }

        fun w3cExtOrange() = listOf(
            "orangeRed", "tomato", "darkOrange", "coral", "orange"
        ).associateWith { of(it) }

        fun w3cExtYellow() = listOf(
            "darkKhaki", "gold", "khaki", "peachPuff", "yellow", "paleGoldenrod", "moccasin", "lightYellow"
        ).associateWith { of(it) }

        fun w3cExtBrown() = listOf(
            "maroon", "brown", "saddleBrown", "sienna", "chocolate", "darkGoldenrod", "peru", "rosyBrown",
            "goldenrod", "sandyBrown", "tan", "burlywood", "wheat", "bisque", "blanchedAlmond"
        ).associateWith { of(it) }

        fun w3cExtGreen() = listOf(
            "darkGreen", "green", "darkOliveGreen", "forestGreen", "seaGreen", "olive", "oliveDrab", "mediumSeaGreen",
            "limeGreen", "lime", "springGreen", "mediumSpringGreen", "darkSeaGreen", "mediumAquamarine", "yellowGreen",
            "lawnGreen", "chartreuse", "lightGreen", "greenYellow", "paleGreen"
        ).associateWith { of(it) }

        fun w3cExtPurpleVioletMagenta() = listOf(
            "indigo", "purple", "darkMagenta", "darkViolet", "darkSlateBlue", "blueViolet", "darkOrchid", "fuchsia",
            "magenta", "slateBlue", "mediumSlateBlue", "mediumOrchid", "mediumPurple", "orchid", "violet",
            "plum", "thistle"
        ).associateWith { of(it) }

        fun w3cExtBlue() = listOf(
            "midnightBlue", "navy", "darkBlue", "mediumBlue", "blue", "royalBlue", "steelBlue", "dodgerBlue",
            "deepSkyBlue", "cornflowerBlue", "skyBlue", "lightSkyBlue", "lightSteelBlue", "lightBlue", "powderBlue",
        ).associateWith { of(it) }

        fun w3cExtCyan() = listOf(
            "teal", "darkCyan", "lightSeaGreen", "cadetBlue", "darkTurquoise", "mediumTurquoise", "turquoise", "aqua",
            "cyan", "aquamarine", "paleTurquoise", "lightCyan"
        ).associateWith { of(it) }

        fun w3cExtWhite() = listOf(
            "antiqueWhite", "beige", "seashell", "ivory", "white"
        ).associateWith { of(it) }

        fun w3cExtGrayBlack() = listOf(
            "black", "darkSlateGray", "dimGray", "slateGray", "gray", "lightSlateGray", "darkGray", "silver",
            "lightGray"
        ).associateWith { of(it) }

        fun w3cColors() = listOf(
            w3cBasic(), w3cExtPink(), w3cExtRed(), w3cExtOrange(), w3cExtYellow(), w3cExtBrown(), w3cExtGreen(),
            w3cExtPurpleVioletMagenta(), w3cExtBlue(), w3cExtCyan(), w3cExtWhite(), w3cExtGrayBlack()
        )
    }
}
