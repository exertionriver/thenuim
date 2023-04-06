package river.exertion.kcop

import java.util.UUID

interface Id {
    var id : String

    companion object {
        fun randomId() : String = UUID.randomUUID().toString()
    }
}