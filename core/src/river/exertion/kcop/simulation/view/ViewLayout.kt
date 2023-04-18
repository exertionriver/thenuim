package river.exertion.kcop.simulation.view

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import river.exertion.kcop.assets.AssetManagerHandler
import river.exertion.kcop.assets.TextureAssets
import river.exertion.kcop.assets.get
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

    fun createDisplayViewCtrl() : DisplayViewCtrl {

        displayViewCtrl.build()

        return displayViewCtrl
    }

    fun createTextViewCtrl() : TextViewCtrl {

        textViewCtrl.build()

        return textViewCtrl
    }
    fun createLogViewCtrl() : LogViewCtrl {

        logViewCtrl.build()

        return logViewCtrl
    }
    fun createStatusViewCtrl() : StatusViewCtrl {

        statusViewCtrl.build()

        return statusViewCtrl
    }

    fun createMenuViewCtrl() : MenuViewCtrl {

        menuViewCtrl.build()

        return menuViewCtrl
    }

    fun createInputsViewCtrl(clickImage : Texture, keyPressImage : Texture, keyUpImage : Texture) : InputViewCtrl {
        inputsViewCtrl.clickImage = clickImage
        inputsViewCtrl.keyPressImage = keyPressImage
        inputsViewCtrl.keyUpImage = keyUpImage

        inputsViewCtrl.build()

        return inputsViewCtrl
    }
    fun createAiViewCtrl() : AiViewCtrl {

        aiViewCtrl.build()

        return aiViewCtrl
    }

    fun createPauseViewCtrl() : PauseViewCtrl {

        pauseViewCtrl.build()

        return pauseViewCtrl
    }

    fun build(stage: Stage, assetManagerHandler: AssetManagerHandler) {
        stage.addActor(this.createDisplayViewCtrl())
        stage.addActor(this.createTextViewCtrl())
        stage.addActor(this.createLogViewCtrl())
        stage.addActor(this.createStatusViewCtrl())
        stage.addActor(this.createMenuViewCtrl())
        stage.addActor(this.createInputsViewCtrl(assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB], assetManagerHandler.assets[TextureAssets.KoboldC]))
        stage.addActor(this.createAiViewCtrl())
        stage.addActor(this.createPauseViewCtrl())
        stage.addActor(this.createTextViewCtrl())
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