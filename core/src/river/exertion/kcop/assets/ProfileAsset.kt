package river.exertion.kcop.assets

import river.exertion.kcop.system.profile.Profile

class ProfileAsset(var profile : Profile? = null) : LoadableAsset {
    override var status : String? = null
    override var statusDetail : String? = null
    override var assetPath : String? = null
}