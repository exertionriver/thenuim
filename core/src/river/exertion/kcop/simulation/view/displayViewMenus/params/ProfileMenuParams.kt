package river.exertion.kcop.simulation.view.displayViewMenus.params

import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.system.profile.Profile

data class ProfileMenuParams(val profileAssets : List<ProfileAsset>? = null, var narrativeAssets: List<NarrativeAsset>? = null, var currentProfile: Profile? = null)
