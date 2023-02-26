package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.kcop.narrative.structure.Narrative
import ktx.assets.load

class NarrativeAssetLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<NarrativeAsset?, NarrativeAssetLoader.NarrativeSequenceParameter?>(resolver) {

    val json = Json { ignoreUnknownKeys = true }
    lateinit var rawData: String

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: NarrativeSequenceParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeSequenceParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeSequenceParameter?): NarrativeAsset {
        try {
            rawData = file.readString()
            val jsonElement = json.parseToJsonElement(rawData)
            val narrative = json.decodeFromJsonElement(jsonElement) as Narrative

            val returnNarrativeAsset = NarrativeAsset(narrative)

            narrative.eventBlocks.forEach { eventBlock ->
                eventBlock.events.forEach { event ->
                    if (event.event.contains("showImage") && !narrative.textures.keys.contains(event.param)) {
                        narrative.textures[event.param] = manager.load(event.param)
                    }
                    if (event.event.contains("playSound") && !narrative.sounds.keys.contains(event.param)) {
                        narrative.sounds[event.param] = manager.load(event.param)
                    }
                    if (event.event.contains("playMusic") && !narrative.music.keys.contains(event.param)) {
                        narrative.music[event.param] = manager.load(event.param)
                    }
                }
            }

            narrative.timelineEventBlocks.forEach { timelineEventBlock ->
                timelineEventBlock.timelineEvents.forEach { timelineEvent ->
                if ( !timelineEvent.validateFields() ) {
                    returnNarrativeAsset.status = "${narrative.id} not loaded"
                    if (returnNarrativeAsset.statusDetail == null)
                        returnNarrativeAsset.statusDetail = "invalid event type : ${timelineEvent.event} in ${timelineEventBlock.narrativeBlockId} for ${narrative.id}"
                    else
                        returnNarrativeAsset.statusDetail += "\ninvalid event type : ${timelineEvent.event} in ${timelineEventBlock.narrativeBlockId} for ${narrative.id}"
                    }
                }
            }

            narrative.timelineEvents.forEach { timelineEvent ->
                if ( !timelineEvent.validateFields() ) {
                    returnNarrativeAsset.status = "${narrative.id} not loaded"
                    if (returnNarrativeAsset.statusDetail == null)
                        returnNarrativeAsset.statusDetail = "invalid event type : ${timelineEvent.event} in <timelineEvents> for ${narrative.id}"
                    else
                        returnNarrativeAsset.statusDetail += "\ninvalid event type : ${timelineEvent.event} in <timelineEvents> for ${narrative.id}"
                }
            }
            return returnNarrativeAsset

        } catch (ex : Exception) {
            return NarrativeAsset().apply {
                this.status = "not loaded"
                this.statusDetail = ex.message
            }
        }
    }

    class NarrativeSequenceParameter : AssetLoaderParameters<NarrativeAsset?>()
}