package river.exertion.kcop.asset

object AppArgHandler {

    var appArgs = mutableMapOf<String, String?>()

    fun setAppArgs(args: Array<String>) {
        args.forEach {
            if (it.contains(":") ) {
                val param = it.split(":")
                appArgs[param[0]] = param[1]
            } else {
                appArgs[it] = null
            }
        }
    }
}