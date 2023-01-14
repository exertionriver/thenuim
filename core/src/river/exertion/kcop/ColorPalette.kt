package river.exertion.kcop

import com.badlogic.gdx.graphics.Color

enum class ColorPalette {
    //helpful: https://www.canva.com/colors/color-wheel/

    //https://en.wikipedia.org/wiki/Web_colors
    //basic color approximations
    Color000 { override fun tag() = "black" },
    Color006 { override fun tag() = "blue" },
    Color060 { override fun tag() = "lime" },
    Color066 { override fun tag() = "aqua" },
    Color003 { override fun tag() = "navy" },
    Color030 { override fun tag() = "green" },
    Color033 { override fun tag() = "teal" },
    Color300 { override fun tag() = "maroon" },
    Color303 { override fun tag() = "purple" },
    Color330 { override fun tag() = "olive" },
    Color333 { override fun tag() = "gray" },
    Color555 { override fun tag() = "silver" },
    Color600 { override fun tag() = "red" },
    Color606 { override fun tag() = "fuchsia" },
    Color660 { override fun tag() = "yellow" },
    Color666 { override fun tag() = "white" },

    //extended color approximations
    //div 255; 0 == 8, 1 == 48, 2 == 88, 3 == 128, 4 == 168, 5 == 208, 6 == 248
    Color122 { override fun tag() = "darkSlateGray" },
    Color222 { override fun tag() = "dimGray" },
    Color233 { override fun tag() = "slateGray" },
    Color444 { override fun tag() = "darkGray" },

    Color640 { override fun tag() = "orange" },

    Color001, Color002, Color004, Color005,
    Color010, Color011, Color012, Color013, Color014, Color015, Color016,
    Color020, Color021, Color022, Color023, Color024, Color025, Color026,
    Color031, Color032, Color034, Color035, Color036,
    Color040, Color041, Color042, Color043, Color044, Color045, Color046,
    Color050, Color051, Color052, Color053, Color054, Color055, Color056,
    Color061, Color062, Color063, Color064, Color065,

    Color100, Color101, Color102, Color103, Color104, Color105, Color106,
    Color110, Color111, Color112, Color113, Color114, Color115, Color116,
    Color120, Color121, Color123, Color124, Color125, Color126,
    Color130, Color131, Color132, Color133, Color134, Color135, Color136,
    Color140, Color141, Color142, Color143, Color144, Color145, Color146,
    Color150, Color151, Color152, Color153, Color154, Color155, Color156,
    Color160, Color161, Color162, Color163, Color164, Color165, Color166,

    Color200, Color201, Color202, Color203, Color204, Color205, Color206,
    Color210, Color211, Color212, Color213, Color214, Color215, Color216,
    Color220, Color221, Color223, Color224, Color225, Color226,
    Color230, Color231, Color232, Color234, Color235, Color236,
    Color240, Color241, Color242, Color243, Color244, Color245, Color246,
    Color250, Color251, Color252, Color253, Color254, Color255, Color256,
    Color260, Color261, Color262, Color263, Color264, Color265, Color266,

    Color301, Color302, Color304, Color305, Color306,
    Color310, Color311, Color312, Color313, Color314, Color315, Color316,
    Color320, Color321, Color322, Color323, Color324, Color325, Color326,
    Color331, Color332, Color334, Color335, Color336,
    Color340, Color341, Color342, Color343, Color344, Color345, Color346,
    Color350, Color351, Color352, Color353, Color354, Color355, Color356,
    Color360, Color361, Color362, Color363, Color364, Color365, Color366,

    Color400, Color401, Color402, Color403, Color404, Color405, Color406,
    Color410, Color411, Color412, Color413, Color414, Color415, Color416,
    Color420, Color421, Color422, Color423, Color424, Color425, Color426,
    Color430, Color431, Color432, Color433, Color434, Color435, Color436,
    Color440, Color441, Color442, Color443, Color445, Color446,
    Color450, Color451, Color452, Color453, Color454, Color455, Color456,
    Color460, Color461, Color462, Color463, Color464, Color465, Color466,

    Color500, Color501, Color502, Color503, Color504, Color505, Color506,
    Color510, Color511, Color512, Color513, Color514, Color515, Color516,
    Color520, Color521, Color522, Color523, Color524, Color525, Color526,
    Color530, Color531, Color532, Color533, Color534, Color535, Color536,
    Color540, Color541, Color542, Color543, Color544, Color545, Color546,
    Color550, Color551, Color552, Color553, Color554, Color556,
    Color560, Color561, Color562, Color563, Color564, Color565, Color566,

