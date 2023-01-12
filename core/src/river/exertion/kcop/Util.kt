package river.exertion.kcop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import ktx.assets.toAbsoluteFile
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
        if (Gdx.files != null)
            return Gdx.files.internal(path)
        else {
            val fullPath = Path.of("").toAbsolutePath().parent.toString() + "/android/assets/$path"
            return FileHandle(fullPath)
        }
    }
}