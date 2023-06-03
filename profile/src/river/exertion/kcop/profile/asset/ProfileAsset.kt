package river.exertion.kcop.profile.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import kotlinx.serialization.json.encodeToJsonElement
import ktx.assets.getAsset
import river.exertion.kcop.asset.AssetManagerHandler.json
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAsset.Companion.AssetNotFound
import river.exertion.kcop.asset.immersionTimer.ImmersionTimer
import river.exertion.kcop.profile.Profile
import river.exertion.kcop.profile.component.ProfileComponent
import river.exertion.kcop.profile.settings.ProfileSettingEntry

class ProfileAsset(var profile : Profile = Profile()) : IAsset {
    override var assetPath : String? = newAssetFilename()
    override var status : String? = null
    override var statusDetail : String? = null
    override var persisted : Boolean = false

    override fun assetId() = profile.id
    override fun assetName() = profile.name
    override fun assetTitle() = assetPath ?: AssetNotFound
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

    fun save(renameAssetPath : String? = null) {
        //used to update serializable field
        cumlTimer = cumlTimer

        val jsonProfile = json.encodeToJsonElement(profile)

        if (renameAssetPath == null) {
            Gdx.files.local(assetPath).writeString(jsonProfile.toString(), false)
        } else {
            Gdx.files.local(assetPath).writeString(jsonProfile.toString(), false)
            if (renameAssetPath != assetPath) {
                Gdx.files.local(assetPath).moveTo(Gdx.files.local(renameAssetPath))
            }
        }

        persisted = true
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