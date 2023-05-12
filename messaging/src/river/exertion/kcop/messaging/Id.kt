package river.exertion.kcop.messaging

import com.badlogic.gdx.Gdx
import java.util.UUID

interface Id {
    var id : String

    companion object {
        fun randomId() : String = UUID.randomUUID().toString()

        fun logDebug(tag : String, message : String) {
            if (Gdx.app != null)
                Gdx.app.debug(tag, message)
            else
                println("$tag: $message")
        }
    }
}