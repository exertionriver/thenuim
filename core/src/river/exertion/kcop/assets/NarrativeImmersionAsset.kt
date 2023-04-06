package river.exertion.kcop.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import kotlinx.serialization.json.encodeToJsonElement
import ktx.assets.getAsset
import river.exertion.kcop.narrative.structure.NarrativeImmersion
import river.exertion.kcop.system.immersionTimer.ImmersionTimer

class NarrativeImmersionAsset(var narrativeImmersion : NarrativeImmersion? = null) : LoadableAsset {
    override lateinit var assetPath : String
    override var status : String? = null
    override var statusDetail : String? = null

    override fun assetId() = if (narrativeImmersion != null) narrativeImmersion?.id!! else throw Exception("NarrativeImmersionAsset::assetId() narrativeImmersion is null")

    override fun assetName() = assetId()

    override fun assetTitle() = assetPath

    override fun newAssetFilename(): String = NarrativeImmersionAssets.narrativeImmersionAssetPath(assetName())

    override fun assetInfo() : List<String> {

        val returnList = mutableListOf<String>()

        returnList.add(narrativeImmersion?.cumlImmersionTime() ?: "no narrative immersion info found")

        return returnList.toList()
    }

    fun cumlImmersionTime() = if (narrativeImmersion != null) narrativeImmersion?.cumlImmersionTime() else ImmersionTimer.zero()

    fun flags() = if (narrativeImmersion != null) narrativeImmersion?.flags else listOf()

    fun save() {
        assetPath = newAssetFilename()
        val jsonNarrativeImmersion = AssetManagerHandler.json.encodeToJsonElement(this.narrativeImmersion)
        Gdx.files.local(assetPath).writeString(jsonNarrativeImmersion.toString(), false)
    }

    fun delete() {
        Gdx.files.local(assetPath).delete()
    }


    companion object {
        fun genAssetId(profileId : String?, narrativeId : String?) = if (profileId != null && narrativeId != null) profileId + narrativeId else null

        operator fun AssetManager.get(asset: NarrativeImmersionAsset) = getAsset<NarrativeImmersionAsset>(asset.assetPath).also {
            if (it.status != null) println ("Asset Status: ${it.status}")
            if (it.statusDetail != null) println ("Status Detail: ${it.statusDetail}")
        }
    }
}