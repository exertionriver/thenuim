package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.decodeFromJsonElement
import ktx.assets.load
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.narrative.structure.events.IImageEvent
import river.exertion.kcop.narrative.structure.events.IMusicEvent
import river.exertion.kcop.narrative.structure.events.ISoundEvent

class NarrativeAssetLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<NarrativeAsset?, NarrativeAssetLoader.NarrativeSequenceParameter?>(resolver) {

    lateinit var rawData: String

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: NarrativeSequenceParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeSequenceParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeSequenceParameter?): NarrativeAsset {
        try {
            rawData = file.readString()
            val jsonElement = AssetManagerHandler.json.parseToJsonElement(rawData)
            val narrative = AssetManagerHandler.json.decodeFromJsonElement(jsonElement) as Narrative

            val returnNarrativeAsset = NarrativeAsset(narrative).apply { this.assetPath = fileName }
            val errorStatus = "${narrative.name} not loaded"

            narrative.eventBlocks.forEach { eventBlock ->
                eventBlock.events.forEachIndexed { idx, event ->
/*                    if ( !event.validateFields() ) {
                        returnNarrativeAsset.status = errorStatus
                        val errorDetail = "invalid event type : ${event.eventType} in ${eventBlock.narrativeBlockId} for ${narrative.name}"
                        if (returnNarrativeAsset.statusDetail == null)
                            returnNarrativeAsset.statusDetail = errorDetail
                        else
                            returnNarrativeAsset.statusDetail += "\n$errorDetail"
                    }
*/
                    if (event.id == null) {
                        event.id = "${eventBlock.narrativeBlockId}_${idx}_${event.type}"
                    }

                    if (event.isImageEvent()) {
                        val imageEvent = event as IImageEvent
                        if (!narrative.textures.keys.contains(imageEvent.imageFile)) {
                            narrative.textures[imageEvent.imageFile] = manager.load(imageEvent.imageFile)
                        }
                    }
                    if (event.isSoundEvent()) {
                        val soundEvent = event as ISoundEvent
                        if (!narrative.sounds.keys.contains(soundEvent.musicFile)) {
                            narrative.sounds[soundEvent.musicFile] = manager.load(soundEvent.musicFile)
                        }
                    }
                    if (event.isMusicEvent()) {
                        val musicEvent = event as IMusicEvent
                        if (!narrative.music.keys.contains(musicEvent.musicFile)) {
                            narrative.music[musicEvent.musicFile] = manager.load(musicEvent.musicFile)
                        }
                    }
                }
            }

            narrative.timelineEventBlocks.forEach { timelineEventBlock ->
                timelineEventBlock.events.forEachIndexed { idx, timelineEvent ->
                    if (timelineEvent.id == null) {
                        timelineEvent.id = "${timelineEventBlock.narrativeBlockId}_${idx}_${timelineEvent.type}"
                    }

                    /*                if ( !timelineEvent.validateFields() ) {
                                        returnNarrativeAsset.status = errorStatus
                                        val errorDetail = "invalid event type : ${timelineEvent.eventType} in ${timelineEventBlock.narrativeBlockId} for ${narrative.name}"
                                        if (returnNarrativeAsset.statusDetail == null)
                                            returnNarrativeAsset.statusDetail = errorDetail
                                        else
                                            returnNarrativeAsset.statusDetail += "\n$errorDetail"                    }
                      */              }
            }

            narrative.timelineEvents.forEachIndexed { idx, timelineEvent ->
                if (timelineEvent.id == null) {
                    timelineEvent.id = "${narrative.name}_${idx}_${timelineEvent.type}"
                }
                /*               if ( !timelineEvent.validateFields() ) {
                    returnNarrativeAsset.status = errorStatus
                    val errorDetail = "invalid event type : ${timelineEvent.eventType} in <timelineEvents> for ${narrative.name}"
                    if (returnNarrativeAsset.statusDetail == null)
                        returnNarrativeAsset.statusDetail = errorDetail
                    else
                        returnNarrativeAsset.statusDetail += "\n$errorDetail"
                }
   */         }

            narrative.promptBlocks.forEach { promptBlock ->
                promptBlock.prompts.forEach { prompt ->
                    if (prompt.promptNextId != null && prompt.promptRandomId != null) {
                        returnNarrativeAsset.status = errorStatus
                        val errorDetail = "narrative ${narrative.name} block ${promptBlock.narrativeBlockId} prompt ${prompt.promptKey} cannot have both promptNextId and promptRandomId"
                        if (returnNarrativeAsset.statusDetail == null)
                            returnNarrativeAsset.statusDetail = errorDetail
                        else
                            returnNarrativeAsset.statusDetail += "\n$errorDetail"
                    }
                }
            }

            return returnNarrativeAsset

        } catch (ex : Exception) {
            return NarrativeAsset().apply {
                this.assetPath = fileName
                this.status = "$fileName not loaded"
                this.statusDetail = ex.message
            }
        }
    }

    class NarrativeSequenceParameter : AssetLoaderParameters<NarrativeAsset?>()
}