package river.exertion.kcop.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import kotlinx.serialization.json.decodeFromJsonElement
import ktx.assets.Asset
import ktx.assets.getAsset
import ktx.assets.load
import river.exertion.kcop.NarrativeSequence
import river.exertion.kcop.Util
import java.io.InputStream

enum class NarrativeAssets(val path: String) {
    KCopTest("kcop/narrative/nsb_kcop.json")
}

fun AssetManager.load(asset: NarrativeAssets) = load<NarrativeSequence>(asset.path)
operator fun AssetManager.get(asset: NarrativeAssets) = getAsset<NarrativeSequence>(asset.path)