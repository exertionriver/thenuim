package river.exertion.thenuim.automation

import river.exertion.thenuim.automation.entity.AutoUserEntity
import river.exertion.thenuim.automation.system.AutoUserSystem
import river.exertion.thenuim.base.Id
import river.exertion.thenuim.base.TnmBase
import river.exertion.thenuim.ecs.EngineHandler
import river.exertion.thenuim.ecs.IECSLoPa
import river.exertion.thenuim.messaging.IMessagingLoPa

object AutomationLoPa : IMessagingLoPa, IECSLoPa {

    override val id = Id.randomId()
    override val tag = this::class.simpleName.toString()
    override val name = TnmBase.appName
    override val version = TnmBase.appVersion

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
        super<IMessagingLoPa>.dispose()
        super<IECSLoPa>.dispose()
    }
}