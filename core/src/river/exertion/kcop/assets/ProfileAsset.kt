package river.exertion.kcop.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import kotlinx.serialization.json.encodeToJsonElement
import ktx.assets.getAsset
import river.exertion.kcop.system.profile.Profile

class ProfileAsset(var profile : Profile? = null) : LoadableAsset {
    override lateinit var assetPath : String
    override var status : String? = null
    override var statusDetail : String? = null

    override fun assetId() = if (profile != null) profile?.id!! else throw Exception("ProfileAsset::assetId() profile is null")
    override fun assetName() = if (profile != null) profile?.name!! else throw Exception("ProfileAsset::assetName() profile is null")
    override fun assetTitle() = assetPath

    override fun newAssetFilename(): String = ProfileAssets.profileAssetPath(super.newAssetFilename())

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
        val jsonProfile = AssetManagerHandler.json.encodeToJsonElement(this.profile)

        if (renameAssetPath == null) {
            Gdx.files.local(assetPath).writeString(jsonProfile.toString(), false)
        } else {
            Gdx.files.local(assetPath).writeString(jsonProfile.toString(), false)
            if (renameAssetPath != assetPath) {
                Gdx.files.local(assetPath).moveTo(Gdx.files.local(renameAssetPath))
            }
        }
    }

    companion object {
        operator fun AssetManager.get(asset: ProfileAsset) = getAsset<ProfileAsset>(asset.assetPath).also {
            if (it.status != null) println ("Asset Status: ${it.status}")
            if (it.statusDetail != null) println ("Status Detail: ${it.statusDetail}")
        }
    }
}