package river.exertion.kcop

import java.util.UUID

open class Id {
    open var id : String = UUID.randomUUID().toString()
}