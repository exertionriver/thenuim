package river.exertion.kcop.simulation.view.displayViewLayouts

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import kotlinx.serialization.SerialName

@SerialName("image")
class DVImagePane(
    override val type: String = ""
) : DVPane() {

    @Transient
    var paneTexture : Texture? = null

    override fun layoutPane(screenWidth : Float, screenHeight : Float, randomColorImage : Image, randomColorLabelStyle : Label.LabelStyle, paneLabel : String) : Stack {
        return super.layoutPane(screenWidth, screenHeight, randomColorImage, randomColorLabelStyle, DVPaneTypes.IMAGE.layoutTag())
    }

    override fun contentPane(screenWidth : Float, screenHeight : Float) : Stack {
        val innerTable = Table()
        innerTable.add(Image(TextureRegionDrawable(TextureRegion(paneTexture))))
            .size(dvpType().width(screenWidth) + refineX(), dvpType().height(screenHeight) + refineY())
            .grow()

        return Stack().apply {
            this.add(innerTable)
        }
    }
}