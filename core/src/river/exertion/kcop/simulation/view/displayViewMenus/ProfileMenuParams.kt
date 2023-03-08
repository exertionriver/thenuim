package river.exertion.kcop.simulation.view.displayViewMenus

import river.exertion.kcop.assets.ProfileAsset

data class ProfileMenuParams(override val targetMenuTag : String, var profile : ProfileAsset? = null) : MenuParams
