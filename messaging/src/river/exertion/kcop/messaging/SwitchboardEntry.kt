package river.exertion.kcop.messaging

data class SwitchboardEntry(val switchboardTag : String, var switchboardTagAction: (() -> Unit)? = { throw Exception("$switchboardTag action not defined") })