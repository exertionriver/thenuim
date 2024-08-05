package river.exertion.thenuim.view.layout

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import ktx.actors.onClick
import river.exertion.thenuim.base.TnmBase
import river.exertion.thenuim.ecs.IECSLoPa
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.view.TnmSkin
import river.exertion.thenuim.view.ViewLoPa.TnmBridge
import river.exertion.thenuim.view.IDisplayViewLoPa
import river.exertion.thenuim.view.IDisplayViewLayoutHandler
import river.exertion.thenuim.view.messaging.TnmSimMessage

object ViewLayout {

    lateinit var tnmButton : Button
    var currentDisplayViewLayoutHandler : IDisplayViewLayoutHandler? = null

    fun build(stage: Stage) {
        stage.addActor(DisplayView.apply { this.build() }.viewTable)
        stage.addActor(DisplayAuxView.apply { this.build() }.viewTable)
        stage.addActor(TextView.apply { this.build() }.viewTable)
        stage.addActor(LogView.apply { this.build() }.viewTable)
        stage.addActor(StatusView.apply { this.build() }.viewTable)
        stage.addActor(ButtonView.apply { this.build() }.viewTable)
        stage.addActor(InputView.apply { this.build() }.viewTable)
        stage.addActor(AiView.apply { this.build() }.viewTable)
        stage.addActor(PauseView.apply { this.build() }.viewTable)

        tnmButton = Button(TnmSkin.skin).apply { this.onClick {
            MessageChannelHandler.send(TnmBridge, TnmSimMessage(TnmSimMessage.TnmSimMessageType.DebugScreen))
        }}

        stage.addActor(tnmButton)

        if (DisplayViewMode.currentDVMode != DisplayViewMode.DisplayViewWork)
            screenTransition(transitionTo = DisplayViewMode.currentDVMode, immediate = true)
        else
            tnmButton.addAction(Actions.sequence(Actions.hide()))
    }

    fun rebuild() {
        if (currentDisplayViewLayoutHandler != null) {
            currentDisplayViewLayoutHandler!!.build()
        }
        else {
            DisplayView.build()
            DisplayAuxView.build()
        }
        TextView.build()
        LogView.build()
        StatusView.build()
        ButtonView.build()
        InputView.build()
        AiView.build()
        PauseView.build()
    }

    val defaultFadeOutDuration = .2f
    val defaultFadeInDuration = defaultFadeOutDuration * 4
    val defaultMoveDuration = defaultFadeOutDuration * 2

    fun screenTransition(currentDisplayViewLoPa : IDisplayViewLoPa? = null, transitionTo : DisplayViewMode, immediate : Boolean = false) {
        //turn off ECS systems temporarily
        if (currentDisplayViewLoPa != null && currentDisplayViewLoPa::class.java.interfaces.contains(IECSLoPa::class.java))
            (currentDisplayViewLoPa as IECSLoPa).unloadSystems()

        val fadeOutDuration = -immediate.compareTo(true) * defaultFadeOutDuration
        val fadeInDuration = -immediate.compareTo(true) * defaultFadeInDuration
        val moveDuration = -immediate.compareTo(true) * defaultMoveDuration

        when {
            (transitionTo == DisplayViewMode.DisplayViewFull) -> {
                val pos : Vector2 = ViewType.DISPLAY.viewPosition(TnmBase.stage.width, TnmBase.stage.height)

                DisplayView.viewTable.addAction(Actions.sequence(Actions.moveTo(pos.x, pos.y, moveDuration, Interpolation.linear), Actions.run {
                    if (currentDisplayViewLoPa != null && currentDisplayViewLoPa::class.java.interfaces.contains(
                            IECSLoPa::class.java))
                        (currentDisplayViewLoPa as IECSLoPa).loadSystems()
                }))

                DisplayView.viewType = ViewType.DISPLAY

                TextView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
                LogView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
                StatusView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
                ButtonView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
                InputView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
                AiView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
                PauseView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))

                DisplayAuxView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeOutDuration, Interpolation.fade)))

                tnmButton.addAction(Actions.sequence(Actions.show()))
            }
            (transitionTo == DisplayViewMode.DisplayViewCenter) -> {
                val pos : Vector2 = ViewType.DISPLAY_ONLY.viewPosition(TnmBase.stage.width, TnmBase.stage.height)

                DisplayView.viewTable.addAction(Actions.sequence(Actions.moveTo(pos.x, pos.y, moveDuration, Interpolation.linear), Actions.run {
                    if (currentDisplayViewLoPa != null && currentDisplayViewLoPa::class.java.interfaces.contains(
                            IECSLoPa::class.java))
                        (currentDisplayViewLoPa as IECSLoPa).loadSystems()
                }))

                DisplayView.viewType = ViewType.DISPLAY_ONLY

                TextView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
                LogView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
                StatusView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
                ButtonView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
                InputView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
                AiView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))
                PauseView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))

                DisplayAuxView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))

                tnmButton.addAction(Actions.sequence(Actions.show()))

            }
            else -> { //to TnmScreen
                val pos : Vector2 = ViewType.DISPLAY.viewPosition(TnmBase.stage.width, TnmBase.stage.height)

                DisplayView.viewTable.addAction(Actions.sequence(Actions.moveTo(pos.x, pos.y, moveDuration, Interpolation.linear), Actions.run {
                    if (currentDisplayViewLoPa != null && currentDisplayViewLoPa::class.java.interfaces.contains(
                            IECSLoPa::class.java))
                        (currentDisplayViewLoPa as IECSLoPa).loadSystems()
                }))

                DisplayView.viewType = ViewType.DISPLAY

                TextView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
                LogView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
                StatusView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
                ButtonView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
                InputView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
                AiView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))
                PauseView.viewTable.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(fadeInDuration, Interpolation.fade)))

                DisplayAuxView.viewTable.addAction(Actions.sequence(Actions.fadeOut(fadeOutDuration, Interpolation.fade), Actions.hide()))

                tnmButton.addAction(Actions.sequence(Actions.hide()))
            }
        }
    }
}