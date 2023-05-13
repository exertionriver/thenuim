package river.exertion.kcop.view.layout

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import ktx.actors.onClick
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewPackage.KcopBridge
import river.exertion.kcop.view.messaging.KcopMessage

class ViewLayout {

    var displayView = DisplayView()
    var textView = TextView()
    var logView = LogView()
    var statusView = StatusView()
    var menuView = MenuView()
    var inputsViewCtrl = InputView()
    var aiView = AiView()
    var pauseView = PauseView()

    lateinit var kcopButton : Button

    fun build(stage: Stage) {
        stage.addActor(displayView.apply { this.build() })
        stage.addActor(textView.apply { this.build() })
        stage.addActor(logView.apply { this.build() })
        stage.addActor(statusView.apply { this.build() })
        stage.addActor(menuView.apply { this.build() })
        stage.addActor(inputsViewCtrl.apply { this.build() })
        stage.addActor(aiView.apply { this.build() })
        stage.addActor(pauseView.apply { this.build() })

        kcopButton = Button(KcopSkin.skin).apply { this.onClick {
            MessageChannelHandler.send(KcopBridge, KcopMessage(KcopMessage.KcopMessageType.KcopScreen))
        }}

        stage.addActor(kcopButton)
        kcopButton.addAction(Actions.sequence(Actions.hide()))
    }

    val fadeOutDuration = .2f
    val fadeInDuration = fadeOutDuration * 4
    val moveDuration = fadeOutDuration * 2

    fun kcopScreen(offset : Vector2) {
        displayView.addAction(Actions.sequence(Actions.moveBy(-offset.x, -offset.y, moveDuration, Interpolation.linear)))
        displayView.viewType = ViewType.DISPLAY
        textView.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
        logView.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
        statusView.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
        menuView.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
        inputsViewCtrl.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
        aiView.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
        pauseView.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
        kcopButton.addAction(Actions.sequence(Actions.hide()))
    }

    fun fullScreen(offset : Vector2) {
        displayView.addAction(Actions.sequence(Actions.moveBy(offset.x, offset.y, moveDuration, Interpolation.linear)))
        displayView.viewType = ViewType.DISPLAY_FULLSCREEN
        textView.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
        logView.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
        statusView.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
        menuView.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
        inputsViewCtrl.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
        aiView.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
        pauseView.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
        kcopButton.addAction(Actions.sequence(Actions.show()))
    }
}