package river.exertion.kcop.base

interface IPackage {
    var id : String
    var name : String

    fun load()
    fun dispose()
}