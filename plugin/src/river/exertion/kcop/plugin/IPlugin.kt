package river.exertion.kcop.plugin

import com.badlogic.gdx.assets.AssetManager

interface IPlugin {
    var id : String
    var name : String

    fun loadAssets(assetManager : AssetManager)
}