package river.exertion.kcop.view.messaging

data class MenuViewMessage(val targetMenuTag : String? = null, val menuButtonIdx : Int? = null, var isChecked : Boolean = false)
