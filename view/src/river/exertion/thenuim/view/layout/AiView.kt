package river.exertion.thenuim.view.layout

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.thenuim.view.KcopSkin

object AiView : ViewBase {

    override var viewType = ViewType.AI
    override var viewTable = Table()

    var hintTextEntries : MutableMap<String, String> = mutableMapOf()

    fun hintText() = hintTextEntries.values.reduceOrNull { acc, s -> acc + "\n$s"} ?: ""

    var isChecked = false

    fun clickButton() : Button {

        val innerButton = Button(KcopSkin.skin)

        KcopSkin.addOnClick(innerButton)

        //override from ctrl
        innerButton.isChecked = this@AiView.isChecked

        innerButton.onClick {
            this@AiView.isChecked = !this@AiView.isChecked
            LogView.addLog("AI character set to: ${if (this.isChecked) "On" else "Off"}")

            if (!this@AiView.isChecked) clearHints()
        }

        return innerButton
    }

    override fun buildCtrl() {
        viewTable.add(Stack().apply {
            this.add(backgroundColorImg())
            this.add(Table().apply {
                this.add(clickButton()).align(Align.center).size(this@AiView.tableWidth() - 5, this@AiView.tableHeight() - 5)
            })
        } ).size(this.tableWidth(), this.tableHeight())

        viewTable.clip()
    }

    fun addHint(eventId : String, eventReport : String) {
        hintTextEntries[eventId] = eventReport
    }

    fun clearHints() {
        hintTextEntries.clear()
    }
}