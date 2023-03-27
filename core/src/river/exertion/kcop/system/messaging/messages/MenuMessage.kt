package river.exertion.kcop.system.messaging.messages

import river.exertion.kcop.simulation.view.displayViewMenus.params.NavMenuParams
import river.exertion.kcop.simulation.view.displayViewMenus.params.ProfileMenuParams

data class MenuMessage(var navMenuParams: NavMenuParams? = null, var profileMenuParams: ProfileMenuParams? = null)
