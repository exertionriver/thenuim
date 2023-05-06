package river.exertion.kcop.sim.narrative.view

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
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.ViewPackage.DisplayViewTextureBridge
import river.exertion.kcop.view.messaging.DisplayViewTextureMessage

@Serializable
class DVImagePane : DVPane() {

    override var idx : String? = null
    override val width : String? = null
    override val height : String? = null
    override val refineX : String? = null
    override val refineY : String? = null

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

        val innerTable = Table()
        innerTable.add(contentImage)
            .size(dvpType().width(screenWidth) + refineX(), dvpType().height(screenHeight) + refineY())
            .grow()

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
//                    println("fadeIn[$layoutPaneIdx]: alpha:${displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx]}")

                    if (textureLock != null && textureLock == this) {

                        val alpha = this@DVImagePane.alphaMask
                        if (alpha >= .1f) {
                            this@DVImagePane.alphaMask = alpha - .1f
                            MessageChannelHandler.send(
                                DisplayViewTextureBridge,
                                DisplayViewTextureMessage(DisplayViewTextureMessage.DisplayViewTextureMessageType.Rebuild)
                            )
                        } else {
                            this@DVImagePane.textureLock = null
                            MessageChannelHandler.send(
                                DisplayViewTextureBridge,
                                DisplayViewTextureMessage(DisplayViewTextureMessage.DisplayViewTextureMessageType.Rebuild)
                            )
                            this.cancel()
                        }
                    } else {
//                        println("texture unlocked! [$layoutPaneIdx]")
                        this@DVImagePane.alphaMask = 0f
                        MessageChannelHandler.send(
                            DisplayViewTextureBridge,
                            DisplayViewTextureMessage(DisplayViewTextureMessage.DisplayViewTextureMessageType.Rebuild)
                        )
                        this.cancel()
                    }
                }
            }.apply { this@DVImagePane.textureLock = this }, 0f, .05f)
        }
    }

    fun fadeImageOut(texture : Texture?) {
        if (this.paneTexture != texture) {
            this.alphaMask = 0f
            Timer.schedule(object : Timer.Task() {
                override fun run() {
//                    println("fadeOut[$layoutPaneIdx]: alpha:${displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx]}")

                    if (textureLock != null && textureLock == this) {

                        val alpha = this@DVImagePane.alphaMask
                        if (alpha <= .9f) {
                            this@DVImagePane.alphaMask = alpha + .1f
                            MessageChannelHandler.send(
                                DisplayViewTextureBridge,
                                DisplayViewTextureMessage(DisplayViewTextureMessage.DisplayViewTextureMessageType.Rebuild)
                            )
                        } else {
                            this@DVImagePane.textureLock = null
                            this@DVImagePane.paneTexture = null
                            this@DVImagePane.alphaMask = 1f
                            MessageChannelHandler.send(
                                DisplayViewTextureBridge,
                                DisplayViewTextureMessage(DisplayViewTextureMessage.DisplayViewTextureMessageType.Rebuild)
                            )
                            this.cancel()
                        }
                    } else {
//                        println("texture unlocked! [$layoutPaneIdx]")
                        this@DVImagePane.alphaMask = 1f
                        MessageChannelHandler.send(
                            DisplayViewTextureBridge,
                            DisplayViewTextureMessage(DisplayViewTextureMessage.DisplayViewTextureMessageType.Rebuild)
                        )
                        this.cancel()
                    }
                }
            }.apply { this@DVImagePane.textureLock = this }, 0f, .05f)
        }
    }

    companion object {
        private val emptyImage = Image()
    }
}