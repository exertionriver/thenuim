package river.exertion.thenuim.ecs.klop

import river.exertion.thenuim.base.IKlop
import river.exertion.thenuim.ecs.EngineHandler

interface IECSKlop : IKlop {

    fun loadSystems()
    fun unloadSystems()

    override fun dispose() {
        EngineHandler.dispose()

        super.dispose()
    }
}