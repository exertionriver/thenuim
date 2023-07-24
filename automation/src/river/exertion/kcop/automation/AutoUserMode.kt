package river.exertion.kcop.automation

enum class AutoUserMode {

    PLAY // default mode, playback btree or bqueue
    , RECORD // take reference screenshots before and after actions
    , VERIFY // take testing screenshots before and after actions to compare against reference screenshots
}