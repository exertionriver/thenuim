package river.exertion.kcop.simulation.view

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import river.exertion.kcop.simulation.view.ctrl.*
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.messaging.*
import river.exertion.kcop.system.messaging.messages.*
import river.exertion.kcop.system.view.*

class ViewLayout(var width : Float, var height : Float) {

    var displayViewCtrl = DisplayViewCtrl(width, height)
    var textViewCtrl = TextViewCtrl(width, height)
    var logViewCtrl = LogViewCtrl(width, height)
    var statusViewCtrl = StatusViewCtrl(width, height)
    var menuViewCtrl = MenuViewCtrl(width, height)
    var inputsViewCtrl = InputViewCtrl(width, height)
    var aiViewCtrl = AiViewCtrl(width, height)
    var pauseViewCtrl = PauseViewCtrl(width, height)

    fun createDisplayViewCtrl(bitmapFont : BitmapFont) : DisplayViewCtrl {

        displayViewCtrl.initCreate(bitmapFont)

        return displayViewCtrl
    }

    fun createTextViewCtrl(bitmapFont : BitmapFont, vScrollKnobImage : Texture) : TextViewCtrl {
        textViewCtrl.vScrollKnobTexture = vScrollKnobImage

        textViewCtrl.initCreate(bitmapFont)

        return textViewCtrl
    }
    fun createLogViewCtrl(bitmapFont : BitmapFont, vScrollImage : Texture, vScrollKnobImage : Texture) : LogViewCtrl {
        logViewCtrl.vScrollTexture = vScrollImage
        logViewCtrl.vScrollKnobTexture = vScrollKnobImage

        logViewCtrl.initCreate(bitmapFont)

        return logViewCtrl
    }
    fun createStatusViewCtrl(bitmapFont : BitmapFont, vScrollKnobImage : Texture) : StatusViewCtrl {
        statusViewCtrl.vScrollKnobTexture = vScrollKnobImage

        statusViewCtrl.initCreate(bitmapFont)

        return statusViewCtrl
    }

    fun createMenuViewCtrl(bitmapFont : BitmapFont, upImage : Texture, downImage : Texture, checkedImage : Texture) : MenuViewCtrl {

        (0..5).forEach {
            menuViewCtrl.menuUpImage[it] = upImage
            menuViewCtrl.menuDownImage[it] = downImage
            menuViewCtrl.menuCheckedImage[it] = checkedImage
        }

        menuViewCtrl.initCreate(bitmapFont)

        return menuViewCtrl
    }

    fun createInputsViewCtrl(bitmapFont : BitmapFont, clickImage : Texture, keyPressImage : Texture, keyUpImage : Texture) : InputViewCtrl {
        inputsViewCtrl.clickImage = clickImage
        inputsViewCtrl.keyPressImage = keyPressImage
        inputsViewCtrl.keyUpImage = keyUpImage

        inputsViewCtrl.initCreate(bitmapFont)

        return inputsViewCtrl
    }
    fun createAiViewCtrl(bitmapFont : BitmapFont, upImage : Texture, downImage : Texture, checkedImage: Texture) : AiViewCtrl {
        aiViewCtrl.aiUpImage = upImage
        aiViewCtrl.aiDownImage = downImage
        aiViewCtrl.aiCheckedImage = checkedImage

        aiViewCtrl.initCreate(bitmapFont)

        return aiViewCtrl
    }

    fun createPauseViewCtrl(bitmapFont : BitmapFont, upImage : Texture, downImage : Texture, checkedImage : Texture) : PauseViewCtrl {
        pauseViewCtrl.pauseUpImage = upImage
        pauseViewCtrl.pauseDownImage = downImage
        pauseViewCtrl.pauseCheckedImage = checkedImage

        pauseViewCtrl.initCreate(bitmapFont)

        return pauseViewCtrl
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