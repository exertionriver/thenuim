package river.exertion.kcop.plugin

interface IPackage {
    var id : String
    var name : String

    fun load()

    fun dispose()
}