    Color601, Color602, Color603, Color604, Color605,
    Color610, Color611, Color612, Color613, Color614, Color615, Color616,
    Color620, Color621, Color622, Color623, Color624, Color625, Color626,
    Color630, Color631, Color632, Color633, Color634, Color635, Color636,
    Color641, Color642, Color643, Color644, Color645, Color646,
    Color650, Color651, Color652, Color653, Color654, Color655, Color656,
    Color661, Color662, Color663, Color664, Color665,
    ;

    open fun tag() : String = this.name
    fun color() = Color(colorSettings[rSetting()], colorSettings[gSetting()], colorSettings[bSetting()], defaultAlpha)
    fun incr() = ColorPalette.of(rSetting(1), gSetting(1), bSetting(1) )
    fun incrR() = ColorPalette.of(rSetting(1), gSetting(), bSetting() )
    fun incrG() = ColorPalette.of(rSetting(), gSetting(1), bSetting() )
    fun incrB() = ColorPalette.of(rSetting(), gSetting(), bSetting(1) )
    fun decr() = ColorPalette.of(rSetting(-1), gSetting(-1), bSetting(-1) )
    fun decrR() = ColorPalette.of(rSetting(-1), gSetting(), bSetting() )
    fun decrG() = ColorPalette.of(rSetting(), gSetting(-1), bSetting() )
    fun decrB() = ColorPalette.of(rSetting(), gSetting(), bSetting(-1) )
    fun comp() : ColorPalette {
        val max = maxOf(rSetting(), gSetting(), bSetting())
        val min = minOf(rSetting(), gSetting(), bSetting())
        val range = max + min
        return ColorPalette.of(range - rSetting(), range - gSetting(), range - bSetting())
    }
    fun triad() = Pair(
        ColorPalette.of(gSetting(), bSetting(), rSetting()),
        ColorPalette.of(bSetting(), rSetting(), gSetting()),
    )
    fun spectrum() = listOf(
        ColorPalette.of(rSetting(-6), gSetting(-6), bSetting(-6)),
        ColorPalette.of(rSetting(-5), gSetting(-5), bSetting(-5)),
        ColorPalette.of(rSetting(-4), gSetting(-4), bSetting(-4)),
        ColorPalette.of(rSetting(-3), gSetting(-3), bSetting(-3)),
        ColorPalette.of(rSetting(-2), gSetting(-2), bSetting(-2)),
        ColorPalette.of(rSetting(-1), gSetting(-1), bSetting(-1)),
        ColorPalette.of(rSetting(), gSetting(), bSetting()),
        ColorPalette.of(rSetting(1), gSetting(1), bSetting(1)),
        ColorPalette.of(rSetting(2), gSetting(2), bSetting(2)),
        ColorPalette.of(rSetting(3), gSetting(3), bSetting(3)),
        ColorPalette.of(rSetting(4), gSetting(4), bSetting(4)),
        ColorPalette.of(rSetting(5), gSetting(5), bSetting(5)),
        ColorPalette.of(rSetting(6), gSetting(6), bSetting(6)),
    ).distinct()

    fun rSetting(offset : Int = 0) = (this.name.substring(5,6).toInt() + offset).coerceIn(0, 6)
    fun gSetting(offset : Int = 0) = (this.name.substring(6,7).toInt() + offset).coerceIn(0, 6)
    fun bSetting(offset : Int = 0) = (this.name.substring(7,8).toInt() + offset).coerceIn(0, 6)

    companion object {
        fun of(rSetting : Int, gSetting : Int, bSetting : Int) = ColorPalette.values().firstOrNull { it.name == "Color$rSetting$gSetting$bSetting" } ?: Color000
        fun of(tag : String) = ColorPalette.values().firstOrNull { it.tag() == tag } ?: Color000

        //div 255; 0 == 8, 1 == 48, 2 == 88, 3 == 128, 4 == 168, 5 == 208, 6 == 248
        val colorSettings : List<Float> = listOf(0.0314f, 0.1882f, 0.3451f, 0.502f, 0.6588f, 0.8157f, 0.9725f)
        const val defaultAlpha = 1f
    }}
