package river.exertion.kcop.simulation.view.displayViewMenus.params

import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.system.profile.Profile

data class NavMenuParams(val targetMenuTag : String, val selectedProfileAsset : ProfileAsset? = null, val selectedNarrativeAsset : NarrativeAsset? = null, var currentProfile: Profile? = null)
