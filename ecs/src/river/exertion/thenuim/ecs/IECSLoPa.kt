package river.exertion.thenuim.ecs

import river.exertion.thenuim.base.ILoPa

interface IECSLoPa : ILoPa {

    fun loadSystems()
    fun unloadSystems()

    override fun dispose() {
        EngineHandler.dispose()

        super.dispose()
    }
}