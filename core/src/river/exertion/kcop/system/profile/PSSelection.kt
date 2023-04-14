package river.exertion.kcop.system.profile

interface PSSelection {

    val selectionKey : String
    val selectionLabel : String
    val options : List<PSOption>
}