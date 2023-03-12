package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.assets.load
import ktx.collections.toGdxArray
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.assets.ProfileAssetLoader
import river.exertion.kcop.assets.ProfileAssets
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.view.DisplayViewMenuMessage
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

class ProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("green")

    val am = AssetManager()
    val profiles = mutableMapOf<String, ProfileAsset>()

    override fun menuPane(bitmapFont: BitmapFont) : Table {
        loadProfiles()

        val listCtrl = List<String>(ListStyle().apply {
            this.font = bitmapFont
            this.selection = TextureRegionDrawable(TextureRegion(Texture("images/kobold64.png")))
        })

        listCtrl.onChange {
            navs.forEach { it.value.profile = profiles[selected] }
        }

        listCtrl.debug()
        listCtrl.alignment = Align.topLeft
        listCtrl.setItems(profiles.keys.toGdxArray())

        return Table().apply {
            this.add(listCtrl).growY().top().left()
            this.add(Table())
            this.debug()
            this.top()
            this.left()
        }
    }

    override val breadcrumbEntries = mapOf(
        MainMenu.tag to MainMenu.label
    )

    override val navs = mapOf(
        "Load >" to ProfileMenuParams("loadProfileMenu", null),
        "Save >" to ProfileMenuParams("saveProfileMenu", null)
    )

    override val actions = mapOf<String, Pair<String, () -> Unit>>()

    @Suppress("NewApi")
    fun loadProfiles() {

        val lfhr = LocalFileHandleResolver()
        am.setLoader(ProfileAsset::class.java, ProfileAssetLoader(lfhr))

        val profilePath = Path(ProfileAssets.profileAssetLocation)

        profilePath.listDirectoryEntries().forEach {
            am.load<ProfileAsset>(it.toString())
        }
        am.finishLoading()

        am.assetNames.forEach {
            if ((am.get(it) as ProfileAsset).status == null)
                profiles[it] = am[it]
        }
    }

    override fun tag() = tag
    override fun label() = label

    override fun dispose() {
        am.dispose()
        super.dispose()
    }

    companion object {
        const val tag = "profileMenu"
        const val label = "Profile"
    }
}