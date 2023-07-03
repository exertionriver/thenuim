package river.exertion.kcop.asset

interface IAssetStore {
    fun load()
    fun get() : Any
}