package river.exertion.thenuim.asset.irlTime

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object IrlTime {
    private fun irlTime() = Clock.System.now()

    private fun localDateTime() = irlTime().toLocalDateTime(TimeZone.currentSystemDefault())

    fun utcLocalDateTime() = irlTime().toLocalDateTime(TimeZone.UTC)
    fun tzLocalDateTime(tz: TimeZone? = null) = irlTime().toLocalDateTime(tz ?: TimeZone.currentSystemDefault())

    private fun localHoursStr() = localDateTime().hour.toString().padStart(2, '0')
    private fun localMinutesStr() = localDateTime().minute.toString().padStart(2, '0')
    private fun localSecondsStr() = localDateTime().second.toString().padStart(2, '0')
    private fun localMillisStr() = localDateTime().nanosecond.toString().padStart(4, '0')

    fun localTime(delim : String? = ":") = "${localHoursStr()}$delim${localMinutesStr()}$delim${localSecondsStr()}"
    fun localTimeMillis(delim : String? = ":", delimMillis : String? = ".") = "${localHoursStr()}$delim${localMinutesStr()}$delim${localSecondsStr()}$delimMillis${localMillisStr()}"
    fun localTimeMillis(delim : String) = localTimeMillis(delim, delim)

}