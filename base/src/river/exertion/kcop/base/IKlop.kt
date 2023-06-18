package river.exertion.kcop.base

interface IKlop {
    var id : String
    var name : String

    fun load()
    fun unload()

    fun dispose()
}