package river.exertion.kcop.automation

import river.exertion.kcop.automation.entity.AutoUserEntity
import river.exertion.kcop.automation.system.AutoUserSystem
import river.exertion.kcop.base.Id
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.ecs.klop.IECSKlop
import river.exertion.kcop.messaging.klop.IMessagingKlop

object AutomationKlop : IMessagingKlop, IECSKlop {

    override var id = Id.randomId()

    override var tag = this::class.simpleName.toString()

    override fun load() {
        loadChannels()
        loadSystems()
    }

    override fun unload() { }

    override fun loadChannels() {
    }

    override fun loadSystems() {
        EngineHandler.instantiateEntity<AutoUserEntity>()

        EngineHandler.addSystem(AutoUserSystem())
    }

    override fun unloadSystems() {

    }

    override fun dispose() {
        super<IMessagingKlop>.dispose()
        super<IECSKlop>.dispose()
    }
}