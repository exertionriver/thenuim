import org.junit.jupiter.api.Test
import river.exertion.kcop.messaging.Id

class TestId {

    @Test
    fun testIds() {

        (0..9).forEach {
            println("id:${Id.randomId()}")
        }

    }

    @Test
    fun testRegEx() {

        val replaceChars = """[.@{}!\\`´"^=()&\[\]$'~#%*:<>?/|, ]"""

        val inString = "asdf@#\$bcd\\/`"

        println(inString.filterNot { replaceChars.contains(it) })

        println(inString.replace(replaceChars.toRegex(), "_"))

        val inString2 = "asdf______fdf"

        val replaceChars2 = """_+"""

        println(inString2.replace(replaceChars2.toRegex(), "_"))

    }
}