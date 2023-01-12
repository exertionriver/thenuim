package river.exertion.kcop.narrative

import java.util.*

enum class NameTypes {
    CELESTIAL_OSH { override fun nextName() = NameGenerator.innerShuffle(NameGenerator.filterOutSyllableShuffle(0)) },
    CELESTIAL_UR { override fun nextName() = NameGenerator.innerShuffle(NameGenerator.filterOutSyllableShuffle(1)) },
    CELESTIAL_LA { override fun nextName() = NameGenerator.innerShuffle(NameGenerator.filterOutSyllableShuffle(2)) },
    CELESTIAL_DI { override fun nextName() = NameGenerator.innerShuffle(NameGenerator.filterOutSyllableShuffle(3)) },
    CELESTIAL_SE { override fun nextName() = NameGenerator.innerShuffle(NameGenerator.filterOutSyllableShuffle(4)) },
    CONTINENT { override fun nextName() = NameGenerator.nextVowel() + NameGenerator.nextSyllable() },
    TITLE { override fun nextName() = NameGenerator.nextConsonant() + NameGenerator.nextSyllable() + NameGenerator.nextVowel() },
    LAND { override fun nextName() = NameGenerator.nextVowel() + NameGenerator.nextSyllable() + NameGenerator.nextVowel() },
    ROYAL { override fun nextName() = NameGenerator.nextConsonant() + NameGenerator.nextSyllable() + NameGenerator.nextConsonant() + NameGenerator.nextSyllable() },
    PRIEST { override fun nextName() = NameGenerator.nextVowel() + NameGenerator.nextSyllable() + NameGenerator.nextVowel() + NameGenerator.nextSyllable() },
    COMMON { override fun nextName() = NameGenerator.nextConsonant() + NameGenerator.nextSyllable() + NameGenerator.nextVowel() + NameGenerator.nextSyllable() },
    LOWER { override fun nextName() = NameGenerator.nextConsonant() + NameGenerator.nextSyllable() + NameGenerator.nextConsonant() + NameGenerator.nextSyllable() },
    ;

    abstract fun nextName() : String
    fun capName() = nextName().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}