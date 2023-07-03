package river.exertion.kcop.asset

interface IAssetStoreCompanion {
    fun loadAll()
    fun getAll() : List<Any>
}