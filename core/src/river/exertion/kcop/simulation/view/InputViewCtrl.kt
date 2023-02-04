package river.exertion.kcop.simulation.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.view.ViewType
import kotlin.reflect.jvm.javaMethod

class InputViewCtrl(viewType : ViewType, screenWidth: Float = 50f, screenHeight: Float = 50f) : ViewCtrl(ViewType.INPUTS, screenWidth, screenHeight) {

    override fun create() {
        clearTable()

        if (backgroundColor != null) {
            if (bitmapFont == null) throw Exception("${::create.javaMethod?.name}: bitmapFont needs to be set")
            if (batch == null) throw Exception("${::create.javaMethod?.name}: batch needs to be set")
            if (sdc == null) sdc = ShapeDrawerConfig(batch!!, backgroundColor!!.color())

            val stack = Stack()

            val backgroundImg = Image(sdc!!.textureRegion.apply {this.setRegion(tablePosX().toInt(), tablePosY().toInt(), tableWidth().toInt(), tableHeight().toInt()) })

            val viewLabel = Label(viewType.name, Label.LabelStyle(bitmapFont, backgroundColor!!.label().color()))
            viewLabel.setAlignment(Align.center)

            stack.onClick {
                println("layout View:${viewType.name}")
                println("x:${Gdx.input.getX()}, y:${Gdx.input.getY()}")
            }

            stack.add(backgroundImg)
            stack.add(viewLabel)

            this.add(stack)
        }

    }
}