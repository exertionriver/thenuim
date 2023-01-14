package river.exertion.kcop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import space.earlygrey.shapedrawer.ShapeDrawer
import java.nio.file.Path

object Util {
    fun logDebug(tag : String, message : String) {
        if (Gdx.app != null)
            Gdx.app.debug(tag, message)
        else
            println("$tag: $message")
    }

    @Suppress("NewApi")
    fun internalFile(path : String) : FileHandle {
        if (Gdx.files != null)
            return Gdx.files.internal(path)
        else {
            val fullPath = Path.of("").toAbsolutePath().parent.toString() + "/android/assets/$path"
            return FileHandle(fullPath)
        }
    }

    val json = Json { ignoreUnknownKeys = true }

    inline fun <reified T>loader(fileHandle: FileHandle) : T? {
        try {
            val jsonElement = json.parseToJsonElement(fileHandle.readString())
            return json.decodeFromJsonElement(jsonElement) as T
        } catch (ex : Exception) {
            logDebug("loader", ex.toString())
        }
        return null
    }
}

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

    fun getDrawer() = shapeDrawer

    fun disposeShapeDrawerConfig() {
        this.texture.dispose()
        this.pixmap.dispose()
    }
}

fun BitmapFont.drawLabel(batch : Batch, location : Vector2, labelText : String, color : Color, fontSize : Int = 32) {

    val labelLayout = GlyphLayout(this, labelText, color, (fontSize * labelText.length).toFloat(), Align.left,true )
    this.draw(batch, labelLayout, location.x - labelLayout.width / 2 , location.y - labelLayout.height / 2)
}