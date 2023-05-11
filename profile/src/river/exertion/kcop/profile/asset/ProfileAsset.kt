package river.exertion.kcop.profile.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import kotlinx.serialization.json.encodeToJsonElement
import ktx.assets.getAsset
import river.exertion.kcop.asset.AssetManagerHandler.json
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.profile.Profile
import river.exertion.kcop.profile.component.ProfileComponent
import river.exertion.kcop.profile.settings.ProfileSettingEntry

class ProfileAsset(var profile : Profile = Profile()) : IAsset {
    override var assetPath : String = newAssetFilename()
    override var status : String? = null
    override var statusDetail : String? = null

    override fun assetId() = profile.id
    override fun assetName() = profile.name
    override fun assetTitle() = assetPath
    override fun newAssetFilename(): String = ProfileAssets.profileAssetPath(super.newAssetFilename())

    var settings : MutableList<ProfileSettingEntry>
        get() = profile.settingEntries
        set(value) { profile.settingEntries = value }

    var cumlTime : String
        get() = profile.cumlTime
        set(value) { profile.cumlTime = value }

    //TODO: diff between asset and current
    override fun assetInfo() : List<String> {

        val returnList = mutableListOf<String>()

        returnList.add("path: $assetPath")
        returnList.addAll(profile.profileInfo())

        return returnList.toList()
    }

    fun save(renameAssetPath : String? = null) {
        val jsonProfile = json.encodeToJsonElement(this.profile)

        if (renameAssetPath == null) {
            Gdx.files.local(assetPath).writeString(jsonProfile.toString(), false)
        } else {
            Gdx.files.local(assetPath).writeString(jsonProfile.toString(), false)
            if (renameAssetPath != assetPath) {
                Gdx.files.local(assetPath).moveTo(Gdx.files.local(renameAssetPath))
            }
        }
    }
/*
    //when loading profileAsset
    fun updateFromImmersionAssets(narrativeAsset: NarrativeAsset?, narrativeImmersionAsset: NarrativeImmersionAsset?) {
        profile!!.currentImmersionName = narrativeAsset?.assetName()
        profile!!.currentImmersionBlockId = narrativeImmersionAsset?.narrativeCurrBlockId()
        profile!!.currentImmersionTime = narrativeImmersionAsset?.cumlImmersionTime()
    }

    fun update(profileComponent: ProfileComponent, narrativeImmersionComponent : NarrativeComponent?) {
        profile = profileComponent.profile
        profile!!.currentImmersionName = narrativeImmersionComponent?.narrativeName()
        profile!!.currentImmersionBlockId = narrativeImmersionComponent?.narrativeCurrBlockId()
        profile!!.currentImmersionTime = narrativeImmersionComponent?.cumlImmersionTime()

        this.assetPath = this.newAssetFilename()
    }
*/
    fun update(profileComponent: ProfileComponent) {
 //       profile = profileComponent.profile
    }

    companion object {

        operator fun AssetManager.get(asset: ProfileAsset) = getAsset<ProfileAsset>(asset.assetPath).also {
            if (it.status != null) println ("Asset Status: ${it.status}")
            if (it.statusDetail != null) println ("Status Detail: ${it.statusDetail}")
        }

        fun isValid(profileAsset: ProfileAsset?) : Boolean {
            return (profileAsset?.profile != null && profileAsset.status == null)
        }

        fun new(saveName : String? = null) : ProfileAsset {
            return ProfileAsset(Profile().apply {
                this.name = saveName ?: this.name
            } ).apply {
                this.assetPath = this.newAssetFilename()
            }
        }

        fun new(profileComponent: ProfileComponent) : ProfileAsset {
            return ProfileAsset(Profile().apply {
                this.name = profileComponent.profile!!.name
            } ).apply {
                this.update(profileComponent)
            }
        }
    }
}