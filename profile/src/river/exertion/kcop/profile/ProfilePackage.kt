package river.exertion.kcop.profile

import com.badlogic.gdx.assets.AssetManager
import river.exertion.kcop.asset.AssetManagerHandler.lfhr
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IPackage
import river.exertion.kcop.plugin.IPlugin
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.profile.asset.ProfileAssetLoader
import river.exertion.kcop.profile.asset.ProfileAssets
import river.exertion.kcop.profile.messaging.ProfileMenuDataMessage
import river.exertion.kcop.profile.messaging.ProfileMessage

class ProfilePackage : IPackage {
    override var id = Id.randomId()
    override var name = this::class.simpleName.toString()

    var profileAssets = ProfileAssets()

    override fun loadAssets(assetManager: AssetManager) {
        assetManager.setLoader(ProfileAsset::class.java, ProfileAssetLoader(lfhr))
        profileAssets.reload()
    }

    override fun loadChannels() {
        MessageChannelHandler.addChannel(MessageChannel(ProfileBridge, ProfileMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(ProfileMenuDataBridge, ProfileMenuDataMessage::class))
    }

    companion object {
        const val ProfileBridge = "ProfileBridge"
        const val ProfileMenuDataBridge = "ProfileMenuDataBridge"
    }
}