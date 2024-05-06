package river.exertion.thenuim.view.layout.displayViewLayout

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Timer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import river.exertion.thenuim.view.layout.ViewLayout
import river.exertion.thenuim.view.layout.displayViewLayout.asset.DVAlign

@Serializable
class DVImagePane : DVPane() {

    override var tag : String? = null
    override var width : String? = null
    override var height : String? = null
    override var refineX : String? = null
    override var refineY : String? = null
    override var padLeft : String? = null
    override var padRight : String? = null
    override var align : String? = null

    @Transient
    override var paneType: String? = DVPaneTypes.IMAGE.tag()

    @Transient
    @Contextual
    var paneTexture : Texture? = null

    override fun layoutPane(screenWidth : Float, screenHeight : Float, randomColorImage : Image, randomColorLabelStyle : Label.LabelStyle, paneLabel : String?) : Stack {
        return super.layoutPane(screenWidth, screenHeight, randomColorImage, randomColorLabelStyle, DVPaneTypes.IMAGE.layoutTag())
    }

    override fun contentPane(screenWidth : Float, screenHeight : Float) : Stack {
        val contentImage = if (paneTexture != null) Image(TextureRegionDrawable(TextureRegion(paneTexture))) else emptyImage
        contentImage.align = DVAlign.byTag(align).align()

        val innerTable = Table()
        innerTable.add(contentImage).apply {
            if (this@DVImagePane.width != null) this.width(dvpType().width(screenWidth) + refineX())
            if (this@DVImagePane.height != null) this.height(dvpType().height(screenHeight) + refineY())
            this.grow()
        }

        return Stack().apply {
            this.add(innerTable)
        }
    }

    @Transient
    var textureLock : Timer.Task? = null

    fun fadeImageIn(texture : Texture?) {
        if (this.paneTexture != texture) {
            this.paneTexture = texture
            this.alphaMask = 1f
            Timer.schedule(object : Timer.Task() {
                override fun run() {

                    if (textureLock != null && textureLock == this) {

                        val alpha = this@DVImagePane.alphaMask
                        if (alpha >= .1f) {
                            this@DVImagePane.alphaMask = alpha - .1f
                            ViewLayout.rebuild()
                        } else {
                            this@DVImagePane.textureLock = null
                            ViewLayout.rebuild()
                            this.cancel()
                        }
                    } else {
                        this@DVImagePane.alphaMask = 0f
                        ViewLayout.rebuild()
                        this.cancel()
                    }
                }
            }.apply { this@DVImagePane.textureLock = this }, 0f, .05f)
        }
    }

    fun fadeImageOut() {
        this.alphaMask = 0f
        Timer.schedule(object : Timer.Task() {
            override fun run() {

                if (textureLock != null && textureLock == this) {

                    val alpha = this@DVImagePane.alphaMask
                    if (alpha <= .9f) {
                        this@DVImagePane.alphaMask = alpha + .1f
                        ViewLayout.rebuild()
                    } else {
                        this@DVImagePane.textureLock = null
                        this@DVImagePane.paneTexture = null
                        this@DVImagePane.alphaMask = 1f
                        ViewLayout.rebuild()
                        this.cancel()
                    }
                } else {
                    this@DVImagePane.alphaMask = 1f
                    ViewLayout.rebuild()
                    this.cancel()
                }
            }
        }.apply { this@DVImagePane.textureLock = this }, 0f, .05f)
    }

    companion object {
        private val emptyImage = Image()
    }
}