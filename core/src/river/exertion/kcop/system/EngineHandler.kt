package river.exertion.kcop.system

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import ktx.ashley.entity
import river.exertion.kcop.system.entity.IEntity
import kotlin.reflect.KClass
import kotlin.reflect.full.valueParameters

class EngineHandler : Telegraph {

    init {
        MessageChannel.ECS_ENGINE_BRIDGE.enableReceive(this)
    }

    val engine = PooledEngine().apply { SystemManager.init(this) }
    val entities = mutableMapOf<IEntity, Entity>()

    inline fun <reified T:Component> has(entity : Entity) : Boolean { return entity.components.firstOrNull{ it is T } != null }
    inline fun <reified T:Component> getFor(entity : Entity) : T? = if (has<T>(entity)) entity.components.firstOrNull { it is T } as T else null

    inline fun <reified T:Component> getFirst() : Entity? = engine.entities.firstOrNull { entity -> entity.components.firstOrNull { it is T } != null }
    inline fun <reified T:Component> getAll() : List<Entity> = engine.entities.filter { entity -> entity.components.firstOrNull { it is T } != null }

    fun remove(entityName : String) {
        val removeEntityEntry = entities.entries.filter { entityEntry -> (entityEntry.key.entityName == entityName) }.firstOrNull()

        if (removeEntityEntry != null) {
            engine.removeEntity(removeEntityEntry.value)
            entities.remove(removeEntityEntry.key)
        }
    }

    inline fun <reified T:IEntity> removeAll() {
        val removeEntityEntryList = entities.filter { entityEntry -> (entityEntry.key is T) }
        removeEntityEntryList.forEach { entityEntry ->
            engine.removeEntity(entityEntry.value)
            entities.remove(entityEntry.key)
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            val engineMessage: EngineMessage = MessageChannel.ECS_ENGINE_BRIDGE.receiveMessage(msg.extraInfo)

            when (engineMessage.messageType) {
                EngineMessageType.INSTANTIATE_ENTITY -> {
                    val newEntity = engine.entity()
                    val instance = engineMessage.entityClass.getDeclaredConstructor().newInstance()
                    val initMethod = engineMessage.entityClass.getMethod(IEntity::initialize.name,
                        (IEntity::initialize.valueParameters[0].type.classifier as KClass<*>).java,
                        (IEntity::initialize.valueParameters[1].type.classifier as KClass<*>).java)
                    initMethod.invoke(instance, newEntity, engineMessage.initInfo)

                    entities[instance as IEntity] = newEntity
                }
                else -> {}
            }
        }
        return true
    }
}