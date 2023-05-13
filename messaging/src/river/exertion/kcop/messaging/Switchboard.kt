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

}