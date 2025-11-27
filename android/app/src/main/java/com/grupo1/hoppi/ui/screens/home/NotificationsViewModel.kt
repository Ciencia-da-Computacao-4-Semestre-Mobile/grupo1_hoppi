package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _notifications = mutableStateListOf<NotificationItem>()
    val notifications: List<NotificationItem> get() = _notifications

    init {
        _notifications.addAll(mockNotifications)
    }

    fun acceptRequest(id: Int) {
        val idx = _notifications.indexOfFirst { it.id == id }
        if (idx != -1) {
            val item = _notifications[idx]
            _notifications[idx] = item.copy(
                text = "Solicitação aceita",
                type = NotificationType.ACCEPTED,
                hasAction = false
            )
        }
    }

    fun denyRequest(id: Int) {
        _notifications.removeAll { it.id == id }
    }

    fun setNotificationsForTest(items: List<NotificationItem>) {
        _notifications.clear()
        _notifications.addAll(items)
    }

}
