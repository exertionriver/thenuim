package river.exertion.kcop.view.layout

enum class DisplayViewMode {

    DisplayViewKcop,
    DisplayViewFull,
    DisplayViewCenter
    ;
    companion object {
        var currentDVMode : DisplayViewMode = DisplayViewKcop
    }
}