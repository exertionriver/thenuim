package river.exertion.kcop.assets

import com.badlogic.gdx.Gdx
import kotlinx.serialization.json.encodeToJsonElement
import river.exertion.kcop.Util
import river.exertion.kcop.system.profile.Profile

class ProfileAsset(var profile : Profile? = null, override var assetPath : String) : LoadableAsset {
    override var status : String? = null
    override var statusDetail : String? = null

    fun profileAssetTitle() = assetPath

    fun profileAssetName() = profile?.name

    //TODO: diff between asset and current
    fun profileAssetInfo(currentProfile : Profile? = null) : List<String> {

        val returnList = mutableListOf<String>()

        if (profile != null) {
            returnList.add("path: $assetPath")
            if (currentProfile == null)
                returnList.addAll(profile!!.profileInfo())
            else
                returnList.addAll(currentProfile.profileInfo())
        }

        if ( returnList.isEmpty() ) returnList.add("no profile info found")

        return returnList.toList()
    }

    fun save() {
        val jsonProfile = Util.json.encodeToJsonElement(this.profile)
        Gdx.files.local(assetPath).writeString(jsonProfile.toString(), false)
    }
}