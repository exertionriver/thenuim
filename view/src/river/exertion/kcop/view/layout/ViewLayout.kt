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
import river.exertion.kcop.view.messaging.KcopSimulationMessage

object ViewLayout {

    lateinit var kcopButton : Button

    fun build(stage: Stage) {
        stage.addActor(DisplayView.apply { this.build() }.viewTable)
        stage.addActor(TextView.apply { this.build() }.viewTable)
        stage.addActor(LogView.apply { this.build() }.viewTable)
        stage.addActor(StatusView.apply { this.build() }.viewTable)
        stage.addActor(MenuView.apply { this.build() }.viewTable)
        stage.addActor(InputView.apply { this.build() }.viewTable)
        stage.addActor(AiView.apply { this.build() }.viewTable)
        stage.addActor(PauseView.apply { this.build() }.viewTable)

        kcopButton = Button(KcopSkin.skin).apply { this.onClick {
            MessageChannelHandler.send(KcopBridge, KcopSimulationMessage(KcopSimulationMessage.KcopMessageType.KcopScreen))
        }}

        stage.addActor(kcopButton)
        kcopButton.addAction(Actions.sequence(Actions.hide()))
    }

    fun rebuild() {
        DisplayView.build()
        TextView.build()
        LogView.build()
        StatusView.build()
        MenuView.build()
        InputView.build()
        AiView.build()
        PauseView.build()
    }

    val fadeOutDuration = .2f
    val fadeInDuration = fadeOutDuration * 4
    val moveDuration = fadeOutDuration * 2

    fun kcopScreen(offset : Vector2) {
        DisplayView.viewTable.addAction(Actions.sequence(Actions.moveBy(-offset.x, -offset.y, moveDuration, Interpolation.linear)))
        DisplayView.viewType = ViewType.DISPLAY

        TextView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
        LogView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
        StatusView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
        MenuView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
        InputView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
        AiView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
        PauseView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))

        kcopButton.addAction(Actions.sequence(Actions.hide()))
    }

    fun fullScreen(offset : Vector2) {
        DisplayView.viewTable.addAction(Actions.sequence(Actions.moveBy(offset.x, offset.y, moveDuration, Interpolation.linear)))
        DisplayView.viewType = ViewType.DISPLAY_FULLSCREEN
        TextView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
        LogView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
        StatusView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
        MenuView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
        InputView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
        AiView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
        PauseView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
        kcopButton.addAction(Actions.sequence(Actions.show()))
    }
}