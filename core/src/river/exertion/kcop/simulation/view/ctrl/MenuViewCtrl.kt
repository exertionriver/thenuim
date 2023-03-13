package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.actors.onClick
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.DisplayViewMenuMessage

class MenuViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : ViewCtrl(ViewType.MENU, screenWidth, screenHeight) {

    var menuUpImage : MutableMap<Int, Texture?> = mutableMapOf()
    var menuDownImage : MutableMap<Int, Texture?> = mutableMapOf()
    var menuCheckedImage : MutableMap<Int, Texture?> = mutableMapOf()

    var isChecked : MutableMap<Int, Boolean> = mutableMapOf()

    fun buttonLayout() : Table {
        //idx 0 is large button, idx 1 - 5 are smaller bordering buttons
        val buttonList = mutableListOf<Button>()

        (0..5).forEach { idx ->

            var buttonStyle = ButtonStyle()

            if (menuUpImage[idx] != null && menuDownImage[idx] != null && menuCheckedImage[idx] != null) {
                buttonStyle = ButtonStyle(
                    TextureRegionDrawable(menuUpImage[idx])
                    , TextureRegionDrawable(menuDownImage[idx])
                    , TextureRegionDrawable(menuCheckedImage[idx]))
            }

            val innerButton = Button(buttonStyle)

            //override from ctrl
            innerButton.isChecked = this@MenuViewCtrl.isChecked[idx] == true

            innerButton.onClick {
                this@MenuViewCtrl.isChecked[idx] = !(this@MenuViewCtrl.isChecked[idx] ?: false)
                MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(idx))
            }

            buttonList.add(innerButton)
        }

        val buttonSubLayout1 = Table()
        buttonSubLayout1.add(buttonList[1]).size(ViewType.seventhWidth(screenWidth), ViewType.seventhHeight(screenHeight))
        buttonSubLayout1.add(buttonList[2]).size(ViewType.seventhWidth(screenWidth), ViewType.seventhHeight(screenHeight))
        buttonSubLayout1.row()
        buttonSubLayout1.add(buttonList[0]).colspan(2).size(ViewType.sixthWidth(screenWidth), ViewType.sixthHeight(screenHeight))

        val buttonSubLayout2 = Table()
        buttonSubLayout2.add(buttonList[3]).size(ViewType.seventhWidth(screenWidth), ViewType.seventhHeight(screenHeight))
        buttonSubLayout2.row()
        buttonSubLayout2.add(buttonList[4]).size(ViewType.seventhWidth(screenWidth), ViewType.seventhHeight(screenHeight))
        buttonSubLayout2.row()
        buttonSubLayout2.add(buttonList[5]).size(ViewType.seventhWidth(screenWidth), ViewType.seventhHeight(screenHeight))

        val buttonLayout = Table()
        buttonLayout.add(buttonSubLayout1)
        buttonLayout.add(buttonSubLayout2)

        buttonLayout.validate()
        buttonLayout.layout()

        return buttonLayout
    }

    override fun build(bitmapFont: BitmapFont, batch: Batch) {
        this.add(buttonLayout()).width(this.tableWidth()).height(this.tableHeight())
        this.clip()
    }
}