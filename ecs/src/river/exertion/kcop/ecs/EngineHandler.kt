package river.exertion.kcop.ecs

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import ktx.ashley.entity
import river.exertion.kcop.base.Log
import river.exertion.kcop.ecs.component.IComponent
import river.exertion.kcop.ecs.entity.IEntity
import river.exertion.kcop.ecs.entity.SubjectEntity
import javax.security.auth.Subject
import kotlin.reflect.KClass
import kotlin.reflect.full.valueParameters

object EngineHandler {

    var engine = PooledEngine()
    val entities = mutableMapOf<IEntity, Entity>()
    val subjectEntity = instantiateEntity<SubjectEntity>()

    fun systemsSize() = engine.systems.size()
    fun entitiesSize() = engine.entities.size()

    fun iEntitiesSize() = entities.size

    fun update(deltaTime : Float) = engine.update(deltaTime)

    fun iEntityByName(entityName : String) : IEntity = entities.entries.firstOrNull { it.key.entityName == entityName }?.key ?: throw Exception("iEntityByName:$entityName not found")

    fun entityByName(entityName : String) : Entity = entities.entries.firstOrNull{ it.key.entityName == entityName }?.value ?: throw Exception("entityByName:$entityName not found")

    fun iEntityByEntity(entity : Entity) : IEntity = entities.entries.firstOrNull { it.value == entity }?.key ?: throw Exception("iEntityByEntity:$entity not found")

    fun removeEntity(entityName : String) {
        removeEntity(iEntityByName(entityName))
    }

    fun removeEntity(iEntity : IEntity) {
        engine.removeEntity(entities[iEntity])
        entities.remove(iEntity)
    }

    fun removeEntity(entityClass : KClass<*>) {
        val removeEntityEntry = entities.entries.firstOrNull { entityEntry -> (entityEntry.key::class == entityClass) }

        if (removeEntityEntry != null) {
            removeEntity(removeEntityEntry.key)
        }
    }

    inline fun <reified T:IEntity> instantiateEntity(initInfo : Any? = null) : Entity {
        val newEntity = engine.entity()
        val instance = T::class.java.getDeclaredConstructor().newInstance()
        val initMethod = T::class.java.getMethod(
            IEntity::initialize.name,
            (IEntity::initialize.valueParameters[0].type.classifier as KClass<*>).java,
            (IEntity::initialize.valueParameters[1].type.classifier as KClass<*>).java)

        entities[instance as IEntity] = newEntity
        initMethod.invoke(instance, newEntity, initInfo)

        return newEntity
    }

    inline fun <reified T:IComponent> hasComponent(entity : Entity) : Boolean = entity.components.firstOrNull{ it is T } != null
    inline fun <reified T:IComponent> getComponentFor(entity : Entity) : T? = if (hasComponent<T>(entity)) entity.components.firstOrNull { it is T } as T else null

    inline fun <reified T:IComponent> removeComponent(entityName : String = SubjectEntity.entityName) {
        val entityEntry = entityByName(entityName)

        val componentToRemove = entityEntry.components.firstOrNull { componentEntry -> (componentEntry::class == T::class) }

        if (componentToRemove != null && (T::class.java.interfaces.contains(IComponent::class.java))) {
            (componentToRemove as IComponent).dispose()
            @Suppress("UNCHECKED_CAST")
            entityEntry.remove(T::class.java as Class<Component>)
        }
    }

    inline fun <reified T:IComponent> addComponentInstance(entityName : String = SubjectEntity.entityName, componentInstance : IComponent) {
        val entityEntry = entityByName(entityName)

        if (!hasComponent<T>(entityEntry)) {
            engine.entities.firstOrNull { it == entityEntry }?.add(componentInstance)
        }
    }

    inline fun <reified T:IComponent> instantiateComponent(entityName : String = SubjectEntity.entityName, initInfo : Any? = null) {
        val instance = T::class.java.getDeclaredConstructor().newInstance()
        val initMethod = T::class.java.getMethod(
            IComponent::initialize.name,
            (IComponent::initialize.valueParameters[0].type.classifier as KClass<*>).java
        )

        addComponentInstance<T>(entityName, instance as IComponent)
        initMethod.invoke(instance, initInfo)
    }

    inline fun <reified T:IComponent> addComponent(entityName : String = SubjectEntity.entityName, initInfo : Any? = null) {
        val entityEntry = entityByName(entityName)

        if (!hasComponent<T>(entityEntry)) {
            instantiateComponent<T>(entityName, initInfo)
        }
    }

    inline fun <reified T:IComponent> replaceComponent(entityName : String = SubjectEntity.entityName, initInfo : Any? = null) {
        removeComponent<T>(entityName)
        addComponent<T>(entityName, initInfo)
    }

    fun addSystem(system : EntitySystem) = engine.addSystem(system)
    fun removeSystem(system : EntitySystem) = engine.removeSystem(system)

    fun dispose() {
        engine.removeAllEntities()
        engine.removeAllSystems()
        entities.clear()
    }
}