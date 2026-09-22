package app.climbeyond.beyondcalendar.notification

enum class RepeatUnit { DAILY, WEEKLY, MONTHLY, CUSTOM }

data class RepeatRule(
    val unit: RepeatUnit,
    val interval: Int = 1, // e.g. interval=2, unit=WEEKLY -> every 2 weeks
)
