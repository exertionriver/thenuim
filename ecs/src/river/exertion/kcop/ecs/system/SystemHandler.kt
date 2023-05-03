package river.exertion.kcop.ecs.system

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx

object SystemHandler {

    var pooledEngine = PooledEngine()

//        pooledEngine.addSystem(NarrativeTextSystem())
//        pooledEngine.addSystem(AMHUpdateSystem())

    fun logDebug(tag : String, message : String) {
        if (Gdx.app != null)
            Gdx.app.debug(tag, message)
        else
            println("$tag: $message")
    }
}