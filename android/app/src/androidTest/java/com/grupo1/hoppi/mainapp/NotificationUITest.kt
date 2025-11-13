package com.grupo1.hoppi.mainapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.grupo1.hoppi.ui.screens.mainapp.NotificationScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotificationUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun notificationScreen_displaysNotifications_andHandlesButtons() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            NotificationScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("Notificações").assertExists()
        composeTestRule.onNodeWithContentDescription("Voltar").assertExists()

        composeTestRule.onAllNodesWithText("Fulano de Tal").assertCountEquals(7)

        composeTestRule.onNodeWithText("pediu para seguir sua comunidade", substring = true, ignoreCase = true, useUnmergedTree = true)
            .assertExists()
        composeTestRule.onNodeWithText("Comunidade de Estudantes aceitou sua solicitação", substring = true, ignoreCase = true, useUnmergedTree = true)
            .assertExists()
        composeTestRule.onNodeWithText("comentou no seu post", substring = true, ignoreCase = true, useUnmergedTree = true)
            .assertExists()
        composeTestRule.onNodeWithText("começou a seguir você", substring = true, ignoreCase = true, useUnmergedTree = true)
            .assertExists()
        composeTestRule.onNodeWithText("Mostrar mais").assertExists()
        composeTestRule.onAllNodesWithText("Curtiu seu post.", substring = true, ignoreCase = true, useUnmergedTree = true)[0]
            .assertExists()

        composeTestRule.onAllNodesWithContentDescription("Aceitar")[0].performClick()
        composeTestRule.onAllNodesWithContentDescription("Recusar")[0].performClick()
    }
}