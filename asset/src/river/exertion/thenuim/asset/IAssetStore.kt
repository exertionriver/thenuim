package river.exertion.thenuim.asset

interface IAssetStore {
    fun load()
    fun get() : Any
}