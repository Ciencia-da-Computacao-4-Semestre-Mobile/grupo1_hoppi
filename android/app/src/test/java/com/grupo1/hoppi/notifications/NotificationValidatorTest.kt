package com.grupo1.hoppi.notifications

import com.grupo1.hoppi.ui.screens.mainapp.NotificationItem
import com.grupo1.hoppi.ui.screens.mainapp.NotificationType
import org.junit.Assert.*
import org.junit.Test

class NotificationValidatorTest {

    @Test
    fun validNotification_returnsTrue() {
        val item = NotificationItem(
            id = 1,
            text = "Mensagem",
            timeAgo = "2h",
            type = NotificationType.LIKE
        )
        assertTrue(NotificationValidator.isValidNotification(item))
    }

    @Test
    fun nullNotification_returnsFalse() {
        assertFalse(NotificationValidator.isValidNotification(null))
    }

    @Test
    fun blankText_returnsFalse() {
        val item = NotificationItem(
            id = 10,
            text = "",
            timeAgo = "1d",
            type = NotificationType.COMMENT
        )
        assertFalse(NotificationValidator.isValidNotification(item))
    }

    @Test
    fun blankTime_returnsFalse() {
        val item = NotificationItem(
            id = 10,
            text = "Comentou algo",
            timeAgo = "",
            type = NotificationType.COMMENT
        )
        assertFalse(NotificationValidator.isValidNotification(item))
    }

    @Test
    fun negativeId_returnsFalse() {
        val item = NotificationItem(
            id = -1,
            text = "Ok",
            timeAgo = "1d",
            type = NotificationType.NEW_FOLLOWER
        )
        assertFalse(NotificationValidator.isValidNotification(item))
    }

    @Test
    fun followRequest_canShowActions() {
        assertTrue(NotificationValidator.canShowActions(NotificationType.FOLLOW_REQUEST))
    }

    @Test
    fun like_cannotShowActions() {
        assertFalse(NotificationValidator.canShowActions(NotificationType.LIKE))
    }
}