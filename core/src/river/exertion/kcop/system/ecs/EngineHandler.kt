package river.exertion.kcop.system.ecs

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentType
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import ktx.ashley.entity
import ktx.ashley.remove
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.system.ecs.component.IComponent
import river.exertion.kcop.system.ecs.entity.IEntity
import river.exertion.kcop.system.messaging.*
import river.exertion.kcop.system.messaging.messages.EngineComponentMessage
import river.exertion.kcop.system.messaging.messages.EngineComponentMessageType
import river.exertion.kcop.system.messaging.messages.EngineEntityMessage
import river.exertion.kcop.system.messaging.messages.EngineEntityMessageType
import kotlin.reflect.KClass
import kotlin.reflect.full.valueParameters

class EngineHandler : Telegraph {

    init {
        MessageChannel.ECS_ENGINE_ENTITY_BRIDGE.enableReceive(this)
        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.enableReceive(this)
    }

    val engine = PooledEngine().apply { SystemManager.init(this) }
    val entities = mutableMapOf<IEntity, Entity>()

    inline fun <reified T:Component> has(entity : Entity) : Boolean { return entity.components.firstOrNull{ it is T } != null }
    inline fun <reified T:Component> getFor(entity : Entity) : T? = if (has<T>(entity)) entity.components.firstOrNull { it is T } as T else null

    inline fun <reified T:Component> getFirst() : Entity? = engine.entities.firstOrNull { entity -> entity.components.firstOrNull { it is T } != null }
    inline fun <reified T:Component> getAll() : List<Entity> = engine.entities.filter { entity -> entity.components.firstOrNull { it is T } != null }

    fun removeEntity(entityName : String) {
        val removeEntityEntry = entities.entries.filter { entityEntry -> (entityEntry.key.entityName == entityName) }.firstOrNull()

        if (removeEntityEntry != null) {
            engine.removeEntity(removeEntityEntry.value)
            entities.remove(removeEntityEntry.key)
        }
    }

    inline fun <reified T: IEntity> removeEntities() {
        val removeEntityEntryList = entities.filter { entityEntry -> (entityEntry.key is T) }
        removeEntityEntryList.forEach { entityEntry ->
            engine.removeEntity(entityEntry.value)
            entities.remove(entityEntry.key)
        }
    }

    fun instantiateEntity(entityClass : Class<*>, initInfo : Any? = null) {
        val newEntity = engine.entity()
        val instance = entityClass.getDeclaredConstructor().newInstance()
        val initMethod = entityClass.getMethod(
            IEntity::initialize.name,
            (IEntity::initialize.valueParameters[0].type.classifier as KClass<*>).java,
            (IEntity::initialize.valueParameters[1].type.classifier as KClass<*>).java)
        initMethod.invoke(instance, newEntity, initInfo)

        entities[instance as IEntity] = newEntity
    }

    fun addComponent(entityName : String, componentClass : Class<*>, initInfo : Any? = null) {
        val entityEntry = entities.entries.firstOrNull { it.key.entityName == entityName }
        val instance = componentClass.getDeclaredConstructor().newInstance()
        val initMethod = componentClass.getMethod(
            IComponent::initialize.name,
            (IComponent::initialize.valueParameters[0].type.classifier as KClass<*>).java,
            (IComponent::initialize.valueParameters[1].type.classifier as KClass<*>).java)
        initMethod.invoke(instance, entityName, initInfo)

        engine.entities.firstOrNull{ it == entityEntry?.value }?.add(instance as Component)
    }

    fun removeComponent(entityName : String, componentClass: Class<*>) {
        val entityEntry = entities.entries.filter { entityEntry -> (entityEntry.key.entityName == entityName) }.firstOrNull()

        if (entityEntry != null) {
            if (componentClass.interfaces.contains(Component::class.java)) {
                @Suppress("UNCHECKED_CAST")
                engine.entities.first().remove(componentClass as Class<Component>)
            }
        }
    }

    //initInfo is an already-initialized component instance
    fun replaceComponent(entityName : String, componentClass : Class<*>, initInfo : Any? = null) {
        val entityEntry = entities.entries.firstOrNull { it.key.entityName == entityName }

        removeComponent(entityName, componentClass)

        engine.entities.firstOrNull{ it == entityEntry?.value }?.add(initInfo as Component)
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.ECS_ENGINE_ENTITY_BRIDGE.isType(msg.message) ) -> {
                    val engineEntityMessage: EngineEntityMessage = MessageChannel.ECS_ENGINE_ENTITY_BRIDGE.receiveMessage(msg.extraInfo)

                    when (engineEntityMessage.messageType) {
                        EngineEntityMessageType.INSTANTIATE_ENTITY -> {
                            instantiateEntity(engineEntityMessage.entityClass, engineEntityMessage.initInfo)
                        }
                        EngineEntityMessageType.REMOVE_ENTITY -> {
                            removeEntity((engineEntityMessage.initInfo as ProfileAsset).profile!!.id)
                        }
                        EngineEntityMessageType.REMOVE_ALL_ENTITIES -> {
                            removeEntities<IEntity>()
                        }
                    }
                }
                (MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.isType(msg.message) ) -> {
                    val engineComponentMessage: EngineComponentMessage = MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.receiveMessage(msg.extraInfo)

                    when (engineComponentMessage.messageType) {
                        EngineComponentMessageType.ADD_COMPONENT -> {
                            addComponent(engineComponentMessage.entityId, engineComponentMessage.componentClass, engineComponentMessage.initInfo)
                        }
                        EngineComponentMessageType.REMOVE_COMPONENT -> {
                            removeComponent(engineComponentMessage.entityId, engineComponentMessage.componentClass)
                        }
                        EngineComponentMessageType.REPLACE_COMPONENT -> {
                            replaceComponent(engineComponentMessage.entityId, engineComponentMessage.componentClass, engineComponentMessage.initInfo)
                        }
                    }
                }
            }
        }
        return true
    }
}