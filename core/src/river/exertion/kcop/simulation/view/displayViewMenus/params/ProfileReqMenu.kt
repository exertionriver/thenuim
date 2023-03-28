package river.exertion.kcop.simulation.view.displayViewMenus.params

import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.system.profile.Profile

interface ProfileReqMenu {

    var profileAssets : List<ProfileAsset>
    var narrativeAssets : List<NarrativeAsset>

    var selectedProfileAsset : ProfileAsset?
    var selectedNarrativeAsset : NarrativeAsset?

    var currentProfile : Profile?
}