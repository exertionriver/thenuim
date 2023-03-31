package river.exertion.kcop.system.messaging.messages

import river.exertion.kcop.simulation.view.displayViewMenus.params.NarrativeMenuDataParams
import river.exertion.kcop.simulation.view.displayViewMenus.params.ProfileMenuDataParams

data class MenuDataMessage(var profileMenuDataParams: ProfileMenuDataParams? = null, var narrativeMenuDataParams: NarrativeMenuDataParams? = null)
