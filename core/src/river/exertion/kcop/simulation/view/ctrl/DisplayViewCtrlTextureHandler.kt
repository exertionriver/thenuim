package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Timer

object DisplayViewCtrlTextureHandler {

    var textureLock : MutableMap<Int, Timer.Task?> = mutableMapOf()

    //clears if texture is null
    fun DisplayViewCtrl.showImage(layoutPaneIdx : Int, texture : Texture?) {

        if (texture != null) {
            displayViewLayouts[currentLayoutIdx].paneTextures[layoutPaneIdx] = texture
        } else {
            displayViewLayouts[currentLayoutIdx].paneTextures[layoutPaneIdx] = displayViewLayouts[currentLayoutIdx].paneBgTextures[layoutPaneIdx]
        }
//        displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] = 1f
    }

    fun DisplayViewCtrl.fadeImageIn(layoutPaneIdx : Int, texture : Texture?) {
        if (displayViewLayouts[currentLayoutIdx].paneTextures[layoutPaneIdx] != texture) {
            displayViewLayouts[currentLayoutIdx].paneTextures[layoutPaneIdx] = texture
            displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] = 1f
            Timer.schedule(object : Timer.Task() {
                override fun run() {
//                    println("fadeIn[$layoutPaneIdx]: alpha:${displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx]}")

                    if (this@DisplayViewCtrlTextureHandler.textureLock[layoutPaneIdx] != null &&
                            this@DisplayViewCtrlTextureHandler.textureLock[layoutPaneIdx] == this) {

                        val alpha = displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] ?: 1f
                        if (alpha >= .1f) {
                            displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] = alpha - .1f
                        } else {
                            this@DisplayViewCtrlTextureHandler.textureLock[layoutPaneIdx] = null
                            this.cancel()
                        }
                        this@fadeImageIn.build()
                    } else {
//                        println("texture unlocked! [$layoutPaneIdx]")
                        displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] = 0f
                        this@fadeImageIn.build()
                        this.cancel()
                    }
                }
            }.apply { this@DisplayViewCtrlTextureHandler.textureLock[layoutPaneIdx] = this }, 0f, .05f)
        }
    }

    fun DisplayViewCtrl.fadeImageOut(layoutPaneIdx : Int, texture : Texture?) {
        if (displayViewLayouts[currentLayoutIdx].paneTextures[layoutPaneIdx] != texture) {
            displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] = 0f
            Timer.schedule(object : Timer.Task() {
                override fun run() {
//                    println("fadeOut[$layoutPaneIdx]: alpha:${displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx]}")

                    if (this@DisplayViewCtrlTextureHandler.textureLock[layoutPaneIdx] != null &&
                            this@DisplayViewCtrlTextureHandler.textureLock[layoutPaneIdx] == this) {

                        val alpha = displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] ?: 0f
                        if (alpha <= .9f) {
                            displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] = alpha + .1f
                        } else {
                            this@DisplayViewCtrlTextureHandler.textureLock[layoutPaneIdx] = null
                            displayViewLayouts[currentLayoutIdx].paneTextures.remove(layoutPaneIdx)
//                            displayViewLayouts[currentLayoutIdx].paneTextures[layoutPaneIdx] = texture
                            this.cancel()
                        }
                        this@fadeImageOut.build()
                    } else {
//                        println("texture unlocked! [$layoutPaneIdx]")
                        displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha[layoutPaneIdx] = 1f
                        this@fadeImageOut.build()
                        this.cancel()
                    }
                }
            }.apply { this@DisplayViewCtrlTextureHandler.textureLock[layoutPaneIdx] = this }, 0f, .05f)
        }
    }

    //TODO: look into fade
    fun DisplayViewCtrl.crossFadeImage(layoutPaneIdx : Int, texture : Texture?) {
        showImage(layoutPaneIdx, texture)
    }

    fun DisplayViewCtrl.clearImages() {
        displayViewLayouts[currentLayoutIdx].paneTextures.clear()
        displayViewLayouts[currentLayoutIdx].paneTextureMaskAlpha.entries.forEach { it.setValue(1f) }
    }
}