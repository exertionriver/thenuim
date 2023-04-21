package river.exertion.kcop.simulation.view

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.kotcrab.vis.ui.widget.VisWindow
import ktx.actors.onClick
import ktx.actors.stage
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.ctrl.*
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.DisplayViewAudioMessage
import river.exertion.kcop.system.messaging.messages.KcopMessage

class ViewLayout(var width : Float, var height : Float) : Telegraph {

    init {
        MessageChannel.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    var displayViewCtrl = DisplayViewCtrl(width, height)
    var textViewCtrl = TextViewCtrl(width, height)
    var logViewCtrl = LogViewCtrl(width, height)
    var statusViewCtrl = StatusViewCtrl(width, height)
    var menuViewCtrl = MenuViewCtrl(width, height)
    var inputsViewCtrl = InputViewCtrl(width, height)
    var aiViewCtrl = AiViewCtrl(width, height)
    var pauseViewCtrl = PauseViewCtrl(width, height)

    lateinit var kcopButton : Button
    lateinit var kcopSkin : KcopSkin

    fun build(stage: Stage) {
        stage.addActor(displayViewCtrl.apply { this.build() })
        stage.addActor(textViewCtrl.apply { this.build() })
        stage.addActor(logViewCtrl.apply { this.build() })
        stage.addActor(statusViewCtrl.apply { this.build() })
        stage.addActor(menuViewCtrl.apply { this.build() })
        stage.addActor(inputsViewCtrl.apply { this.build() })
        stage.addActor(aiViewCtrl.apply { this.build() })
        stage.addActor(pauseViewCtrl.apply { this.build() })

        kcopButton = Button(kcopSkin.skin).apply { this.onClick {
            MessageChannel.KCOP_BRIDGE.send(null, KcopMessage(KcopMessage.KcopMessageType.KcopScreen))
        }}

        stage.addActor(kcopButton)
        kcopButton.addAction(Actions.sequence(Actions.hide()))
    }

    fun kcopScreen() {
        displayViewCtrl.addAction(Actions.sequence(Actions.moveTo(ViewType.DISPLAY.viewRect(width, height).x, ViewType.DISPLAY.viewRect(width, height).y, .25f, Interpolation.linear)))
        displayViewCtrl.viewType = ViewType.DISPLAY
        textViewCtrl.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(VisWindow.FADE_TIME, Interpolation.fade)))
        logViewCtrl.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(VisWindow.FADE_TIME, Interpolation.fade)))
        statusViewCtrl.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(VisWindow.FADE_TIME, Interpolation.fade)))
        menuViewCtrl.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(VisWindow.FADE_TIME, Interpolation.fade)))
        inputsViewCtrl.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(VisWindow.FADE_TIME, Interpolation.fade)))
        aiViewCtrl.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(VisWindow.FADE_TIME, Interpolation.fade)))
        pauseViewCtrl.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(VisWindow.FADE_TIME, Interpolation.fade)))
        kcopButton.addAction(Actions.sequence(Actions.hide()))
        MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                DisplayViewAudioMessage.DisplayViewAudioMessageType.PlaySound, kcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh]))
    }

    fun fullScreen() {
        displayViewCtrl.addAction(Actions.sequence(Actions.moveTo(ViewType.DISPLAY_FULLSCREEN.viewRect(width, height).x, ViewType.DISPLAY_FULLSCREEN.viewRect(width, height).y, .25f, Interpolation.linear)))
        displayViewCtrl.viewType = ViewType.DISPLAY_FULLSCREEN
        textViewCtrl.addAction(Actions.sequence(Actions.fadeOut(VisWindow.FADE_TIME, Interpolation.fade), Actions.hide()))
        logViewCtrl.addAction(Actions.sequence(Actions.fadeOut(VisWindow.FADE_TIME, Interpolation.fade), Actions.hide()))
        statusViewCtrl.addAction(Actions.sequence(Actions.fadeOut(VisWindow.FADE_TIME, Interpolation.fade), Actions.hide()))
        menuViewCtrl.addAction(Actions.sequence(Actions.fadeOut(VisWindow.FADE_TIME, Interpolation.fade), Actions.hide()))
        inputsViewCtrl.addAction(Actions.sequence(Actions.fadeOut(VisWindow.FADE_TIME, Interpolation.fade), Actions.hide()))
        aiViewCtrl.addAction(Actions.sequence(Actions.fadeOut(VisWindow.FADE_TIME, Interpolation.fade), Actions.hide()))
        pauseViewCtrl.addAction(Actions.sequence(Actions.fadeOut(VisWindow.FADE_TIME, Interpolation.fade), Actions.hide()))
        kcopButton.addAction(Actions.sequence(Actions.show()))
        MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                DisplayViewAudioMessage.DisplayViewAudioMessageType.PlaySound, kcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh]))
    }

    fun dispose() {
        displayViewCtrl.dispose()
        textViewCtrl.dispose()
        logViewCtrl.dispose()
        statusViewCtrl.dispose()
        menuViewCtrl.dispose()
        inputsViewCtrl.dispose()
        aiViewCtrl.dispose()
        pauseViewCtrl.dispose()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.KCOP_SKIN_BRIDGE.isType(msg.message)) -> {
                    kcopSkin = MessageChannel.KCOP_SKIN_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
            }
        }
        return false
    }
}