package river.exertion.kcop.view.layout

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import ktx.actors.onClick
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.KcopSkin.Companion.KcopSkinBridge
import river.exertion.kcop.view.messaging.KcopMessage
import river.exertion.kcop.view.messaging.KcopMessage.Companion.KcopBridge

class ViewLayout(var width : Float, var height : Float) : Telegraph {

    init {
        MessageChannelHandler.enableReceive(KcopSkinBridge, this)
    }

    var displayView = DisplayView(width, height)
    var textView = TextView(width, height)
    var logView = LogView(width, height)
    var statusView = StatusView(width, height)
    var menuView = MenuView(width, height)
    var inputsViewCtrl = InputView(width, height)
    var aiView = AiView(width, height)
    var pauseView = PauseView(width, height)

    lateinit var kcopButton : Button
    lateinit var kcopSkin : KcopSkin

    fun build(stage: Stage) {
        stage.addActor(displayView.apply { this.build() })
        stage.addActor(textView.apply { this.build() })
        stage.addActor(logView.apply { this.build() })
        stage.addActor(statusView.apply { this.build() })
        stage.addActor(menuView.apply { this.build() })
        stage.addActor(inputsViewCtrl.apply { this.build() })
        stage.addActor(aiView.apply { this.build() })
        stage.addActor(pauseView.apply { this.build() })

        kcopButton = Button(kcopSkin.skin).apply { this.onClick {
            MessageChannelHandler.send(KcopBridge, KcopMessage(KcopMessage.KcopMessageType.KcopScreen))
        }}

        stage.addActor(kcopButton)
        kcopButton.addAction(Actions.sequence(Actions.hide()))
    }

    val fadeDuration = .25f

    fun kcopScreen(offset : Vector2) {
        displayView.addAction(Actions.sequence(Actions.moveBy(-offset.x, -offset.y, fadeDuration, Interpolation.linear)))
        displayView.viewType = ViewType.DISPLAY
        textView.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeDuration, Interpolation.fade)))
        logView.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeDuration, Interpolation.fade)))
        statusView.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeDuration, Interpolation.fade)))
        menuView.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeDuration, Interpolation.fade)))
        inputsViewCtrl.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeDuration, Interpolation.fade)))
        aiView.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeDuration, Interpolation.fade)))
        pauseView.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeDuration, Interpolation.fade)))
        kcopButton.addAction(Actions.sequence(Actions.hide()))
    }

    fun fullScreen(offset : Vector2) {
        displayView.addAction(Actions.sequence(Actions.moveBy(offset.x, offset.y, fadeDuration, Interpolation.linear)))
        displayView.viewType = ViewType.DISPLAY_FULLSCREEN
        textView.addAction(Actions.sequence(Actions.fadeOut(fadeDuration, Interpolation.fade), Actions.hide()))
        logView.addAction(Actions.sequence(Actions.fadeOut(fadeDuration, Interpolation.fade), Actions.hide()))
        statusView.addAction(Actions.sequence(Actions.fadeOut(fadeDuration, Interpolation.fade), Actions.hide()))
        menuView.addAction(Actions.sequence(Actions.fadeOut(fadeDuration, Interpolation.fade), Actions.hide()))
        inputsViewCtrl.addAction(Actions.sequence(Actions.fadeOut(fadeDuration, Interpolation.fade), Actions.hide()))
        aiView.addAction(Actions.sequence(Actions.fadeOut(fadeDuration, Interpolation.fade), Actions.hide()))
        pauseView.addAction(Actions.sequence(Actions.fadeOut(fadeDuration, Interpolation.fade), Actions.hide()))
        kcopButton.addAction(Actions.sequence(Actions.show()))
    }

    fun dispose() {
        displayView.dispose()
        textView.dispose()
        logView.dispose()
        statusView.dispose()
        menuView.dispose()
        inputsViewCtrl.dispose()
        aiView.dispose()
        pauseView.dispose()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(KcopSkinBridge, msg.message)) -> {
                    kcopSkin = MessageChannelHandler.receiveMessage(KcopSkinBridge, msg.extraInfo)
                    return true
                }
            }
        }
        return false
    }
}