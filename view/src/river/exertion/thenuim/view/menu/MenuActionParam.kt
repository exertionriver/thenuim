package river.exertion.thenuim.view.menu

data class MenuActionParam(var label : String, var action : () -> Unit, var log : String? = null, var enabled : Boolean = true)