package river.exertion.kcop.view.layout

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.view.KcopSkin

object PauseView : ViewBase {

    override var viewType = ViewType.PAUSE
    override var viewTable = Table()

    var isChecked = false
        set(value) {
            field = value
            toggleImmersionPause()
        }

    fun clickButton() : Button {

        val innerButton = Button(KcopSkin.skin)
        //override from ctrl
        innerButton.isChecked = this@PauseView.isChecked

        innerButton.onClick {
            this@PauseView.isChecked = !this@PauseView.isChecked
            toggleImmersionPause()
        }

        return innerButton
    }

    fun toggleImmersionPause() {
//        val messageType = if (isChecked) NarrativeMessage.NarrativeMessageType.Pause else NarrativeMessage.NarrativeMessageType.Unpause
//        MessageChannelEnum.NARRATIVE_BRIDGE.send(null, NarrativeMessage(messageType, null) )
    }

    override fun buildCtrl() {
        viewTable.add(Stack().apply {
            this.add(backgroundColorImg())
            this.add(Table().apply {
                this.add(clickButton()).align(Align.center).size(this@PauseView.tableWidth() - 5, this@PauseView.tableHeight() - 5)
            })
        } ).size(this.tableWidth(), this.tableHeight())

        viewTable.clip()
    }
}