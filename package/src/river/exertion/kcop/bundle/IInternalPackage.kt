package river.exertion.kcop.bundle

interface IInternalPackage : IPackage {

    override fun load() {
        loadChannels()
    }

    fun loadChannels()
}