package river.exertion.kcop.assets

import river.exertion.kcop.system.profile.Profile

class ProfileAsset(var profile : Profile? = null, override var assetPath : String) : LoadableAsset {
    override var status : String? = null
    override var statusDetail : String? = null

    fun profileAssetTitle() = profile?.name

    fun profileAssetInfo() : MutableList<String?> {

        val returnList = mutableListOf<String?>()

        if (profile != null) {
            returnList.add("name: ${profile!!.name}")
            returnList.add("path: ${assetPath}")
            returnList.add("current: ${profile!!.currentImmersionId}: ${profile!!.currentImmersionBlockId}")

            if (profile!!.statuses.isNotEmpty()) returnList.add("\nstatuses:")

            val listMaxSize = profile!!.statuses.size.coerceAtMost(8)

            profile!!.statuses.sortedByDescending { it.value }.subList(0, listMaxSize).forEach {
                returnList.add("${it.key}: ${it.value} (${it.cumlImmersionTime})")
            }
        }

        if ( returnList.isEmpty() ) returnList.add("no profile info found")

        return returnList
    }
}