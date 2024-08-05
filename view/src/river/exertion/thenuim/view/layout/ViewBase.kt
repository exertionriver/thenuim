package river.exertion.thenuim.view.layout

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.thenuim.view.TnmSkin
import river.exertion.thenuim.view.SdcHandler
import river.exertion.thenuim.view.TnmFont

interface ViewBase {

    var viewType : ViewType
    var viewTable : Table

    fun viewRect() = viewType.viewRect(TnmSkin.screenWidth, TnmSkin.screenHeight)

    fun tableWidth() = viewRect().width
    fun tableHeight() = viewRect().height
    fun tablePosX() = viewRect().x
    fun tablePosY() = viewRect().y

    fun backgroundColor() = if (TnmSkin.displayMode) viewType.defaultColor() else TnmSkin.BackgroundColor

    fun backgroundColorTexture() : TextureRegion {
        return SdcHandler.updorad("background_${viewType}", backgroundColor()).textureRegion().apply {
                this.setRegion(0, 0, tableWidth().toInt() - 1, tableHeight().toInt() - 1)
        }
    }

    fun backgroundColorImg() : Image = Image(backgroundColorTexture()).apply { this.setFillParent(true) }

    private fun clearTable() {
        viewTable.clearChildren()
        viewTable.clearListeners()

        viewTable.setSize(tableWidth(), tableHeight())
        viewTable.setPosition(tablePosX(), tablePosY())
    }

    fun layoutLabel() : Label {
        return Label(viewType.name, TnmSkin.labelStyle(TnmFont.TEXT, backgroundColor().label())).apply {
            this.setAlignment(Align.center)
        }
    }

    fun buildCtrl() {
        val stack = Stack()

        stack.onClick {
            println("layout View:${viewType.name}")
            println("x:${Gdx.input.getX()}, y:${Gdx.input.getY()}")
        }

        stack.add(backgroundColorImg())
        stack.add(layoutLabel())

        viewTable.add(stack).size(this.tableWidth(), this.tableHeight())
        viewTable.clip()
    }

    fun build() {
        clearTable()
        buildCtrl()
    }
}