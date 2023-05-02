package river.exertion.kcop.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import space.earlygrey.shapedrawer.ShapeDrawer

//https://github.com/earlygrey/shapedrawer/wiki/Using-Shape-Drawer
class ShapeDrawerConfig(val batch: Batch, var baseColor : Color = ColorPalette.of("white").color(), var alpha : Float?) {

    private var pixmap : Pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
    private var texture : Texture
    private var textureRegion : TextureRegion
    private var shapeDrawer : ShapeDrawer

    private var resetTexture = false
    private var resetTextureRegion = false
    private var resetShapeDrawer = false

    init {
        pixmap.setColor(baseColor.r, baseColor.g, baseColor.b, alpha ?: 1f)
        pixmap.drawPixel(0, 0)
        pixmap.fill()
        texture = Texture(pixmap, true)
        textureRegion = TextureRegion(texture, 0, 0, 1, 1)
        shapeDrawer = ShapeDrawer(batch, textureRegion)
    }

    fun setAlpha(alpha : Float) {
        setColor(this.baseColor, alpha)
    }

    fun setColor(baseColor: Color, alpha : Float? = 1f) {
        if (baseColor != this.baseColor || alpha != this.alpha) {
            this.baseColor = baseColor
            this.alpha = alpha

            pixmap.setColor(baseColor.r, baseColor.g, baseColor.b, alpha ?: 1f)
            pixmap.fill()

            resetTexture = true
            resetTextureRegion = true
            resetShapeDrawer = true
        }
    }

    fun texture() : Texture {
        if (resetTexture) {
            texture.dispose()
            texture = Texture(pixmap, true)

            resetTexture = false
        }

        return texture
    }

    fun textureRegion() : TextureRegion {
        if (resetTextureRegion) {
            textureRegion = TextureRegion(texture(), 0, 0, 1, 1)

            resetTextureRegion = false
        }

        return textureRegion
    }

    fun shapeDrawer(): ShapeDrawer {
        if (resetShapeDrawer) {
            shapeDrawer = ShapeDrawer(batch, textureRegion())

            resetShapeDrawer = false
        }

        return shapeDrawer
    }

    fun dispose() {
        this.texture.dispose()
        this.pixmap.dispose()
    }
}