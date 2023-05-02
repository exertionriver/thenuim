package river.exertion.kcop.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import kotlinx.serialization.json.encodeToJsonElement
import ktx.assets.getAsset
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent
import river.exertion.kcop.system.profile.ProfileSetting
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.profile.Profile

class ProfileAsset(var profile : Profile? = null) : IAsset {
    override lateinit var assetPath : String
    override var status : String? = null
    override var statusDetail : String? = null

    override fun assetId() = if (profile != null) profile?.id!! else throw Exception("ProfileAsset::assetId() profile is null")
    override fun assetName() = if (profile != null) profile?.name!! else throw Exception("ProfileAsset::assetName() profile is null")
    override fun assetTitle() = assetPath
    override fun newAssetFilename(): String = ProfileAssets.profileAssetPath(super.newAssetFilename())

    var settings : MutableList<ProfileSetting>
        get() = profile?.settings ?: mutableListOf()
        set(value) { profile?.settings = value }

    var cumlTime : String
        get() = profile?.cumlTime ?: ImmersionTimer.CumlTimeZero
        set(value) { profile?.cumlTime = value }

    //TODO: diff between asset and current
    override fun assetInfo() : List<String> {

        val returnList = mutableListOf<String>()

        if (profile != null) {
            returnList.add("path: $assetPath")
            returnList.addAll(profile!!.profileInfo())
        } else {
            returnList.add("no profile info found")
        }

        return returnList.toList()
    }

    fun save(renameAssetPath : String? = null) {
        val jsonProfile = AssetManagerHandlerCl.json.encodeToJsonElement(this.profile)

        if (renameAssetPath == null) {
            Gdx.files.local(assetPath).writeString(jsonProfile.toString(), false)
        } else {
            Gdx.files.local(assetPath).writeString(jsonProfile.toString(), false)
            if (renameAssetPath != assetPath) {
                Gdx.files.local(assetPath).moveTo(Gdx.files.local(renameAssetPath))
            }
        }
    }

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

        fun new(profileComponent: ProfileComponent, narrativeImmersionComponent : NarrativeComponent?) : ProfileAsset {
            return ProfileAsset(Profile().apply {
                this.name = profileComponent.profile!!.name
            } ).apply {
                this.update(profileComponent, narrativeImmersionComponent)
            }
        }
    }
}