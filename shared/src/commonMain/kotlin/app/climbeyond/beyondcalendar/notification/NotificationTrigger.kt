package app.climbeyond.beyondcalendar.notification

import kotlin.time.Instant
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus

fun Notification.isDue(nowUtcMs: Long): Boolean = nowUtcMs >= startAtUtcMs

fun Notification.nextOccurrenceAfterUtcMs(firedAtUtcMs: Long): Long? {
    val rule = repeat ?: return null
    val unit = when (rule.unit) {
        RepeatUnit.DAILY -> DateTimeUnit.DAY
        RepeatUnit.WEEKLY -> DateTimeUnit.WEEK
        RepeatUnit.MONTHLY -> DateTimeUnit.MONTH
        RepeatUnit.CUSTOM -> DateTimeUnit.DAY
    }
    return Instant.fromEpochMilliseconds(firedAtUtcMs)
        .plus(rule.interval, unit, TimeZone.UTC)
        .toEpochMilliseconds()
}
