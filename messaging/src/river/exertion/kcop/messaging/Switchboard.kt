package river.exertion.kcop.messaging

object Switchboard {

    val entries = mutableListOf<SwitchboardEntry>()

    fun checkByTag(switchboardTag : String) : SwitchboardEntry? {
        return entries.firstOrNull { it.switchboardTag == switchboardTag }
    }

    fun byTag(switchboardTag : String) : SwitchboardEntry {
        return checkByTag(switchboardTag) ?: throw Exception("${this::class.simpleName}:${Switchboard::byTag.name} : entry $switchboardTag not found")
    }

    fun addEntry(switchboardEntry: SwitchboardEntry) {
        val entryCheck = checkByTag(switchboardEntry.switchboardTag)

        if (entryCheck == null) entries.add(switchboardEntry) else throw Exception("${this::class.simpleName}:${Switchboard::addEntry.name} : entry ${switchboardEntry.switchboardTag} already added, please remove entry or modify action")
    }

    fun addChannels(switchboardEntries: List<SwitchboardEntry>) {
        switchboardEntries.forEach { addEntry(it) }
    }

    fun removeEntry(switchboardEntry: SwitchboardEntry) {
        val entryToRemove = byTag(switchboardEntry.switchboardTag)

        entries.remove(entryToRemove)
    }

    fun removeEntries(switchboardEntries : List<SwitchboardEntry>) {
        switchboardEntries.forEach { removeEntry(it) }
    }

    fun modifyAction(switchboardTag : String, switchboardAction : () -> Unit) {
        byTag(switchboardTag).switchboardTagAction = switchboardAction
    }

    fun executeAction(switchboardTag : String) {
        byTag(switchboardTag).switchboardTagAction?.let { it() }
    }

    /*

        fun clearMenu() {
            //clear menu state
            MessageChannelEnum.INTER_MENU_BRIDGE.send(null, MenuDataMessage())
            MessageChannelEnum.INTRA_MENU_BRIDGE.send(null, MenuNavMessage())
            //put nav back on main menu
            MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, MenuViewMessage(MainMenu.tag))
        }

        fun openMenu() {
            clearMenu()
            MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, MenuViewMessage(null, 0, true))
            MessageChannelEnum.PAUSE_VIEW_BRIDGE.send(null, PauseViewMessage(true))
            MessageChannelEnum.MENU_VIEW_BRIDGE.send(null, MenuViewMessage(null, 0, true))
        }

        fun loadSelectedProfile() {
            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.InitSelectedProfile))
        }

        fun loadSelectedNarrative() {
            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.InitSelectedNarrative))
        }

        fun saveOverwriteSelectedProfile(saveName : String) {

            MessageChannelEnum.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(
                AMHSaveMessage.AMHSaveMessageType.SaveOverwriteProfile, saveName)
            )

        }

        fun newProfile(saveName : String) {

            MessageChannelEnum.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(
                AMHSaveMessage.AMHSaveMessageType.NewProfile, saveName)
            )
        }

        fun noloadProfile() {
            MessageChannelEnum.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                    EngineComponentMessage.EngineComponentMessageType.RemoveComponent,
                    ProfileEntity.entityName, ProfileComponent::class.java))
            MessageChannelEnum.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, AssetManagerHandlerCl.NoProfileLoaded))
            MessageChannelEnum.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                    EngineComponentMessage.EngineComponentMessageType.RemoveComponent,
                    ProfileEntity.entityName, IRLTimeComponent::class.java))
            MessageChannelEnum.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.ResetTime))
        }

        fun noloadNarrative() {
            MessageChannelEnum.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                    EngineComponentMessage.EngineComponentMessageType.RemoveComponent,
                    ProfileEntity.entityName, NarrativeComponent::class.java))
            MessageChannelEnum.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(TextViewMessage.TextViewMessageType.ReportText, AssetManagerHandlerCl.NoNarrativeLoaded))
            MessageChannelEnum.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                    EngineComponentMessage.EngineComponentMessageType.RemoveComponent,
                    ProfileEntity.entityName, ImmersionTimerComponent::class.java))
            MessageChannelEnum.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.ResetTime))
        }


    */
}