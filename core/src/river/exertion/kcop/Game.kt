package river.exertion.kcop

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import river.exertion.kcop.system.SystemManager
import river.exertion.kcop.system.entity.Observer

class Game : ApplicationAdapter() {

    var batch: SpriteBatch? = null
    var img: Texture? = null
    val engine = PooledEngine().apply { SystemManager.init(this) }
    val observer = Observer.instantiate(engine)

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")

        Gdx.app.logLevel = Application.LOG_DEBUG
    }

    override fun render() {
        ScreenUtils.clear(1f, 0f, 0f, 1f)
        batch!!.begin()
        batch!!.draw(img, 0f, 0f)
        batch!!.end()

        engine.update(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        batch!!.dispose()
        img!!.dispose()
    }
}