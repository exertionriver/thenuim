package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import ktx.assets.getAsset
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.EngineComponentMessage
import river.exertion.kcop.system.messaging.messages.EngineComponentMessageType

class NarrativeAsset(var narrative : Narrative? = null) : IAsset {
    override lateinit var assetPath : String
    override var status : String? = null
    override var statusDetail : String? = null

    override fun assetId() = if (narrative != null) narrative?.id!! else throw Exception("NarrativeAsset::assetId() narrative is null")
    override fun assetName() = if (narrative != null) narrative?.name!! else throw Exception("NarrativeAsset::assetName() narrative is null")
    override fun assetTitle() = assetPath

    override fun newAssetFilename(): String = NarrativeAssets.narrativeAssetPath(super.newAssetFilename())

    override fun assetInfo() : List<String> {

        val returnList = mutableListOf<String>()

        if (narrative != null) {
            returnList.add("path: $assetPath")
            returnList.addAll(narrative!!.narrativeInfo())
        } else {
            returnList.add("no narrative info found")
        }

        return returnList.toList()
    }

    fun initNarrative(narrativeImmersionAsset: NarrativeImmersionAsset? = null) {
        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessageType.REPLACE_COMPONENT,
                ProfileEntity.entityName, NarrativeComponent::class.java,
                NarrativeComponent.NarrativeComponentInit(this, narrativeImmersionAsset)
        ) )
    }

    fun update(narrativeImmersionComponent : NarrativeComponent?) {
        narrative = narrativeImmersionComponent?.narrative
    }

    companion object {
        operator fun AssetManager.get(asset: NarrativeAsset) = getAsset<NarrativeAsset>(asset.assetPath).also {
            if (it.status != null) println ("Asset Status: ${it.status}")
            if (it.statusDetail != null) println ("Status Detail: ${it.statusDetail}")
        }
    }
}