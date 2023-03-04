package river.exertion.kcop.system.profile

import kotlinx.serialization.Serializable
import river.exertion.kcop.Id
import java.util.*

@Serializable
data class Profile(
    override var id : String,
    var name : String,
    var currentImmersionId : String?,
    var currentImmersionIdx : Int?,
    val statuses : MutableList<Status>
) : Id() {
    constructor() : this(UUID.randomUUID().toString(), "default", null, null, mutableListOf())
}
