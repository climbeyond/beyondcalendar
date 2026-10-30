package app.climbeyond.beyondcalendar.notification.storage

import app.climbeyond.beyondcalendar.notification.Notification

interface NotificationStore {
    suspend fun upsert(notification: Notification)
    suspend fun delete(id: String)
    suspend fun get(id: String): Notification?
    suspend fun getAll(): List<Notification>
    suspend fun getDue(nowUtcMs: Long): List<Notification>
}
