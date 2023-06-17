package river.exertion.kcop.bundle

interface IPackage {
    var id : String
    var name : String

    fun load()
    fun dispose()
}