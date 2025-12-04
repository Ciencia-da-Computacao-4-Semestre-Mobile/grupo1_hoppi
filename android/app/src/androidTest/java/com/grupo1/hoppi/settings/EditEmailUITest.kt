package com.grupo1.hoppi.settings

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.ui.screens.settings.account.EditEmailScreen
import com.grupo1.hoppi.ui.screens.settings.account.MOCK_CODE
import org.junit.Rule
import org.junit.Test

class EditEmailUITest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun editEmail_interactionsWork() {
        var backCalled = false

        composeRule.setContent {
            val navController = rememberNavController()
            navController.addOnDestinationChangedListener { _, _, _ ->
                backCalled = true
            }

            NavHost(
                navController = navController,
                startDestination = "editEmail"
            ) {
                composable("editEmail") {
                    EditEmailScreen(navController = navController)
                }
                composable("home") {}
            }
        }

        composeRule.onAllNodes(hasSetTextAction())[0]
            .performTextInput("novoemail@gmail.com")

        composeRule.onNodeWithText("Mudar e-mail").performClick()

        composeRule.onNodeWithText("Digite o código").assertExists()

        composeRule.onAllNodes(hasSetTextAction())[1]
            .performTextInput(MOCK_CODE)

        composeRule.onNodeWithText("Validar").performClick()

        composeRule.onNodeWithText("Sucesso!").assertExists()

        composeRule.onNodeWithText("OK").performClick()

        assert(backCalled)
    }
}
