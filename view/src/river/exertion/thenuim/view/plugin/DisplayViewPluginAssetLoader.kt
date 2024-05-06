package river.exertion.thenuim.view.plugin

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import river.exertion.thenuim.asset.AssetStatus
import river.exertion.thenuim.view.klop.IDisplayViewKlop
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile

class DisplayViewPluginAssetLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<DisplayViewPluginAsset?, AssetLoaderParameters<DisplayViewPluginAsset?>>(resolver) {

    override fun getDependencies(
        fileName: String?,
        file: FileHandle?,
        parameter: AssetLoaderParameters<DisplayViewPluginAsset?>?
    ): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(
        manager: AssetManager,
        fileName: String,
        file: FileHandle,
        parameter: AssetLoaderParameters<DisplayViewPluginAsset?>?
    ) {
    }

    override fun loadSync(
        manager: AssetManager,
        fileName: String,
        file: FileHandle,
        parameter: AssetLoaderParameters<DisplayViewPluginAsset?>?
    ): DisplayViewPluginAsset {
        var returnPluginAsset = DisplayViewPluginAsset().apply {
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

                //TODO: general plugin registry
                if ( jarEntry.name.endsWith("Klop.class") ) {
                    if ( loadedClass.interfaces.contains(IDisplayViewKlop::class.java) && DisplayViewPluginAssets.byId(className) == null) {
                        @Suppress("UNCHECKED_CAST")
                        returnPluginAsset = DisplayViewPluginAsset((classLoader.loadClass(className) as Class<IDisplayViewKlop>))
                    }
                }
            }

            returnPluginAsset.persisted = true //loaded from fs

            return returnPluginAsset

        } catch (ex: Exception) {
            return returnPluginAsset.apply {
                this.assetStatus = AssetStatus(this.assetPath(), "exception:asset not loaded", ex.message)
            }
        }
    }
}