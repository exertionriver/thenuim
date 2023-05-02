package river.exertion.kcop.assets

import kotlin.reflect.KClass

interface IPlugin {
    var pluginPath : String
    var plugin : MutableList<KClass<*>>
    var status : String?
    var statusDetail : String?

    fun pluginId() : String



    companion object {
        const val SDCBridge = "SDCBridge"
        const val KCopBridge = "KCopBridge"
        const val KCopSkinBridge = "KCopSkinBridge"
    }
}