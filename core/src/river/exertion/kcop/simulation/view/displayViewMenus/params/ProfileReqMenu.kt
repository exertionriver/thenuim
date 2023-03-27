package river.exertion.kcop.simulation.view.displayViewMenus.params

import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.assets.ProfileAsset

interface ProfileReqMenu {

    var profileAssets : List<ProfileAsset>
    var narrativeAssets : List<NarrativeAsset>

    var selectedProfileAsset : ProfileAsset?
    var selectedNarrativeAsset : NarrativeAsset?
}