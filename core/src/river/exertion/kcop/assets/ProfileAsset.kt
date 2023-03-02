package river.exertion.kcop.assets

import com.badlogic.gdx.Gdx
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import river.exertion.kcop.system.profile.Profile

class ProfileAsset(var profile : Profile? = null) {
    var status : String? = null
    var statusDetail : String? = null
    var assetPath : String? = null
}