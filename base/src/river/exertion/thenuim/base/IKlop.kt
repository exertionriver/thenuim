package river.exertion.thenuim.base

interface IKlop {
    val id : String
    val tag : String
    val name : String
    val version : String

    fun load()
    fun unload()

    fun title() = "$name $version"

    fun dispose() {}
}