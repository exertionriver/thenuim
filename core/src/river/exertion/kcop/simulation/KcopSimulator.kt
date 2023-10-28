package river.exertion.kcop.simulation

import GdxDesktopTestBehavior
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import ktx.app.KtxScreen
import river.exertion.kcop.automation.AutomationKlop
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.colorPalette.ColorPaletteDisplayKlop
import river.exertion.kcop.sim.narrative.NarrativeKlop
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewKlop.KcopBridge
import river.exertion.kcop.view.klop.IDisplayViewKlop
import river.exertion.kcop.view.layout.AudioView
import river.exertion.kcop.view.layout.ButtonView
import river.exertion.kcop.view.layout.ViewLayout
import river.exertion.kcop.view.layout.ViewType
import river.exertion.kcop.view.messaging.KcopSimulationMessage
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile


class KcopSimulator : Telegraph, KtxScreen {

    val coreDisplayViewPackages = mutableListOf<IDisplayViewKlop>(
        NarrativeKlop,
        ColorPaletteDisplayKlop,
    )

    var loadedKlop : IDisplayViewKlop? = null

    init {
        val jarPath = "plugins/SweetAstroConsole-kcop-0.4.jar"
        val jarFile = JarFile(jarPath)
        val jarEnumeration = jarFile.entries()
        val classLoader = URLClassLoader.newInstance(arrayOf(URL("jar:file:$jarPath!/")))

        val coreDisplayViewPackageNames = coreDisplayViewPackages.map { it::class.qualifiedName }

        while (jarEnumeration.hasMoreElements()) {
            val jarEntry = jarEnumeration.nextElement()

            if (jarEntry.isDirectory || !jarEntry.name.endsWith("Klop.class")) continue

            val className = jarEntry.name.substring(0, jarEntry.name.length - 6).replace('/', '.')

            if (classLoader.loadClass(className).interfaces.contains(IDisplayViewKlop::class.java) &&
                !coreDisplayViewPackageNames.contains(className) ) {

                loadedKlop = (classLoader.loadClass(className) as Class<IDisplayViewKlop>).kotlin.objectInstance
            }
        }

        coreDisplayViewPackages.forEach {
            it.load()
        }

        loadedKlop?.load()

        AutomationKlop.load()

        MessageChannelHandler.enableReceive(KcopBridge, this)
    }

    var currentDisplayViewKlop : IDisplayViewKlop = loadedKlop ?: NarrativeKlop

    override fun render(delta: Float) {

        KcopBase.render(delta, EngineHandler.engine)

    }

    override fun hide() {
    }

    override fun show() {
        currentDisplayViewKlop.showView()
        ViewLayout.build(KcopBase.stage)
        ButtonView.assignableButtons[5] = { GdxDesktopTestBehavior.testBehavior() }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        KcopBase.orthoCamera.viewportWidth = width.toFloat()
        KcopBase.orthoCamera.viewportHeight = height.toFloat()
        KcopBase.stage.viewport.update(width, height)
    }


    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(KcopBridge, msg.message) ) -> {
                    val kcopSimulationMessage: KcopSimulationMessage = MessageChannelHandler.receiveMessage(KcopBridge, msg.extraInfo)

                    when (kcopSimulationMessage.kcopMessageType) {
                        KcopSimulationMessage.KcopMessageType.FullScreen -> {
                            ViewLayout.fullScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(KcopBase.stage.width, KcopBase.stage.height))
                            AudioView.playSound(KcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh])
                        }
                        KcopSimulationMessage.KcopMessageType.KcopScreen -> {
                            ViewLayout.kcopScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(KcopBase.stage.width, KcopBase.stage.height))
                            AudioView.playSound(KcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh])
                        }
                        KcopSimulationMessage.KcopMessageType.ColorPaletteOn -> {
                            currentDisplayViewKlop = ColorPaletteDisplayKlop

                            coreDisplayViewPackages.filter { it != currentDisplayViewKlop }.forEach {
                                it.hideView()
                            }

                            currentDisplayViewKlop.showView()
                        }
                        KcopSimulationMessage.KcopMessageType.ColorPaletteOff -> {
                            currentDisplayViewKlop = NarrativeKlop

                            coreDisplayViewPackages.filter { it != currentDisplayViewKlop }.forEach {
                                it.hideView()
                            }

                            currentDisplayViewKlop.showView()
                        }
                    }

                    return true
                }
            }
        }
        return false
    }

    override fun dispose() {
        coreDisplayViewPackages.forEach {
            it.dispose()
        }

        super.dispose()
    }
}