package river.exertion.kcop.asset.irlTime

import java.time.LocalDateTime
import java.util.*

object IrlTime {
    private fun irlTime() = Calendar.getInstance()

    private fun localDateTime() = LocalDateTime.of(irlTime().get(Calendar.YEAR), irlTime().get(Calendar.MONTH) + 1, irlTime().get(
        Calendar.DAY_OF_MONTH),
        irlTime().get(Calendar.HOUR_OF_DAY), irlTime().get(Calendar.MINUTE), irlTime().get(Calendar.SECOND))

    private fun localHoursStr() = localDateTime().hour.toString().padStart(2, '0')
    private fun localMinutesStr() = localDateTime().minute.toString().padStart(2, '0')
    private fun localSecondsStr() = localDateTime().second.toString().padStart(2, '0')

    fun localTime(delim : String? = ":") = "${localHoursStr()}$delim${localMinutesStr()}$delim${localSecondsStr()}"
}