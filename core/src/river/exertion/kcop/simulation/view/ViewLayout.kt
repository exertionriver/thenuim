package river.exertion.kcop.simulation.view

import com.badlogic.gdx.scenes.scene2d.Stage
import river.exertion.kcop.simulation.view.ctrl.*

class ViewLayout(var width : Float, var height : Float) {

    var displayViewCtrl = DisplayViewCtrl(width, height)
    var textViewCtrl = TextViewCtrl(width, height)
    var logViewCtrl = LogViewCtrl(width, height)
    var statusViewCtrl = StatusViewCtrl(width, height)
    var menuViewCtrl = MenuViewCtrl(width, height)
    var inputsViewCtrl = InputViewCtrl(width, height)
    var aiViewCtrl = AiViewCtrl(width, height)
    var pauseViewCtrl = PauseViewCtrl(width, height)

    fun build(stage: Stage) {
        stage.addActor(displayViewCtrl.apply { this.build() })
        stage.addActor(textViewCtrl.apply { this.build() })
        stage.addActor(logViewCtrl.apply { this.build() })
        stage.addActor(statusViewCtrl.apply { this.build() })
        stage.addActor(menuViewCtrl.apply { this.build() })
        stage.addActor(inputsViewCtrl.apply { this.build() })
        stage.addActor(aiViewCtrl.apply { this.build() })
        stage.addActor(pauseViewCtrl.apply { this.build() })
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
}