package com.grupo1.hoppi.notifications

import com.grupo1.hoppi.ui.screens.mainapp.NotificationItem
import com.grupo1.hoppi.ui.screens.mainapp.NotificationType

object NotificationValidator {
    fun isValidNotification(item: NotificationItem?): Boolean {
        if (item == null) return false
        if (item.id <= 0) return false
        if (item.text.isBlank()) return false
        if (item.timeAgo.isBlank()) return false
        return true
    }

    fun canShowActions(type: NotificationType): Boolean {
        return type == NotificationType.FOLLOW_REQUEST
    }
}
