package river.exertion.kcop.assets

import kotlin.reflect.KClass

interface IPlugin {
    var pluginPath : String
    var plugin : KClass<Any>
    var status : String?
    var statusDetail : String?

    fun pluginId() : String

    companion object {
    }
}