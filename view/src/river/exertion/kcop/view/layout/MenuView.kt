package river.exertion.kcop.view.layout

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewKlop.KcopBridge
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.KcopSimulationMessage

object MenuView : ViewBase {

    override var viewType = ViewType.MENU
    override var viewTable = Table()

    val menuButton = 0
    val displayMode = 1
    val fullScreen = 3

    var assignableButtons = mutableMapOf(
        menuButton to {
            if (isChecked[menuButton] == true)
                openMenu()
            else
                closeMenu()
        },
        displayMode to {
            KcopSkin.displayMode = isChecked[displayMode]!!
            ViewLayout.rebuild()
       },
        2 to {
            if (isChecked[2] == true) {
                LogView.addLog("Color Palette On")
                MessageChannelHandler.send(KcopBridge, KcopSimulationMessage(KcopSimulationMessage.KcopMessageType.ColorPaletteOn))
            } else {
                LogView.addLog("Color Palette Off")
                MessageChannelHandler.send(KcopBridge, KcopSimulationMessage(KcopSimulationMessage.KcopMessageType.ColorPaletteOff))
            }
        },
        fullScreen to {
            MessageChannelHandler.send(KcopBridge, KcopSimulationMessage(KcopSimulationMessage.KcopMessageType.FullScreen))
        },
        4 to {
//            KcopSkin.displayMode = isChecked[4]!!
//            ViewLayout.rebuild()
        },
        5 to {
//            KcopSkin.displayMode = isChecked[5]!!
//            ViewLayout.rebuild()
        }
    )

    var isChecked : MutableMap<Int, Boolean> = mutableMapOf()

    fun buttonLayout() : Table {
        //idx 0 is large button, idx 1 - 5 are smaller bordering buttons
        val buttonList = mutableListOf<Button>()

        (0..5).forEach { idx ->

            val innerButton = Button(KcopSkin.skin).apply { KcopSkin.addOnClick(this) }

            //override from ctrl
            innerButton.isChecked = this@MenuView.isChecked[idx] == true

            innerButton.onClick {
                this@MenuView.isChecked[idx] = !(this@MenuView.isChecked[idx] ?: false)

                assignableButtons[idx]?.let { it() }
            }

            buttonList.add(innerButton)
        }

        val buttonSubLayout1 = Table()
        buttonSubLayout1.add(buttonList[1]).size(ViewType.seventhWidth(KcopSkin.screenWidth), ViewType.seventhHeight(KcopSkin.screenHeight))
        buttonSubLayout1.add(buttonList[2]).size(ViewType.seventhWidth(KcopSkin.screenWidth), ViewType.seventhHeight(KcopSkin.screenHeight))
        buttonSubLayout1.row()
        buttonSubLayout1.add(buttonList[0]).align(Align.center).colspan(2).size(ViewType.sixthWidth(KcopSkin.screenWidth) - 3, ViewType.sixthHeight(KcopSkin.screenHeight) - 3)

        val buttonSubLayout2 = Table()
        buttonSubLayout2.add(buttonList[3]).align(Align.center).size(ViewType.seventhWidth(KcopSkin.screenWidth) - 5, ViewType.seventhHeight(KcopSkin.screenHeight) - 5)
        buttonSubLayout2.row()
        buttonSubLayout2.add(buttonList[4]).size(ViewType.seventhWidth(KcopSkin.screenWidth), ViewType.seventhHeight(KcopSkin.screenHeight)).padTop(1f)
        buttonSubLayout2.row()
        buttonSubLayout2.add(buttonList[5]).size(ViewType.seventhWidth(KcopSkin.screenWidth), ViewType.seventhHeight(KcopSkin.screenHeight))

        val buttonLayout = Table()
        buttonLayout.add(buttonSubLayout1).padTop(2f).padLeft(2f).padBottom(2f)
        buttonLayout.add(buttonSubLayout2).padTop(2f).padRight(1f).padBottom(2f)

        buttonLayout.validate()
        buttonLayout.layout()

        return buttonLayout
    }

    override fun buildCtrl() {
        viewTable.add(Stack().apply {
            this.add(backgroundColorImg())
            this.add(buttonLayout())
        } ).size(this.tableWidth(), this.tableHeight())

        viewTable.clip()
    }

    var openMenu = {
        PauseView.isChecked = true
        PauseView.build()
        DisplayViewMenuHandler.currentMenuTag = MainMenu.tag
        DisplayView.menuOpen = true
        DisplayView.build()
    }

    var closeMenu = {
        isChecked[menuButton] = false
        MenuView.build()
        PauseView.isChecked = false
        PauseView.build()
        DisplayView.menuOpen = false
        DisplayView.build()
    }

}