package river.exertion.kcop.base

interface IKlop {
    var id : String
    var tag : String

    fun load()
    fun unload()

    fun dispose() {}
}