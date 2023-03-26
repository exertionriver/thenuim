package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.assets.AssetManager
import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.assets.ProfileAsset

interface ProfileReqMenu {

    var profile : ProfileAsset?
    var currentNarrativeAsset : NarrativeAsset?
    val am : AssetManager

}