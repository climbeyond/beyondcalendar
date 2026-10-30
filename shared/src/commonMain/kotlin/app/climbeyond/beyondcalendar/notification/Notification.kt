package app.climbeyond.beyondcalendar.notification

enum class NotificationType {
    EVENT_REMINDER, // fires before the linked event's start (lead time)
    EVENT_START,    // fires at the linked event's start
    EVENT_END,      // fires at the linked event's end
    CUSTOM          // standalone, not tied to a calendar event
}

data class Notification(
    val id: String,
    val title: String,
    val message: String? = null,
    val type: NotificationType,
    val startAtUtcMs: Long,
    val repeat: RepeatRule? = null, // null = one-shot
    val eventId: String? = null,    // links back to a CalendarEvent; null for CUSTOM
)
