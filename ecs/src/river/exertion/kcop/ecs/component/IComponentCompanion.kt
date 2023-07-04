package river.exertion.kcop.ecs.component

import river.exertion.kcop.ecs.entity.SubjectEntity

interface IComponentCompanion {
    fun ecsInit(entityName : String = SubjectEntity.entityName, initData : Any? = null)
}