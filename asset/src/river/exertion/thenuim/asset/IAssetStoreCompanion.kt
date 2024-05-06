package river.exertion.thenuim.asset

interface IAssetStoreCompanion {
    fun loadAll()
    fun getAll() : List<Any>
}