package river.exertion.kcop.system.ecs

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import ktx.ashley.entity
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

    fun entityByName(entityName : String) : Entity = entities.entries.firstOrNull{ it.key.entityName == entityName }?.value ?: throw Exception("entityByName:$entityName not found")

    inline fun <reified T:Component> hasComponent(entityName : String) : Boolean = hasComponent<T>(entityByName(entityName))
    inline fun <reified T:Component> hasComponent(entity : Entity) : Boolean { return entity.components.firstOrNull{ it is T } != null }

    inline fun <reified T:Component> getComponentFor(entityName : String) : T? = getComponentFor(entityByName(entityName))
    inline fun <reified T:Component> getComponentFor(entity : Entity) : T? = if (hasComponent<T>(entity)) entity.components.firstOrNull { it is T } as T else null

    inline fun <reified T:Component> getFirst() : Entity? = engine.entities.firstOrNull { entity -> entity.components.firstOrNull { it is T } != null }
    inline fun <reified T:Component> getAll() : List<Entity> = engine.entities.filter { entity -> entity.components.firstOrNull { it is T } != null }

    fun removeEntity(removeIEntity : IEntity) {
        engine.removeEntity(entities[removeIEntity])

        removeIEntity.dispose()
        entities.remove(removeIEntity)
    }

    fun removeEntity(entityName : String) {
        val removeEntityEntry = entities.entries.filter { entityEntry -> (entityEntry.key.entityName == entityName) }.firstOrNull()

        if (removeEntityEntry != null) {
            removeEntity(removeEntityEntry.key)
        }
    }

    inline fun <reified T: IEntity> removeEntities() {
        val removeEntityEntryList = entities.entries.filter { entityEntry -> (entityEntry.key is T) }

        removeEntityEntryList.forEach { removeEntityEntry ->
            removeEntity(removeEntityEntry.key)
        }
    }

    fun instantiateEntity(entityClass : Class<*>, initInfo : Any? = null) : String {
        val newEntity = engine.entity()
        val instance = entityClass.getDeclaredConstructor().newInstance()
        val initMethod = entityClass.getMethod(
            IEntity::initialize.name,
            (IEntity::initialize.valueParameters[0].type.classifier as KClass<*>).java,
            (IEntity::initialize.valueParameters[1].type.classifier as KClass<*>).java)

        entities[instance as IEntity] = newEntity
        initMethod.invoke(instance, newEntity, initInfo)

        return instance.entityName
    }

    fun removeComponent(entityName : String, componentClass: Class<*>) {
        val entityEntry = entities.entries.filter { entityEntry -> (entityEntry.key.entityName == entityName) }.firstOrNull()

        if (entityEntry != null) {
            if (componentClass.interfaces.contains(Component::class.java)) {
                val componentToRemove = engine.entities.first()

                (componentToRemove as IComponent).dispose()
                @Suppress("UNCHECKED_CAST")
                componentToRemove.remove(componentClass as Class<Component>)
            }
        }
    }

    fun addComponentInstance(entityName : String, entity : Entity, componentInstance : IComponent) {
        componentInstance.entityName = entityName
        engine.entities.firstOrNull { it == entity }?.add(componentInstance)
    }

    fun addInstantiateComponent(entityName : String, entity : Entity, componentClass : Class<*>, initInfo : Any? = null) : String? {
        val instance = componentClass.getDeclaredConstructor().newInstance()
        val initMethod = componentClass.getMethod(
            IComponent::initialize.name,
            (IComponent::initialize.valueParameters[0].type.classifier as KClass<*>).java,
            (IComponent::initialize.valueParameters[1].type.classifier as KClass<*>).java)

            addComponentInstance(entityName, entity, instance as IComponent)
            initMethod.invoke(instance, entityName, initInfo)

        return entityName
    }

    fun addComponent(entityName : String, componentClass : Class<*>, componentInstance : IComponent? = null, initInfo : Any? = null) {
        val entityEntry = entities.entries.firstOrNull { it.key.entityName == entityName }

        if (entityEntry != null) {
            if (componentInstance != null && componentInstance.isInitialized) {
                addComponentInstance(entityName, entityEntry.value, componentInstance)
            } else if (initInfo != null) {
                addInstantiateComponent(entityName, entityEntry.value, componentClass, initInfo)
            }
        }
    }

    fun replaceComponent(entityName : String, componentClass : Class<*>, componentInstance : IComponent? = null, initInfo : Any? = null) {
        removeComponent(entityName, componentClass)
        addComponent(entityName, componentClass, componentInstance, initInfo)
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
                            addComponent(engineComponentMessage.entityName, engineComponentMessage.componentClass, engineComponentMessage.componentInstance, engineComponentMessage.initInfo)
                        }
                        EngineComponentMessageType.REMOVE_COMPONENT -> {
                            removeComponent(engineComponentMessage.entityName, engineComponentMessage.componentClass)
                        }
                        EngineComponentMessageType.REPLACE_COMPONENT -> {
                            replaceComponent(engineComponentMessage.entityName, engineComponentMessage.componentClass, engineComponentMessage.componentInstance, engineComponentMessage.initInfo)
                        }
                    }
                }
            }
        }
        return true
    }
}