package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.assets.load
import ktx.collections.gdxArrayOf
import river.exertion.kcop.assets.*
import river.exertion.kcop.system.*
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.messaging.*
import river.exertion.kcop.system.messaging.messages.EngineComponentMessage
import river.exertion.kcop.system.messaging.messages.EngineComponentMessageType
import river.exertion.kcop.system.messaging.messages.EngineEntityMessage
import river.exertion.kcop.system.messaging.messages.EngineEntityMessageType
import river.exertion.kcop.system.view.ShapeDrawerConfig
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

class LoadProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("teal")

    var profileAsset : ProfileAsset? = null
    var currentNarrativeAsset : NarrativeAsset? = null

    val am = AssetManager()

    fun profileAssetTitle() = profileAsset?.assetPath

    fun profileAssetInfo() : MutableList<String?> {

        val returnList = mutableListOf<String?>()

        if ((profileAsset != null) && (profileAsset!!.profile != null)) {
            returnList.add("name: ${profileAsset!!.profile!!.name}")
            returnList.add("current: ${profileAsset!!.profile!!.currentImmersionId}: ${profileAsset!!.profile!!.currentImmersionBlockId}")

            if (profileAsset!!.profile!!.statuses.isNotEmpty()) returnList.add("\nstatuses:")

            val listMaxSize = profileAsset!!.profile!!.statuses.size.coerceAtMost(8)

            profileAsset!!.profile!!.statuses.sortedByDescending { it.value }.subList(0, listMaxSize).forEach {
                returnList.add("${it.key}: ${it.value} (${it.cumlImmersionTime})")
            }
            profileAsset!!.profile!!.statuses.sortedByDescending { it.value }.subList(0, listMaxSize).forEach {
                returnList.add("${it.key}: ${it.value} (${it.cumlImmersionTime})")
            }

            this.actions["Yes"] = Pair("Profile loaded: ${profileAsset!!.profile!!.name}", this.actions["Yes"]!!.second)

            currentNarrativeAsset = loadNarrativeAsset(profileAsset!!.profile!!.currentImmersionId!!)
        }

        if ( returnList.isEmpty() ) returnList.add("no profile info found")

        return returnList
    }

    override fun menuPane(bitmapFont: BitmapFont) = Table().apply {

        this.add(Label(profileAssetTitle(), LabelStyle(bitmapFont, backgroundColor.label().color())).apply {
            this.wrap = true
        }).growX()
        this.row()
        val profileAssetInfo = profileAssetInfo()
        profileAssetInfo.forEach { profileEntry ->
            this.add(Label(profileEntry, LabelStyle(bitmapFont, backgroundColor.label().color())).apply {
                this.wrap = true
            }).growX().left()
            this.row()
        }
        this.top()
        this.debug()
    }

    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val navs = mapOf<String, MenuParams>()

    override val actions = mutableMapOf(
        "Yes" to Pair("Profile Loaded!") {
            MessageChannel.ECS_ENGINE_ENTITY_BRIDGE.send(null, EngineEntityMessage(
                EngineEntityMessageType.INSTANTIATE_ENTITY,
                ProfileEntity::class.java, profileAsset)
            )
            if (profileAsset?.profile != null) {
                MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                    EngineComponentMessageType.ADD_COMPONENT,
                    profileAsset!!.profile!!.id, ProfileComponent::class.java, profileAsset?.profile)
                )

                MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                    EngineComponentMessageType.ADD_COMPONENT,
                    profileAsset!!.profile!!.id, NarrativeComponent::class.java, Pair(currentNarrativeAsset, profileAsset!!.profile!!.currentImmersionBlockId))
                )
            }
        },
        "No" to Pair(null) {}
    )

    @Suppress("NewApi")
    fun loadNarrativeAsset(currentImmersionId : String) : NarrativeAsset? {

        val lfhr = LocalFileHandleResolver()
        am.setLoader(NarrativeAsset::class.java, NarrativeAssetLoader(lfhr))

        val narrativePath = Path(NarrativeAssets.narrativeAssetLocation)

        narrativePath.listDirectoryEntries().forEach {
            am.load<NarrativeAsset>(it.toString())
        }
        am.finishLoading()

        return am.getAll(NarrativeAsset::class.java, gdxArrayOf()).firstOrNull { it.narrative?.id == currentImmersionId }
    }

    override fun tag() = tag
    override fun label() = label

    override fun dispose() {
        am.dispose()
        super.dispose()
    }

    companion object {
        const val tag = "loadProfileMenu"
        const val label = "Load"
    }
}