package river.exertion.thenuim.profile.asset

import river.exertion.thenuim.asset.AssetManagerHandler
import river.exertion.thenuim.asset.AssetStatus
import river.exertion.thenuim.asset.IAsset
import river.exertion.thenuim.asset.immersionTimer.ImmersionTimer
import river.exertion.thenuim.profile.Profile
import river.exertion.thenuim.profile.settings.ProfileSettingEntry

class ProfileAsset(var profile : Profile = Profile()) : IAsset {

    override fun assetData() : Any = profile

    override fun assetId() = profile.id
    override fun assetName() = profile.name

    override fun assetPath() : String = newAssetFilename()
    override fun assetTitle() = assetPath()

    override var assetStatus : AssetStatus? = null
    override var persisted : Boolean = false

    override fun newAssetFilename(): String = ProfileAssets.iAssetPath(super.newAssetFilename())

    var settings : MutableList<ProfileSettingEntry>
        get() = profile.settingEntries
        set(value) { profile.settingEntries = value }

    var cumlTimer : ImmersionTimer
        get() = profile.cumlTimer
        set(value) { profile.cumlTimer = value }

    //TODO: diff between asset and current
    override fun assetInfo() : List<String> {

        val returnList = mutableListOf<String>()

        returnList.add("path: ${assetPath()}")
        returnList.addAll(profile.profileInfo())

        return returnList.toList()
    }

    override fun saveTyped(assetSaveLocation : String?) {
        persisted = AssetManagerHandler.saveAsset<Profile>(this, assetSaveLocation).persisted
    }

    override fun save(assetSaveLocation : String?) {
        //used to update serializable field
        cumlTimer = cumlTimer

        this.saveTyped(assetSaveLocation)
    }

    companion object {

        var selectedProfileAsset = ProfileAsset()
        var currentProfileAsset = ProfileAsset()

        fun new(saveName : String? = null) : ProfileAsset {
            return ProfileAsset(Profile().apply {
                this.name = saveName ?: this.name
            } )
        }
    }
}