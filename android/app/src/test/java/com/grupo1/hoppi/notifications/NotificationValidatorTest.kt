package com.grupo1.hoppi.notifications

import com.grupo1.hoppi.ui.screens.mainapp.NotificationItem
import com.grupo1.hoppi.ui.screens.mainapp.NotificationType
import org.junit.Assert.*
import org.junit.Test

class NotificationValidatorTest {

    @Test
    fun `valida notificacao com dados corretos`() {
        val item = NotificationItem(1, "Teste", "1 dia", NotificationType.LIKE)
        assertTrue(NotificationValidator.isValidNotification(item))
    }

    @Test
    fun `recusa notificacao com id invalido`() {
        val item = NotificationItem(0, "Teste", "1 dia", NotificationType.LIKE)
        assertFalse(NotificationValidator.isValidNotification(item))
    }

    @Test
    fun `recusa notificacao com texto vazio`() {
        val item = NotificationItem(1, "", "1 dia", NotificationType.LIKE)
        assertFalse(NotificationValidator.isValidNotification(item))
    }

    @Test
    fun `recusa notificacao com tempo vazio`() {
        val item = NotificationItem(1, "Teste", "", NotificationType.LIKE)
        assertFalse(NotificationValidator.isValidNotification(item))
    }

    @Test
    fun `mostra acoes somente para follow request`() {
        val follow = NotificationItem(1, "pediu", "agora", NotificationType.FOLLOW_REQUEST, true)
        val like = NotificationItem(2, "curtiu", "agora", NotificationType.LIKE)
        assertTrue(NotificationValidator.shouldShowActions(follow))
        assertFalse(NotificationValidator.shouldShowActions(like))
    }
}