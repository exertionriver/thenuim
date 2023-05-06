package river.exertion.kcop.view.messaging.menuParams

data class ActionParam(var label : String, var action : () -> Unit, var log : String? = null)