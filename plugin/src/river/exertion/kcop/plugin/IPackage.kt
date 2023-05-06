package river.exertion.kcop.plugin

import com.badlogic.gdx.assets.AssetManager

interface IPackage {
    var id : String
    var name : String

    fun loadChannels()
    fun loadAssets(assetManager : AssetManager)
    fun loadMenus()
    fun loadSystems()

    fun dispose()
}