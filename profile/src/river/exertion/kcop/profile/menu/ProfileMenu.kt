package river.exertion.kcop.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.asset.view.KcopSkin
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.profile.Profile
import river.exertion.kcop.profile.ProfilePackage
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.view.ViewPackage
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.DisplayViewMessage
import river.exertion.kcop.view.messaging.menuParams.ActionParam

object ProfileMenu : DisplayViewMenu {

    override val tag = "profileMenu"
    override val label = "Profile"

    override val backgroundColor = ColorPalette.of("green")

    override fun menuPane() : Table {

        val listCtrl = com.badlogic.gdx.scenes.scene2d.ui.List<String>(KcopSkin.skin)
        val profileAssetsMap = ProfilePackage.profileAssets.reload().associateBy { it.assetTitle() }

        if (profileAssetsMap.isNotEmpty()) {
            ProfilePackage.selectedProfileAsset = profileAssetsMap.entries.first().value as ProfileAsset

            listCtrl.onChange {
                ProfilePackage.selectedProfileAsset = profileAssetsMap.values.toList()[this.selectedIndex] as ProfileAsset
            }

            listCtrl.setItems(profileAssetsMap.keys.toGdxArray())
        } else {
            listCtrl.setItems(listOf("no profiles found").toGdxArray() )
        }

        listCtrl.alignment = Align.topLeft

        return Table().apply {
            this.add(listCtrl).growY().top().left()
//            this.add(Table())
//            this.debug()
            this.top()
            this.left()
        }
    }

    override val breadcrumbEntries = mapOf(
        MainMenu.tag to MainMenu.label
    )

    override fun navs() = assignableNavs

    override val actions = mutableListOf<ActionParam>()

    val assignableNavs = mutableListOf(
        ActionParam("Load >", {
            DisplayViewMenuHandler.currentMenuTag = LoadProfileMenu.tag
            MessageChannelHandler.send(ViewPackage.DisplayViewBridge, DisplayViewMessage(DisplayViewMessage.DisplayViewMessageType.Rebuild) )
        }),
        ActionParam("New >", {
            NewProfileMenu.newName = Profile.genName()
            DisplayViewMenuHandler.currentMenuTag = NewProfileMenu.tag
            MessageChannelHandler.send(ViewPackage.DisplayViewBridge, DisplayViewMessage(DisplayViewMessage.DisplayViewMessageType.Rebuild) )
        })
    )
}