package app.climbeyond.beyondcalendar

import androidx.compose.ui.graphics.Color
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

    val initSelectedDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var weekStart = WeekStart.MONDAY
    var colorHeaderBg = Color(0xFF042941)
    var colorHeaderText = Color(0xFFFFFFFF)
    var colorHeaderIconTint = Color(0xFFFFFFFF)
    var colorDayNumber = Color(0xFFFFFFFF)
    var colorDaySelectedBg = Color(0xFF001C30)
    var colorDayTodayBorder = Color(0xFFFFFFFF)
    var colorDayName = Color(0xFFFFFFFF)
}
