package river.exertion.thenuim.view.layout

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.thenuim.base.KcopBase
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.view.KcopSkin
import river.exertion.thenuim.view.ViewKlop.KcopBridge
import river.exertion.thenuim.view.menu.DisplayViewMenuHandler
import river.exertion.thenuim.view.menu.MainMenu
import river.exertion.thenuim.view.messaging.KcopSimulationMessage

object ButtonView : ViewBase {

    override var viewType = ViewType.MENU
    override var viewTable = Table()

    var buttonList = mutableListOf<Button>()

    val menuButton = 0
    val displayMode = 1
    val displayFullScreen = 3
    val displayViewScreen = 4

    var assignableButtons = mutableMapOf(
        menuButton to {
            if (isChecked[menuButton] == true)
                openMenu()
            else
                closeMenu()
        },
        displayMode to {
            KcopSkin.displayMode = isChecked[displayMode]!!
            KcopBase.stage.isDebugAll = KcopSkin.displayMode
            ViewLayout.rebuild()
       },
        2 to {
            LogView.addLog("Cycle Plugin")
            MessageChannelHandler.send(KcopBridge, KcopSimulationMessage(KcopSimulationMessage.KcopMessageType.NextPlugin))
        },
        displayFullScreen to {
            MessageChannelHandler.send(KcopBridge, KcopSimulationMessage(KcopSimulationMessage.KcopMessageType.DisplayFullScreen))
        },
        displayViewScreen to {
            MessageChannelHandler.send(KcopBridge, KcopSimulationMessage(KcopSimulationMessage.KcopMessageType.DisplayViewScreen))
        },
        5 to {
//            KcopSkin.displayMode = isChecked[5]!!
//            ViewLayout.rebuild()
        }
    )

    var isChecked : MutableMap<Int, Boolean> = mutableMapOf()

    fun buttonLayout() : Table {
        //idx 0 is large button, idx 1 - 5 are smaller bordering buttons
        buttonList.clear()

        (0..5).forEach { idx ->

            val innerButton = Button(KcopSkin.skin).apply { KcopSkin.addOnClick(this) }

            //override from ctrl
            innerButton.isChecked = this@ButtonView.isChecked[idx] == true

            innerButton.onClick {
                this@ButtonView.isChecked[idx] = !(this@ButtonView.isChecked[idx] ?: false)

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
        ViewLayout.rebuild()
    }

    var closeMenu = {
        isChecked[menuButton] = false
        ButtonView.build()
        PauseView.isChecked = false
        PauseView.build()
        DisplayView.menuOpen = false
        ViewLayout.rebuild()
    }

}