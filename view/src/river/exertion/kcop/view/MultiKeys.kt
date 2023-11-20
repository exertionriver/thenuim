package river.exertion.kcop.view

import com.badlogic.gdx.Input

//  ref: https://stackoverflow.com/questions/32851110/libgdx-is-it-possible-to-return-two-keys-held-down-at-the-same-time

//  Usage: add the following to your InputProcessor:

//  override fun keyDown(keycode: Int): Boolean {
//      MultiKeys.keysDown.add(keycode)
//  ...}
//
//  override fun keyUp(keycode: Int): Boolean {
//        MultiKeys.keysDown.remove(keycode)
//  ...}

// singleKeyException() is throw when an entry expects multiple keys and keyDown() is called

enum class MultiKeys {
    SPACE { override fun keysDown() = keysDown.contains(Input.Keys.SPACE) },
    ESCAPE { override fun keysDown() = keysDown.contains(Input.Keys.ESCAPE) },

    EQUALS { override fun keysDown() = keysDown.contains(Input.Keys.EQUALS) && !shiftDown() },
    PLUS { override fun keysDown() = keysDown.contains(Input.Keys.EQUALS) && shiftDown() ; override fun keyDown() = singleKeyException() },

    MINUS { override fun keysDown() = keysDown.contains(Input.Keys.MINUS) && !shiftDown() },
    USCORE { override fun keysDown() = keysDown.contains(Input.Keys.MINUS) && shiftDown() ; override fun keyDown() = singleKeyException() },

    a { override fun keysDown() = keysDown.contains(Input.Keys.A) && !shiftDown() },
    e { override fun keysDown() = keysDown.contains(Input.Keys.E) && !shiftDown() },
    s { override fun keysDown() = keysDown.contains(Input.Keys.S) && !shiftDown() },
    m { override fun keysDown() = keysDown.contains(Input.Keys.M) && !shiftDown() },
    h { override fun keysDown() = keysDown.contains(Input.Keys.H) && !shiftDown() },
    d { override fun keysDown() = keysDown.contains(Input.Keys.D) && !shiftDown() },
    w { override fun keysDown() = keysDown.contains(Input.Keys.W) && !shiftDown() },
    o { override fun keysDown() = keysDown.contains(Input.Keys.O) && !shiftDown() },
    x { override fun keysDown() = keysDown.contains(Input.Keys.X) && !shiftDown() },
    y { override fun keysDown() = keysDown.contains(Input.Keys.Y) && !shiftDown() },
    t { override fun keysDown() = keysDown.contains(Input.Keys.T) && !shiftDown() },
    p { override fun keysDown() = keysDown.contains(Input.Keys.P) && !shiftDown() },

    C { override fun keysDown() = keysDown.contains(Input.Keys.C) && shiftDown() ; override fun keyDown() = singleKeyException() },
    E { override fun keysDown() = keysDown.contains(Input.Keys.E) && shiftDown() ; override fun keyDown() = singleKeyException() },
    M { override fun keysDown() = keysDown.contains(Input.Keys.M) && shiftDown() ; override fun keyDown() = singleKeyException() },
    P { override fun keysDown() = keysDown.contains(Input.Keys.P) && shiftDown() ; override fun keyDown() = singleKeyException() },
    R { override fun keysDown() = keysDown.contains(Input.Keys.R) && shiftDown() ; override fun keyDown() = singleKeyException() },

    D { override fun keysDown() = keysDown.contains(Input.Keys.D) && shiftDown() ; override fun keyDown() = singleKeyException() },
    T { override fun keysDown() = keysDown.contains(Input.Keys.T) && shiftDown() ; override fun keyDown() = singleKeyException() },
    O { override fun keysDown() = keysDown.contains(Input.Keys.O) && shiftDown() ; override fun keyDown() = singleKeyException() },
    A { override fun keysDown() = keysDown.contains(Input.Keys.A) && shiftDown() ; override fun keyDown() = singleKeyException() },
    Z { override fun keysDown() = keysDown.contains(Input.Keys.Z) && shiftDown() ; override fun keyDown() = singleKeyException() },

    NUM_1 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_1) && !shiftDown() },
    NUM_2 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_2) && !shiftDown() },
    NUM_3 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_3) && !shiftDown() },
    NUM_4 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_4) && !shiftDown() },
    NUM_5 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_5) && !shiftDown() },
    NUM_6 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_6) && !shiftDown() },
    NUM_7 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_7) && !shiftDown() },
    NUM_8 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_8) && !shiftDown() },
    NUM_9 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_9) && !shiftDown() },
    NUM_0 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_0) && !shiftDown() },

    BANG_1 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_1) && shiftDown() ; override fun keyDown() = singleKeyException() },
    AT_2 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_2) && shiftDown() ; override fun keyDown() = singleKeyException() },
    HASH_3 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_3) && shiftDown() ; override fun keyDown() = singleKeyException() },
    DOLLAR_4 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_4) && shiftDown() ; override fun keyDown() = singleKeyException() },
    PERCENT_5 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_5) && shiftDown() ; override fun keyDown() = singleKeyException() },
    CARET_6 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_6) && shiftDown() ; override fun keyDown() = singleKeyException() },
    AMPERSAND_7 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_7) && shiftDown() ; override fun keyDown() = singleKeyException() },
    ASTERISK_8 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_8) && shiftDown() ; override fun keyDown() = singleKeyException() },
    OPENPARENS_9 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_9) && shiftDown() ; override fun keyDown() = singleKeyException() },
    CLOSEPARENS_0 { override fun keysDown() = keysDown.contains(Input.Keys.NUM_0) && shiftDown() ; override fun keyDown() = singleKeyException() }
    ;
    open fun keysDown() = false
    open fun keyDown() : Boolean = keysDown() && keysDown.size == 1

    companion object {
        val keysDown = mutableSetOf<Int>()
        fun shiftDown() = keysDown.contains(Input.Keys.SHIFT_LEFT) || keysDown.contains(Input.Keys.SHIFT_RIGHT)
        fun numKeyDown() =
            NUM_1.keyDown() || NUM_2.keyDown() || NUM_3.keyDown() || NUM_4.keyDown() || NUM_5.keyDown() ||
            NUM_6.keyDown() || NUM_7.keyDown() || NUM_8.keyDown() || NUM_9.keyDown() || NUM_0.keyDown()

        fun numPunctDown() =
            BANG_1.keysDown() || AT_2.keysDown() || HASH_3.keysDown() || DOLLAR_4.keysDown() || PERCENT_5.keysDown() ||
            CARET_6.keysDown() || AMPERSAND_7.keysDown() || ASTERISK_8.keysDown() || OPENPARENS_9.keysDown() || CLOSEPARENS_0.keysDown()

        fun numPressed() = MultiKeys.keysDown.first { it in 7..16 } - 7

        fun singleKeyException(): Nothing = throw Exception("keyDown() not supported for multi-key entry, use keysDown() instead.")
    }
}