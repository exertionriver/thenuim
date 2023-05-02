package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.ColorPalette

//no longer used
class SaveProfileMenu(var screenWidth: Float, var screenHeight: Float)  {

    val menuSkin = Skin()

    val backgroundColor = ColorPalette.of("olive")

    var selectedProfileAssetInfo: List<String>? = null
    var selectedProfileAssetName : String? = null

    fun selectedProfileAssetName() = selectedProfileAssetName ?: throw Exception("SaveProfileMenu requires valid selectProfileAssetName")

    fun menuPane() = Table().apply {
        if (selectedProfileAssetInfo != null) {
            this.add(Label("save name: ", menuSkin))
                    //Label.LabelStyle(bitmapFont, backgroundColor.label().color())))

            val nameTextField = TextField(selectedProfileAssetName(), menuSkin)
                    //TextField.TextFieldStyle(bitmapFont, backgroundColor.label().color(), null, null, null))
          //  .apply {
//                this.alignment = Align.top
          //  }
            nameTextField.setTextFieldListener {
                textField, _ -> selectedProfileAssetName = textField.text
                this@SaveProfileMenu.actions.firstOrNull { it.label == "Overwrite" }?.apply { this.log = "Profile Saved : $selectedProfileAssetName" }
            }
            this.add(nameTextField).growX().top()
            this.row()

            selectedProfileAssetInfo!!.forEach { profileEntry ->
                this.add(Label(profileEntry, menuSkin
                        //Label.LabelStyle(bitmapFont, backgroundColor.label().color()))
                ).apply {
                    this.wrap = true
                }).colspan(2).growX()
                this.row()
            }
            this@SaveProfileMenu.actions.firstOrNull { it.label == "Overwrite" }?.apply { this.log = "Profile Saved : $selectedProfileAssetName" }
            //      this.debug()
        } else {
            this.add(Label("no profile info found", menuSkin)
                    //Label.LabelStyle(bitmapFont, backgroundColor.label().color()))
            ).growX().left()
            this@SaveProfileMenu.actions.firstOrNull { it.label == "Overwrite" }?.apply { this.label = "Error"; this.action = {} }
        }
        this.top()
    }

    val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    val navs = mutableListOf<ActionParam>()

    val actions = mutableListOf(
        ActionParam("Overwrite", {
            Switchboard.closeMenu()
            Switchboard.saveOverwriteSelectedProfile(selectedProfileAssetName())
        }, "Profile Saved!"),
/*        ActionParam("Merge", {
            Switchboard.closeMenu()
            Switchboard.saveProfile(selectedProfileAsset!!, AssetManagerHandler.SaveType.Merge)
        }, "Profile Saved : ${selectedProfileAsset?.profile?.name}"),
 */        //go back a menu
        ActionParam("Cancel", {
            MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, MenuViewMessage(breadcrumbEntries.keys.toList()[0]) )
        })
    )

/*
    (MessageChannel.INTER_MENU_BRIDGE.isType(msg.message) ) -> {
         val menuDataMessage: MenuDataMessage = MessageChannel.INTER_MENU_BRIDGE.receiveMessage(msg.extraInfo)

 //to menus from elsewhere
 if ( menuDataMessage.profileMenuDataParams != null ) {
     displayViewMenus.filter { it is SaveProfileMenu }.forEach { saveProfileMenu ->
         if (menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo != null) {
             (saveProfileMenu as SaveProfileMenu).selectedProfileAssetInfo = menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo
         }
         if (menuDataMessage.profileMenuDataParams!!.selectedProfileAssetName != null) {
             (saveProfileMenu as SaveProfileMenu).selectedProfileAssetName = menuDataMessage.profileMenuDataParams!!.selectedProfileAssetName
         }
     }
 } else {
     displayViewMenus.filter { it is SaveProfileMenu }.forEach { saveProfileMenu ->
         (saveProfileMenu as SaveProfileMenu).selectedProfileAssetInfo = null
         saveProfileMenu.selectedProfileAssetInfo = null
         saveProfileMenu.selectedProfileAssetName = null
     }
 }
 return true
}
*/
    fun tag() = tag
    fun label() = label

    companion object {
        const val tag = "saveProfileMenu"
        const val label = "Save"
    }

}