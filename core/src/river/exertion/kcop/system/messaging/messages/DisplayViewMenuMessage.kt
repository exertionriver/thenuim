package river.exertion.kcop.system.messaging.messages

import river.exertion.kcop.simulation.view.displayViewMenus.MenuParams

data class DisplayViewMenuMessage(val menuButtonIdx : Int? = null, var menuParams : MenuParams? = null)
