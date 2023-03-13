package river.exertion.kcop.system.ecs

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import river.exertion.kcop.system.ecs.system.NarrativeTextSystem
import river.exertion.kcop.system.ecs.system.PositionSystem
import river.exertion.kcop.system.ecs.system.TimeLogSystem

object SystemManager {

    fun init(pooledEngine: PooledEngine) {
        pooledEngine.addSystem(NarrativeTextSystem())
        pooledEngine.addSystem(TimeLogSystem())
        pooledEngine.addSystem(PositionSystem())
    }

    fun logDebug(tag : String, message : String) {
        if (Gdx.app != null)
            Gdx.app.debug(tag, message)
        else
            println("$tag: $message")
    }
}