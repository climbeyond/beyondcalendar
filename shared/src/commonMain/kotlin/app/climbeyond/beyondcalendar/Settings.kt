package app.climbeyond.beyondcalendar

import app.climbeyond.beyondcalendar.helpers.Colors
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object Settings {
    enum class WeekStart {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }

    val initDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var weekStart = WeekStart.MONDAY
    var colorBgHeader = Colors.bg_header
}
