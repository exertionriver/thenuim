package river.exertion.kcop.profile

import com.badlogic.gdx.assets.AssetManager
import river.exertion.kcop.asset.AssetManagerHandler.lfhr
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.plugin.IPlugin
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.profile.asset.ProfileAssetLoader
import river.exertion.kcop.profile.asset.ProfileAssets

class ProfilePlugin : IPlugin {
    override var id = Id.randomId()
    override var name = "profilePlugin"

    var profileAssets = ProfileAssets()

    override fun loadAssets(assetManager: AssetManager) {
        assetManager.setLoader(ProfileAsset::class.java, ProfileAssetLoader(lfhr))
        profileAssets.reload()
    }
}