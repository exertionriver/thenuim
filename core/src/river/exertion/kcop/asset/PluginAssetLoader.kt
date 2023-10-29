package river.exertion.kcop.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.kcop.asset.AssetManagerHandler.json
import river.exertion.kcop.sim.narrative.structure.Narrative
import river.exertion.kcop.sim.narrative.structure.events.IImageEvent
import river.exertion.kcop.sim.narrative.structure.events.ISoundEvent
import river.exertion.kcop.sim.narrative.structure.events.IMusicEvent
import river.exertion.kcop.view.klop.IDisplayViewKlop
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile

class PluginAssetLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<PluginAsset?, PluginAssetLoader.NarrativeSequenceParameter?>(resolver) {

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: NarrativeSequenceParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeSequenceParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeSequenceParameter?): PluginAsset {
        var returnPluginAsset = PluginAsset().apply {
            this.assetStatus = AssetStatus(this.assetPath(), "asset not loaded", "no plugin found:$fileName")
        }

        try {
            val jarPath = fileName
            val jarFile = JarFile(jarPath)
            val jarEnumeration = jarFile.entries()
            val classLoader = URLClassLoader.newInstance(arrayOf(URL("jar:file:$jarPath!/")))

            while (jarEnumeration.hasMoreElements()) {
                val jarEntry = jarEnumeration.nextElement()

                if (jarEntry.isDirectory || !jarEntry.name.endsWith(".class")) continue

                val className = jarEntry.name.substring(0, jarEntry.name.length - 6).replace('/', '.')

                val loadedClass = classLoader.loadClass(className)

                if (jarEntry.name.endsWith("Klop.class") &&
                    loadedClass.interfaces.contains(IDisplayViewKlop::class.java)) {
                    returnPluginAsset = PluginAsset((classLoader.loadClass(className) as Class<IDisplayViewKlop>))
                }
            }

            returnPluginAsset.persisted = true //loaded from fs

            return returnPluginAsset

        } catch (ex : Exception) {
            return returnPluginAsset.apply {
                this.assetStatus = AssetStatus(this.assetPath(), "exception:asset not loaded", ex.message)
            }
        }
    }

    class NarrativeSequenceParameter : AssetLoaderParameters<PluginAsset?>()
    }