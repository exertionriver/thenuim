package river.exertion.thenuim.automation

import river.exertion.thenuim.automation.entity.AutoUserEntity
import river.exertion.thenuim.automation.system.AutoUserSystem
import river.exertion.thenuim.base.Id
import river.exertion.thenuim.base.KcopBase
import river.exertion.thenuim.ecs.EngineHandler
import river.exertion.thenuim.ecs.klop.IECSKlop
import river.exertion.thenuim.messaging.klop.IMessagingKlop

object AutomationKlop : IMessagingKlop, IECSKlop {

    override val id = Id.randomId()
    override val tag = this::class.simpleName.toString()
    override val name = KcopBase.appName
    override val version = KcopBase.appVersion

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