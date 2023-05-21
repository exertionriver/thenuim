package river.exertion.kcop.ecs

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.entity
import river.exertion.kcop.ecs.component.IComponent
import river.exertion.kcop.ecs.entity.IEntity
import river.exertion.kcop.ecs.entity.SubjectEntity
import river.exertion.kcop.ecs.system.SystemHandler
import kotlin.reflect.KClass
import kotlin.reflect.full.valueParameters

object EngineHandler {

    var engine = SystemHandler.pooledEngine
    val entities = mutableMapOf<IEntity, Entity>()
    val subjectEntity = instantiateEntity(SubjectEntity::class.java)

    fun entityByName(entityName : String) : Entity = entities.entries.firstOrNull{ it.key.entityName == entityName }?.value ?: throw Exception("entityByName:$entityName not found")

    inline fun <reified T:Component> hasComponent(entityName : String) : Boolean = hasComponent<T>(entityByName(entityName))
    inline fun <reified T:Component> hasComponent(entity : Entity) : Boolean { return entity.components.firstOrNull{ it is T } != null }

    inline fun <reified T:Component> getComponentFor(entityName : String) : T? = getComponentFor(entityByName(entityName))
    inline fun <reified T:Component> getComponentFor(entity : Entity) : T? = if (hasComponent<T>(entity)) entity.components.firstOrNull { it is T } as T else null

    inline fun <reified T:Component> getFirst() : Entity? = engine.entities.firstOrNull { entity -> entity.components.firstOrNull { it is T } != null }
    inline fun <reified T:Component> getAll() : List<Entity> = engine.entities.filter { entity -> entity.components.firstOrNull { it is T } != null }

    fun removeEntity(removeIEntity : IEntity) {
        engine.removeEntity(entities[removeIEntity])
        entities.remove(removeIEntity)
    }

    fun removeEntity(entityClass : Class<*>) {
        val removeEntityEntry = entities.entries.filter { entityEntry -> (entityEntry.key.javaClass == entityClass) }.firstOrNull()

        if (removeEntityEntry != null) {
            removeEntity(removeEntityEntry.key)
        }
    }

    fun instantiateEntity(entityClass : Class<*>, initInfo : Any? = null) : Entity {
        val newEntity = engine.entity()
        val instance = entityClass.getDeclaredConstructor().newInstance()
        val initMethod = entityClass.getMethod(
            IEntity::initialize.name,
            (IEntity::initialize.valueParameters[0].type.classifier as KClass<*>).java,
            (IEntity::initialize.valueParameters[1].type.classifier as KClass<*>).java)

        entities[instance as IEntity] = newEntity
        initMethod.invoke(instance, newEntity, initInfo)

        return newEntity
    }

    fun removeComponent(entityName : String = SubjectEntity.entityName, componentClass: Class<*>) {
        val entityEntry = entities.entries.filter { entityEntry -> (entityEntry.key.entityName == entityName) }.firstOrNull()

        if (entityEntry != null) {
            val componentToRemove = entityEntry.value.components.filter { componentEntry -> (componentEntry.javaClass == componentClass) }.firstOrNull()

            if (componentToRemove != null && (componentClass.interfaces.contains(IComponent::class.java))) {
                (componentToRemove as IComponent).dispose()
                @Suppress("UNCHECKED_CAST")
                entityEntry.value.remove(componentClass as Class<Component>)
            }
        }
    }

    fun addComponentInstance(entity : Entity = subjectEntity, componentInstance : IComponent) {
        engine.entities.firstOrNull { it == entity }?.add(componentInstance)
    }

    fun addInstantiateComponent(entity : Entity = subjectEntity, componentClass : Class<*>, initInfo : Any? = null) {
        val instance = componentClass.getDeclaredConstructor().newInstance()
        val initMethod = componentClass.getMethod(
            IComponent::initialize.name,
            (IComponent::initialize.valueParameters[0].type.classifier as KClass<*>).java
        )

        addComponentInstance(entity, instance as IComponent)
        initMethod.invoke(instance, initInfo)
    }

    fun addComponent(entityName : String = SubjectEntity.entityName, componentClass : Class<*>, initInfo : Any? = null) {
        val entityEntry = entities.entries.firstOrNull { it.key.entityName == entityName }

        if (entityEntry != null) {
            addInstantiateComponent(entityEntry.value, componentClass, initInfo)
        }
    }

    fun replaceComponent(entityName : String = SubjectEntity.entityName, componentClass : Class<*>, initInfo : Any? = null) {
        removeComponent(entityName, componentClass)
        addComponent(entityName, componentClass, initInfo)
    }

    fun dispose() { }
}