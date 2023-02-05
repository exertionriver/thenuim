package river.exertion.kcop.system.view

data class InputViewMessage(val targetView : ViewType = ViewType.INPUTS, val event : String, val eventParams : Map<String, Any>) {

    fun getKeyStr() : String = eventParams[KeycodeStrKey]?.toString() ?: eventParams[CharacterKey]?.toString() ?: ""

    fun getScreenX() : Int = eventParams[ScreenXKey]?.toString()?.toInt() ?: 0

    fun getScreenY() : Int = eventParams[ScreenYKey]?.toString()?.toInt() ?: 0

    fun getButton() : Int = eventParams[ButtonKey]?.toString()?.toInt() ?: 0

    companion object {
        fun isPressEvent(event : String) = listOf(KeyDownEvent, TouchDownEvent).contains(event)
        fun isReleaseEvent(event : String) = listOf(KeyUpEvent, TouchUpEvent).contains(event)

        fun isKeyEvent(event : String) = listOf(KeyDownEvent, KeyUpEvent, KeyTypedEvent).contains(event)
        fun isTouchEvent(event : String) = listOf(TouchDownEvent, TouchUpEvent, TouchDraggedEvent).contains(event)

        const val KeyDownEvent = "keyDown"
        const val KeyUpEvent = "keyUp"
        const val KeyTypedEvent = "keyTyped"
        const val TouchDownEvent = "touchDown"
        const val TouchUpEvent = "touchUp"
        const val TouchDraggedEvent = "touchDragged"
        const val MouseMovedEvent = "mouseMoved"
        const val ScrolledEvent = "scrolled"

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
