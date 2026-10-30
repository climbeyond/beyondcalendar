package app.climbeyond.beyondcalendar.notification

// Platform hook for presenting a Notification; actuals are stubs until WorkManager/UNUserNotificationCenter wiring lands.
expect class NotificationAlerter() {
    fun alert(notification: Notification)
}
