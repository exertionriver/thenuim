package river.exertion.kcop

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.inject.register
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.simulation.KcopSimulator

class Kcop : KtxGame<KtxScreen>() {

    private val context = Context()

    override fun create() {
        KcopBase.init()

        context.register {
            bindSingleton(KcopBase.orthoCamera)
            bindSingleton(KcopBase.stage)

            addScreen(KcopSimulator( inject(), inject() ) )
        }

        setScreen<KcopSimulator>()
    }
}