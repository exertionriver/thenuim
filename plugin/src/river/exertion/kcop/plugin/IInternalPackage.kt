package river.exertion.kcop.plugin

interface IInternalPackage : IPackage {

    override fun load() {
        loadChannels()
    }

    fun loadChannels()
}