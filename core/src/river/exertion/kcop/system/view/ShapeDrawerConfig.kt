package river.exertion.kcop.system.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import river.exertion.kcop.system.colorPalette.ColorPalette
import space.earlygrey.shapedrawer.ShapeDrawer

//https://github.com/earlygrey/shapedrawer/wiki/Using-Shape-Drawer
class ShapeDrawerConfig(val batch: Batch, val baseColor : Color = ColorPalette.of("white").color()) {

    var pixmap : Pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
    var texture : Texture
    var textureRegion: TextureRegion
    var shapeDrawer: ShapeDrawer

    init {
        pixmap.setColor(baseColor)
        pixmap.drawPixel(0, 0)

        texture = Texture(pixmap, true)
        textureRegion = TextureRegion(texture, 0, 0, 1, 1)
        shapeDrawer = ShapeDrawer(this.batch, textureRegion)
    }

    fun dispose() {
        this.texture.dispose()
        this.pixmap.dispose()
    }
}