package river.exertion.kcop

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.inject.register
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.simulation.KcopSimulator

class Kcop : KtxGame<KtxScreen>() {

    override fun create() {
        KcopBase.create()
        addScreen(KcopSimulator())
        setScreen<KcopSimulator>()
    }
}