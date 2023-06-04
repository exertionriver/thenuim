package river.exertion.kcop.profile.asset

import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAsset.Companion.AssetNotFound
import river.exertion.kcop.asset.immersionTimer.ImmersionTimer
import river.exertion.kcop.profile.Profile
import river.exertion.kcop.profile.settings.ProfileSettingEntry

class ProfileAsset(var profile : Profile = Profile()) : IAsset {

    override fun assetData() : Any = profile

    override var assetId = profile.id
    override var assetName = profile.name

    override var assetPath : String? = newAssetFilename()
    override var assetTitle = assetPath ?: AssetNotFound

    override var status : String? = null
    override var statusDetail : String? = null
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

        returnList.add("path: $assetPath")
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
            } ).apply {
                this.assetPath = this.newAssetFilename()
            }
        }
    }
}