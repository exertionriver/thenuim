package river.exertion.kcop.system.view

data class InputViewMessage(val targetView : ViewType = ViewType.INPUTS, val event : String, val eventParams : Map<String, Any>) {

    companion object {
        const val KeycodeStrKey = "keycodeStr"
        const val CharacterKey = "character"
        const val ScreenXKey = "screenX"
        const val ScreenYKey = "screenY"
        const val PointerKey = "pointer"
        const val ButtonKey = "button"
        const val AmountXKey = "amountX"
        const val AmountYKey = "amountY"
    }
}
