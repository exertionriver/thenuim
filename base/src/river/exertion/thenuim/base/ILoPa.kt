package river.exertion.thenuim.base

//Thenuim Loadable Package Interface
interface ILoPa {
    val id : String
    val tag : String
    val name : String
    val version : String

    fun load()
    fun unload()

    fun title() = "$name $version"

    fun dispose() {}
}