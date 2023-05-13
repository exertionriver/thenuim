package river.exertion.kcop.plugin

interface IKcopPackage : IInternalPackage {

    override fun load() {
        super.load()

        loadAssets()
        loadSystems()
        loadMenus()
    }

    fun loadAssets()
    fun loadSystems()
    fun loadMenus()
}