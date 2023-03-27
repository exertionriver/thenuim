package river.exertion.kcop.simulation.view.displayViewMenus.params

data class ActionParam(var label : String, var action : () -> Unit, var log : String? = null)