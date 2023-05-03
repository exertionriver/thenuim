package river.exertion.kcop.sim.menu.params

data class ActionParam(var label : String, var action : () -> Unit, var log : String? = null)