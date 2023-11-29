package river.exertion.kcop.view.layout

enum class DisplayViewMode {

    KcopScreen,
    DisplayFullScreen,
    DisplayViewScreen
    ;
    companion object {
        var currentDVMode : DisplayViewMode = KcopScreen
    }
}