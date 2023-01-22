package river.exertion.kcop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import java.nio.file.Path

object Util {
    fun logDebug(tag : String, message : String) {
        if (Gdx.app != null)
            Gdx.app.debug(tag, message)
        else
            println("$tag: $message")
    }

    @Suppress("NewApi")
    fun internalFile(path : String) : FileHandle {
        return if (Gdx.files != null)
            Gdx.files.internal(path)
        else {
            val fullPath = Path.of("").toAbsolutePath().parent.toString() + "/android/assets/$path"
            FileHandle(fullPath)
        }
    }

    val json = Json { ignoreUnknownKeys = true }

    inline fun <reified T>loader(fileHandle: FileHandle) : T? {
        try {
            val jsonElement = json.parseToJsonElement(fileHandle.readString())
            return json.decodeFromJsonElement(jsonElement) as T
        } catch (ex : Exception) {
            logDebug("loader", ex.toString())
        }
        return null
    }
}
