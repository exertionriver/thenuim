package river.exertion.kcop.narrative.character

import kotlin.random.Random

object NameGenerator {

    val commonSyllables = listOf(
        listOf("osh", "ov"),
        listOf("ur", "uth"),
        listOf("al", "at"),
        listOf("in", "id"),
        listOf("ez", "es"),
    )
    fun filterOutSyllableShuffle(idx : Int) = commonSyllables.filter {it != commonSyllables[idx]}.shuffled(rnd)
    fun innerShuffle(syllablePairs : List<List<String>>) = syllablePairs[0].shuffled(rnd)[0] + syllablePairs[1].shuffled(
        rnd
    )[0] + syllablePairs[2].shuffled(rnd)[0] + syllablePairs[3].shuffled(rnd)[0]

    val commonVowels = mutableListOf("o", "u", "a", "i", "e")
    val commonConsonants = mutableListOf("y", "dz", "dj", "ch", "sz", "s", "h", "p", "ng", "b", "g", "m", "k", "w","zh", "f", "sh", "v", "l", "t", "r", "th", "n","d", "z","s")

    val rnd = Random(11161978)

    fun nextVowel() = commonVowels[rnd.nextInt(commonVowels.size)]
    fun nextConsonant() = commonConsonants[rnd.nextInt(commonConsonants.size)]
    fun nextSyllable() = commonSyllables.flatten()[rnd.nextInt(commonSyllables.flatten().size)]
}