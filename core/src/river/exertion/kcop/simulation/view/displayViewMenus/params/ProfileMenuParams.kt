package river.exertion.kcop.simulation.view.displayViewMenus.params

import river.exertion.kcop.assets.ProfileAsset

data class ProfileMenuParams(override val targetMenuTag : String, var profileAsset : ProfileAsset? = null) : MenuParams
