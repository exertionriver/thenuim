package river.exertion.kcop.system.ecs.component

interface PSSelection {

    val selectionKey : String
    val selectionLabel : String
    val options : List<PSOption>
}