package com.grupo1.hoppi.mainapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.grupo1.hoppi.ui.screens.mainapp.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotificationUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun notificationScreen_displaysNotifications_andHandlesButtons() {
        val testViewModel = NotificationsViewModel().apply {
            setNotificationsForTest(
                listOf(
                    NotificationItem(
                        id = 1,
                        text = "pediu para seguir sua comunidade",
                        timeAgo = "2 dias",
                        type = NotificationType.FOLLOW_REQUEST,
                        hasAction = true
                    ),
                    NotificationItem(
                        id = 2,
                        text = "Outro usuário pediu para seguir",
                        timeAgo = "1 dia",
                        type = NotificationType.FOLLOW_REQUEST,
                        hasAction = true
                    ),
                    NotificationItem(
                        id = 3,
                        text = "Comunidade de Estudantes aceitou sua solicitação",
                        timeAgo = "1 sem",
                        type = NotificationType.ACCEPTED
                    ),
                    NotificationItem(
                        id = 4,
                        text = "Curtiu seu post.",
                        timeAgo = "1 sem",
                        type = NotificationType.LIKE
                    )
                )
            )
        }

        composeTestRule.setContent {
            val navController = rememberNavController()
            NotificationScreen(navController = navController, viewModel = testViewModel)
        }

        composeTestRule.onNodeWithText("Notificações").assertExists()
        composeTestRule.onNodeWithContentDescription("Voltar").assertExists()
        composeTestRule.onAllNodesWithText("Fulano de Tal").assertCountEquals(4)

        composeTestRule.onNodeWithText("pediu para seguir sua comunidade", substring = true).assertExists()
        composeTestRule.onNodeWithText("Outro usuário pediu para seguir", substring = true).assertExists()
        composeTestRule.onNodeWithText("Comunidade de Estudantes aceitou sua solicitação", substring = true).assertExists()
        composeTestRule.onNodeWithText("Curtiu seu post.", substring = true).assertExists()

        composeTestRule.onAllNodesWithContentDescription("Aceitar")[0].performClick()
        composeTestRule.onNodeWithText("Solicitação aceita").assertExists()

        composeTestRule.onAllNodesWithContentDescription("Recusar")[0].performClick()
        composeTestRule.onNodeWithText("Outro usuário pediu para seguir").assertDoesNotExist()
    }
}
