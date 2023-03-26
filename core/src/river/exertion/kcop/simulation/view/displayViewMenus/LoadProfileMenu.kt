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
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuParams
import river.exertion.kcop.system.*
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.messaging.*
import river.exertion.kcop.system.messaging.messages.*
import river.exertion.kcop.system.view.ShapeDrawerConfig
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

class LoadProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu, ProfileReqMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("teal")

    override var profile : ProfileAsset? = null
    override var currentNarrativeAsset : NarrativeAsset? = null
    override val am = AssetManager()

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
//        this.debug()
    }

    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val navs = mapOf<String, MenuParams>()

    override val actions = mutableMapOf(
        "Yes" to Pair("Profile Loaded!") { loadProfile() },
        "No" to Pair(null) {}
    )

    fun profileAssetTitle() = profile?.assetPath

    fun profileAssetInfo() : MutableList<String?> {

        val returnList = mutableListOf<String?>()

        if ((profile != null) && (profile!!.profile != null)) {
            returnList.add("name: ${profile!!.profile!!.name}")
            returnList.add("current: ${profile!!.profile!!.currentImmersionId}: ${profile!!.profile!!.currentImmersionBlockId}")

            if (profile!!.profile!!.statuses.isNotEmpty()) returnList.add("\nstatuses:")

            val listMaxSize = profile!!.profile!!.statuses.size.coerceAtMost(8)

            profile!!.profile!!.statuses.sortedByDescending { it.value }.subList(0, listMaxSize).forEach {
                returnList.add("${it.key}: ${it.value} (${it.cumlImmersionTime})")
            }

            this.actions["Yes"] = Pair("Profile loaded: ${profile!!.profile!!.name}", this.actions["Yes"]!!.second)

            currentNarrativeAsset = loadNarrativeAsset(profile!!.profile!!.currentImmersionId!!)
        }

        if ( returnList.isEmpty() ) returnList.add("no profile info found")

        return returnList
    }

    fun loadProfile() {
        //clear profile entities
        MessageChannel.ECS_ENGINE_ENTITY_BRIDGE.send(null, EngineEntityMessage(
            EngineEntityMessageType.REMOVE_ALL_ENTITIES,
            ProfileEntity::class.java, profile)
        )

        //remove statuses
        MessageChannel.STATUS_VIEW_BRIDGE.send(null, StatusViewMessage(StatusViewMessageType.CLEAR_STATUSES))

        Switchboard.closeMenu()

        //instantiate profile
        MessageChannel.ECS_ENGINE_ENTITY_BRIDGE.send(null, EngineEntityMessage(
            EngineEntityMessageType.INSTANTIATE_ENTITY,
            ProfileEntity::class.java, profile)
        )

        //add current narrative
        if (profile?.profile != null) {
            MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessageType.ADD_COMPONENT,
                profile!!.profile!!.id, NarrativeComponent::class.java, null,
                NarrativeComponent.NarrativeComponentInit(currentNarrativeAsset!!,
                    profile!!.profile!!.currentImmersionBlockId!!,
                    profile!!.profile!!.statuses.firstOrNull { it.key == currentNarrativeAsset?.narrative?.id }?.cumlImmersionTime))
            )

        }
    }


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