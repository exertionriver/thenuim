package river.exertion.kcop.base

import com.badlogic.gdx.Gdx

object Log {

    val debugPrefix = "kcop_debug"
    val testPrefix = "kcop_test"

    fun debug(tag : String, message : String? = null) : String {
        val returnLog = if (message != null) "[$debugPrefix $tag] $message" else "[$debugPrefix $tag]"

        if (Gdx.app != null)
            Gdx.app.debug("${debugPrefix}_gdx $tag", message ?: "")
        else //for non-Gdx unit tests
            println(returnLog)

        return returnLog
    }

    fun test(tag : String, message : String? = null) : String {
        val returnLog = if (message != null) "[$testPrefix $tag] $message" else "[$testPrefix $tag]"

        if (Gdx.app != null)
            Gdx.app.log("${testPrefix}_gdx $tag", message ?: "")
        else //for non-Gdx unit tests
            println(returnLog)

        return returnLog
    }
}

fun List<String>.str(delim : String) = this.reduceOrNull { acc, s -> acc + "$delim$s"} ?: ""
