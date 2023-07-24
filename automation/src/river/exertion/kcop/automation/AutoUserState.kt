package river.exertion.kcop.automation

enum class AutoUserState {

    WATCH // log behaviors into a queue, generate bqueue asset(s)
    , LEARN // optimize queued behaviors to a reward, e.g. cull no-ops so that verifications still pass
    , EXEC // simple execution of a btree or bqueue
}