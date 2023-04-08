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

    fun createTextViewCtrl(vScrollKnobImage : Texture) : TextViewCtrl {
        textViewCtrl.vScrollKnobTexture = vScrollKnobImage

        textViewCtrl.build()

        return textViewCtrl
    }
    fun createLogViewCtrl(vScrollImage : Texture, vScrollKnobImage : Texture) : LogViewCtrl {
        logViewCtrl.vScrollTexture = vScrollImage
        logViewCtrl.vScrollKnobTexture = vScrollKnobImage

        logViewCtrl.build()

        return logViewCtrl
    }
    fun createStatusViewCtrl(vScrollKnobImage : Texture) : StatusViewCtrl {
        statusViewCtrl.vScrollKnobTexture = vScrollKnobImage

        statusViewCtrl.build()

        return statusViewCtrl
    }

    fun createMenuViewCtrl(upImage : Texture, downImage : Texture, checkedImage : Texture) : MenuViewCtrl {

        (0..5).forEach {
            menuViewCtrl.menuUpImage[it] = upImage
            menuViewCtrl.menuDownImage[it] = downImage
            menuViewCtrl.menuCheckedImage[it] = checkedImage
        }

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
    fun createAiViewCtrl(upImage : Texture, downImage : Texture, checkedImage: Texture) : AiViewCtrl {
        aiViewCtrl.aiUpImage = upImage
        aiViewCtrl.aiDownImage = downImage
        aiViewCtrl.aiCheckedImage = checkedImage

        aiViewCtrl.build()

        return aiViewCtrl
    }

    fun createPauseViewCtrl(upImage : Texture, downImage : Texture, checkedImage : Texture) : PauseViewCtrl {
        pauseViewCtrl.pauseUpImage = upImage
        pauseViewCtrl.pauseDownImage = downImage
        pauseViewCtrl.pauseCheckedImage = checkedImage

        pauseViewCtrl.build()

        return pauseViewCtrl
    }

    fun build(stage: Stage, assetManagerHandler: AssetManagerHandler) {
        stage.addActor(this.createDisplayViewCtrl())
        stage.addActor(this.createTextViewCtrl(assetManagerHandler.assets[TextureAssets.KoboldA]))
        stage.addActor(this.createLogViewCtrl(assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB]))
        stage.addActor(this.createStatusViewCtrl(assetManagerHandler.assets[TextureAssets.KoboldA]))
        stage.addActor(this.createMenuViewCtrl(assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB], assetManagerHandler.assets[TextureAssets.KoboldC]))
        stage.addActor(this.createInputsViewCtrl(assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB], assetManagerHandler.assets[TextureAssets.KoboldC]))
        stage.addActor(this.createAiViewCtrl(assetManagerHandler.assets[TextureAssets.BlueSphere], assetManagerHandler.assets[TextureAssets.BlueSphere], assetManagerHandler.assets[TextureAssets.BlueSphere]))
        stage.addActor(this.createPauseViewCtrl(assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB], assetManagerHandler.assets[TextureAssets.KoboldC]))
        stage.addActor(this.createTextViewCtrl(assetManagerHandler.assets[TextureAssets.KoboldA]))
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