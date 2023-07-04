package river.exertion.kcop.ecs.klop

import river.exertion.kcop.base.IKlop
import river.exertion.kcop.ecs.EngineHandler

interface IECSKlop : IKlop {

    fun loadSystems()

    override fun dispose() {
        EngineHandler.dispose()

        super.dispose()
    }
}